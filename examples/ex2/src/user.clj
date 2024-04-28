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
    (println "trying")
    (reload)
    (test)
    (commit)
    (println "success")
    (catch Exception _
      (println "failure")
      (revert)
      (reload))))

(comment
  (reload)
  (test)
  (tcr)

  :rcf)
