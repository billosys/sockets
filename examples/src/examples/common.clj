(ns examples.common
  (:require
    [clojure.java.shell :as shell]))

(def max-packet-size 4096)
(def default-port 15099)

(defn get-port
  [port]
  (case port
    nil default-port
    (Integer/parseInt port)))

(defn get-quote
  []
  (:out (shell/sh "fortune")))

(defn str->bytes
  [text]
  (byte-array (map byte text)))

(defn bytes->str
  [data]
  (new String data))
