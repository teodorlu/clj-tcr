(ns clj-tcr.ex2-test
  (:require [clj-tcr.ex2 :as sut]
            [clojure.test :as t :refer [deftest is testing]]))

(deftest f-test
  (testing "we can do multiplication all right"
    (is (= 100 (sut/f 10)))))

:gyldig-clojure
