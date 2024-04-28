(ns clj-tcr.core
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [clojure.test :as test]
   [kaocha.runner]))

(defn load-all-tests []
  (require '[clj-tcr.ex1-test]))

(defn all-tests-green? []
  (let [{:keys [fail error]} (test/run-all-tests)]
    (zero? (+ fail error))))

(defn assert-all-tests-green
  []
  (assert (all-tests-green?)))

(defn test [] true)
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
