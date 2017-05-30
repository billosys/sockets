(ns examples.udp.quote-server.client
  (:require
    [clojure.java.io :as io]
    [examples.common :as common]
    [inet.address :as inet]
    [sockets.datagram.packet :as packet]
    [sockets.datagram.socket :as socket]))

(defn send-request
  [sock port]
  (socket/send sock
               (-> (packet/create 1)
                   (packet/update-address (inet/create [127 0 0 1]))
                   (packet/update-port port))))

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
