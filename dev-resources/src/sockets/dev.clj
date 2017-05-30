(ns sockets.dev
  (:require
    [clojure.core.async :as async]
    [clojure.java.io :as io]
    [clojure.java.shell :as shell]
    [clojure.pprint :refer [pprint]]
    [clojure.tools.namespace.repl :as repl]
    [examples.tcp.echo-server.server :as tcp-echo-server]
    [examples.tcp.quote-server.server :as tcp-quote-server]
    [examples.udp.echo-server.server :as udp-echo-server]
    [examples.udp.quote-server.client :as udp-quote-client]
    [examples.udp.quote-server.server :as udp-quote-server]
    [sockets.datagram.packet :as datagram-packet]
    [sockets.datagram.socket :as datagram-socket]
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
