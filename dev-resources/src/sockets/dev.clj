(ns sockets.dev
  (:require
    [clojure.core.async :as async]
    [clojure.java.shell :as shell]
    [clojure.tools.namespace.repl :as repl]
    [quote-server.quotes :as quotes]
    [trifl.java :refer [show-methods]]))

(defn run
  []
  :ok)

(defn refresh
  ([]
    (repl/refresh))
  ([& args]
    (apply #'repl/refresh args)))

(defn reset []
  (refresh :after 'sockets.dev/run))
