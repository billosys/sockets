(ns examples.udp.quote-server.client
  (:require
    [clojure.core.async :as async]
    [clojure.java.io :as io]
    [examples.common :as common]
    [inet.address :as inet])
  (:import (java.net DatagramPacket
                     DatagramSocket)))

(defn send-request
  [sock port]
  (let [buf-len 1
        buf (byte-array buf-len)
        in (async/chan)
        packet (new DatagramPacket buf
                                   buf-len
                                   (inet/create [127 0 0 1])
                                   port)]
    (.send sock packet)))

(defn get-response
  [sock]
  (let [buf-len 4096
        buf (byte-array buf-len)
        packet (new DatagramPacket buf buf-len)]
    (.receive sock packet)
    (println (str "\n" (new String (.getData packet))))))

(defn -main
  "Before running the client, make sure the UDP quote server is running.

  You can run the client like this:
  ```
  $ lein run -m examples.udp.quote-server.client
  ```

  "
  [& [port & args]]
  (let [sock (new DatagramSocket)
        port (common/get-port port)]
    (println (format "Connecting to udp://127.0.0.1:%s ..." port))
    (send-request sock port)
    (get-response sock)
    (.close sock)))
