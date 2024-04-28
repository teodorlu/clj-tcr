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

Example Clojure `tcr` function that relies on Cognitect's test runner:

``` clojure

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

Same as for Emacs --- but instead of an Emacs Lisp function that saves, reloads and tests, we'll create a key binding that chains multiple commands:

https://blog.agical.se/en/posts/vs-code-runcommands-for-multi-commands-keyboard-shortcuts/

TODO:

1. Find out how to save all open tabs
2. Find out how to use REPL commands within other commands.
