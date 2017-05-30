(ns examples.common
  (:require
    [clojure.java.shell :as shell]))

(def default-port 15099)

(defn get-port
  [port]
  (case port
    nil default-port
    (Integer/parseInt port)))

(defn get-quote
  []
  (:out (shell/sh "fortune")))
