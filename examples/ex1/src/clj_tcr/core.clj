(ns clj-tcr.core
  (:refer-clojure :exclude [test])
  (:require
   babashka.process
   [clj-reload.core :as clj-reload]
   [cognitect.test-runner]
   [kaocha.runner]
   [nextjournal.beholder :as beholder]))

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

(defn tcr [_]
  (let [ok (test)]
    (if ok
      (commit)
      (revert))))

(defn rtcr
  "reload && test && commit || revert"
  [opts]
  (clj-reload/reload)
  (tcr opts))

(comment
  (rtcr {})

  :rcf)

(defonce watcher (atom nil))

(defn rtcr-stop []
  (when-let [w @watcher]
    (beholder/stop w)))

(defn rtcr-watch []
  (rtcr-stop)
  (reset! watcher (beholder/watch rtcr "src" "test")))

(comment
  (rtcr-watch)

  :rcf)
