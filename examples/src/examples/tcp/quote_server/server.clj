(ns examples.tcp.quote-server.server
  (:require
    [clojure.core.async :as async]
    [clojure.java.io :as io]
    [clojure.java.shell :as shell])
  (:import (java.net ServerSocket)))

(defn quote-service
  "For any in-coming message, simply ignore it and respond with a quote."
  [in out]
  (async/go-loop []
    (let [msg (async/<! in)]
      (async/>! out (:out (shell/sh "fortune")))
      (recur))))

(defn loop-socket-reader
  [sock]
  (let [in (async/chan)]
    (async/go-loop []
      (do
        (->> sock
             (io/reader)
             (.readLine)
             (async/>! in)))
      (recur))
    in))

(defn loop-socket-writer
  [sock]
  (let [out (async/chan)
        writer (io/writer sock)]
    (async/go-loop []
      (do
        (->> out
             (async/<!)
             (str "Your quote:\n\n")
             (.write writer)))
      (.flush writer)
      (recur))
    out))

(defn -main
  "You can start the server like this:
  ```
  $ lein run -m examples.tcp.quote-server.server
  ```

  To connect to the server:
  ```
  $ telnet localhost 15099
  ```

  Then type away, and enjoy the echo chamber ;-)"
  []
  (println "Starting server ...")
  (let [server (ServerSocket. 15099)
        sock (.accept server)]
    (println (format "Listening on socket %s ..." sock))
    (async/go
      (quote-service
        (loop-socket-reader sock)
        (loop-socket-writer sock)))
    (.join (Thread/currentThread))))
