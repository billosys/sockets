(ns quote-server.quotes
  (:require
    [clojure.java.shell :as shell])
  (:refer-clojure :exclude [get print]))

(defn get
  "Get a quote.

  This functio requires that the `fortune` executable be installed on your
  system."
  []
  (-> "fortune"
      (shell/sh)
      :out))

(defn print
  "Print a quote."
  []
  (clojure.core/print (get)))
