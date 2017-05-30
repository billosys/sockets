(ns examples.udp.quote-server.server
  (:require
    [clojure.core.async :as async]
    [clojure.java.io :as io]
    [examples.common :as common]
    [inet.address :as inet]
    [sockets.datagram.socket :as socket])
  (:import (java.net DatagramPacket
                     DatagramSocket)))

(defn quote-service
  "For any in-coming message, simply ignore it and respond with a quote."
  [in out]
  (async/go-loop []
    (let [dest (async/<! in)]
      (async/>! out (assoc dest :quote (common/get-quote)))
      (recur))))

(defn packet-reader
  [sock]
  (let [buf-len 1
        buf (byte-array buf-len)
        in (async/chan)
        packet (new DatagramPacket buf buf-len)]
    (async/go-loop []
      (do
        (socket/receive sock packet)
        (async/>! in {:remote-addr (.getAddress packet)
                      :remote-port (.getPort packet)}))
      (recur))
    in))

(defn quote-writer
  [sock]
  (let [out (async/chan)]
    (async/go-loop []
      (let [msg (async/<! out)
            packet-text (str "Your quote:\n\n" (:quote msg))
            packet-data (common/str->bytes packet-text)
            packet (new DatagramPacket packet-data
                                       (count packet-data)
                                       (:remote-addr msg)
                                       (:remote-port msg))]
        (socket/send sock packet))
      (recur))
    out))

(defn -main
  "You can start the server like this:
  ```
  $ lein run -m examples.tcp.quote-server.server
  ```

  To connect to the server:
  ```
  $ nc -u localhost 15099
  ```

  Then type away, and enjoy the endless quotes ;-)"
  [& [port & args]]
  (println "Starting server ...")
  (let [sock (socket/create (common/get-port port))]
    (println (format "Listening on udp://%s:%s ..."
                     (inet/host-address (socket/local-address sock))
                     (socket/local-port sock)))
    (async/go
      (quote-service
        (packet-reader sock)
        (quote-writer sock)))
    (.join (Thread/currentThread))))
