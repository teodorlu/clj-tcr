(ns clj-tcr.ex1-test
  (:require
   [clojure.test :refer [deftest is]]
   [clj-tcr.ex1 :as subject]))

(deftest f-test
  (is (= 10 (subject/f 4 6))))

(deftest t2
  (is (= 2 2)))
