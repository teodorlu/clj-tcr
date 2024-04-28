(ns dev
  (:require
   [clj-tcr.core :as clj-tcr]))

(comment

  (clj-tcr/all-tests-green?)
  ;; => false

  (clj-tcr/assert-all-tests-green)

  :rcf)
