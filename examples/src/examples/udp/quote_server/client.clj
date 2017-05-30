(ns examples.udp.quote-server.client
  (:require
    [clojure.core.async :as async]
    [clojure.java.io :as io]
    [examples.common :as common]
    [inet.address :as inet]
    [sockets.datagram.packet :as packet]
    [sockets.datagram.socket :as socket]))

(def max-quote-size 4096)

(defn send-request
  [sock port]
  (let [in (async/chan)
        buf-len 1
        buf (byte-array buf-len)
        pkt (packet/create buf
                           buf-len
                           (inet/create [127 0 0 1])
                           port)]
    (socket/send sock pkt)))

(defn get-response
  [sock]
  (let [pkt (socket/receive sock common/max-packet-size)]
    (println (str "\n" (common/bytes->str (packet/data pkt))))))

(defn -main
  "Before running the client, make sure the UDP quote server is running.

  You can run the client like this:
  ```
  $ lein run -m examples.udp.quote-server.client
  ```

  Your quote will appear on `stdout` and the client will exit."
  [& [port & args]]
  (let [sock (socket/create)
        port (common/get-port port)]
    (println (format "Connecting to udp://127.0.0.1:%s ..." port))
    (send-request sock port)
    (get-response sock)
    (.close sock)))
