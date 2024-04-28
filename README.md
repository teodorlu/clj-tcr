**⚠️ THIS PROJECT IS HIGHLY EXPERIMENTAL, PROCEED AT YOUR OWN RISK ⚠️**

# Goal: enable TCR-powered workflows for Clojure without loosing REPL-driven goodies

xx

## Configuring Emacs for TCR with Clojure

We recommend the following approach for implementing TCR with Clojure:

1. Create a `tcr` function in `user.clj`

2. Create a `tcr` function in your Emacs config that:

    1. Ensure `auto-revert-mode` is active (otherwise we don't see new file states on disk)

    2. Saves all project buffers

    3. Evaluates `clj-reload.core/reload` with CIDER

    4. Evaluates `user/tcr` with CIDER

Example Clojure `tcr` function that relies on Cognitect's test runner.

You'll want to write this function yourself, so that you can run the tests you care about.
Here is an example:

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
  (babashka.process/shell "git commit --allow-empty -m working"))

(defn revert []
  (babashka.process/shell "git reset --hard HEAD"))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn tcr
  "TCR RELOADED: AN IN-PROCESS INTERACTIVE LOOP"
  []
  (try
    (reload)
    (test)
    (commit)
    (catch Exception _
      (revert)
      (reload))))
```

Example Emacs Lisp TCR function:

``` emacs-lisp
(defun teod-clj-tcr ()
  (interactive)
  (auto-revert-mode 1)
  (projectile-save-project-buffers)
  (cider-interactive-eval "(clj-reload.core/reload)")
  (cider-interactive-eval "(user/tcr)"))
```

Bind the function to `SPC ø t` in Doom Emacs:

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
