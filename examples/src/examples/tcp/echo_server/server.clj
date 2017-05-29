(ns examples.tcp.echo-server.server
  (:require
    [clojure.core.async :as async]
    [clojure.java.io :as io])
  (:import (java.net ServerSocket)))

(defn echo-service
  [in out]
  (async/go-loop []
    (let [msg (async/<! in)]
      (async/>! out msg)
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
             (str "Echoing: ")
             (.write writer)))
      (.flush writer)
      (recur))
    out))

(defn -main
  "You can start the server like this:
  ```
  $ lein run -m examples.tcp.echo-server.server
  ```

  To connect to the server:
  ```
  $ telnet localhost 12345
  ```

  Then type away, and enjoy the echo chamber ;-)"
  []
  (println "Starting server ...")
  (let [server (ServerSocket. 12345)
        sock (.accept server)]
    (println (format "Listening on socket %s ..." sock))
    (async/go
      (echo-service
        (loop-socket-reader sock)
        (loop-socket-writer sock)))
    (.join (Thread/currentThread))))
