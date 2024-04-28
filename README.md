**⚠️ THIS PROJECT IS HIGHLY EXPERIMENTAL, PROCEED AT YOUR OWN RISK ⚠️**

# Goal: enable TCR-powered workflows for Clojure without loosing REPL-driven goodies

Traditional TCR sucks with Clojure because JVM restarts produces a slow feedback loop.

We can enable a fast feedback loop by not restarting the JVM between _test_ cycles.
Read on to understand how.

## A possible TCR script for your `user` namespace

In "vanilla" TCR, _reload_ is implied as part of the `test` operation.
This assumption doesn't hold for how people develop Clojure in practice.
We can tweak the TCR process by inserting approperiate _reload_ operations where needed.

We'll be using `tonsky/clj-reload` for this. Thank you, Niki!

Here's how your `user.clj` could look:

``` clojure
(ns user
  (:refer-clojure :exclude [test])
  (:require
   babashka.process
   clj-reload.core
   cognitect.test-runner))

(defn reload [] (clj-reload.core/reload))

(defn test []
  (let [{:keys [fail error]} (cognitect.test-runner/test {})]
    (assert (zero? (+ fail error)))))

(defn commit []
  (babashka.process/shell "git add .")
  (babashka.process/shell "git commit -m working"))

(defn revert []
  (babashka.process/shell "git reset --hard HEAD"))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn tcr
  "TCR RELOADED: AN IN-PROCESS INTERACTIVE LOOP"
  []
  (try
    (test)
    (commit)
    (println "success")
    (catch Exception _
      (println "failure")
      (revert)
      (reload) ; In those cases where we revert, we choose to clean up our mess
               ; -- don't leave the user with a REPL out of sync with their files.
      )))
```

Differences from vanilla TCR:

1. We reload before we test
2. We reload after we revert if we revert.

If you want to do your own thing:

1. Tweak your _test_, _commit_, _revert_ and _reload_ steps to work for you
2. Expose them in a function. Call that function _tcr_ or something else
3. In your editor, set up a key binding to save all files, do a reload, then call the _tcr_ function.

## Configuring Emacs for TCR with Clojure

We will create an Emacs Lisp function that:

1. Ensures `auto-revert-mode` is active (otherwise we don't see new file states on disk)
2. Saves all project buffers
3. Evaluates `clj-reload.core/reload` with CIDER
4. Evaluates `user/tcr` with CIDER

Here's one way to do that:

``` emacs-lisp
(defun teod-clj-tcr ()
  (interactive)
  (auto-revert-mode 1)
  (projectile-save-project-buffers)
  (cider-interactive-eval "(clj-reload.core/reload)")
  (cider-interactive-eval "(user/tcr)"))
```

To bind that function to `SPC ø t` in Doom Emacs:

``` emacs-lisp
(map! :g "M-RET" #'teod-clj-tcr)
```

## Configuring Calva for TCR with Clojure

With Calva, we can use a `runCommands` command to run multiple commands, then `calva.runCustomREPLCommand` to interact with our REPL.

Peter Strömberg describes `runCommands` nicely here:

https://blog.agical.se/en/posts/vs-code-runcommands-for-multi-commands-keyboard-shortcuts/

I chose to bind the editor TCR action to `Cmd+ø`.
You may of course choose whatever key binding you prefer.

If you don't want `Cmd+ø` as the key binding (perhaps because there's no letter `ø` on your keyboard), please choose a key binding that works for you.

``` json
    {
        "key": "cmd+[Semicolon]",
        "command": "runCommands",
        "args": {
            "commands": [
                "workbench.action.files.saveFiles",
                {
                    "command": "calva.runCustomREPLCommand",
                    "args": {
                        "snippet": "(clj-reload.core/reload)"
                    }
                },
                {
                    "command": "calva.runCustomREPLCommand",
                    "args": {
                        "snippet": "(user/tcr)"
                    }
                },
            ]
        }
    }
```
