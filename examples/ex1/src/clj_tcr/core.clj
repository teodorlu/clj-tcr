(ns clj-tcr.core
  (:refer-clojure :exclude [test])
  (:require
   babashka.process
   [clj-reload.core :as clj-reload]
   [cognitect.test-runner]
   [kaocha.runner]))

(defn run-tests []
  (cognitect.test-runner/test {}))

(defn all-tests-green? []
  (let [{:keys [fail error]} (run-tests)]
    (zero? (+ fail error))))

(defn test []
  (all-tests-green?))

(defn commit []
  (babashka.process/shell "git add .")
  (babashka.process/shell "git commit -m working"))

(defn revert []
  (babashka.process/shell "git reset --hard HEAD"))

(defn tcr []
  (clj-reload/reload)
  (let [ok (test)]
    (if ok
      (commit)
      (revert))))

(defn autotcr [])

;; clj tcr!
