(ns clj-tcr.core
  (:refer-clojure :exclude [test])
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [clojure.test :as test]
   [cognitect.test-runner]
   [kaocha.runner]))

(defn run-tests []
  (cognitect.test-runner/test {}))

(defn all-tests-green? []
  (let [{:keys [fail error]} (run-tests)]
    (zero? (+ fail error))))

(defn assert-all-tests-green
  []
  (assert (all-tests-green?)))

(defn test []
  (all-tests-green?))
(defn commit [])
(defn revert [])

(defn tcr []
  (let [ok (test)]
    (if ok
      (commit)
      (revert))))


(defn autotcr [])

;; clj tcr!

(comment

  (load-all-tests)

  (test/run-all-tests)

  (all-tests-green?)

  (assert-all-tests-green)

  :rcf)
