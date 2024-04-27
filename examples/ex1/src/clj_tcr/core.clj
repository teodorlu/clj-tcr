(ns clj-tcr.core
  (:require [clojure.test :as test]))

(defn all-tests-green? []
  (let [{:keys [fail error]} (test/run-all-tests)]
    (zero? (+ fail error))))

(defn assert-all-tests-green
  []
  (assert (all-tests-green?)))

;; clj tcr!

(comment

  (all-tests-green?)

  (assert-all-tests-green)

  :rcf)
