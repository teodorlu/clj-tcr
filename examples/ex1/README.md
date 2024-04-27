# TCR for Clojure, example 1

We make TCR for Clojure by just using it.

This folder is a standalone Clojure project that we aim to set up for TCR.

## Open questions

- TCR as a REPL action or as an editor action?
- When TCR is run as a REPL action, do we want a file watcher (`user/autotcr`) or a oneoff action (`user/tcr`)?

## REPL action or editor action?

Arguments for REPL action:

1. Programmers using different editors get the same experience.
2. We can write Clojure rather than a mix of Emacs Lisp, Javascript, Vimscript, Lua (and other languages)

Advantages in favor of editor action:

1. The editor knows which buffers contain unsaved changes, and can force an atomic save of _everything_, and do that _before_ referting stuff
2. Editor action is faster than user save, then listen to the file save.

Synthesis, design that enables both:

1. Make a low-level API that can be used from the REPL
2. Implement `autotcr` in terms of that low-level API and a file watcher (like `nextjournal/beholder`)
3. Implement a reference package (eg `clj-tcr.el`) for an editor, showing an example of how to set up the editor for TCR

