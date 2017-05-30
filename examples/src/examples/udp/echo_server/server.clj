(ns examples.udp.echo-server.server
  (:require
    [clojure.core.async :as async]
    [clojure.java.io :as io]
    [examples.common :as common]
    [inet.address :as inet]
    [sockets.datagram.packet :as packet]
    [sockets.datagram.socket :as socket]))

(defn echo-service
  "For any in-coming message, simply return the same data."
  [in out]
  (async/go-loop []
    (let [dest (async/<! in)]
      (async/>! out dest)
      (recur))))

(defn packet-reader
  [sock]
  (let [in (async/chan)
        buf-len 4096
        buf (byte-array buf-len)
        pkt (packet/create buf buf-len)]
    (async/go-loop []
      (do
        (socket/receive sock pkt)
        (async/>! in {:remote-addr (packet/address pkt)
                      :remote-port (packet/port pkt)
                      :data (common/bytes->str (packet/data pkt))}))
      (recur))
    in))

(defn echo-writer
  [sock]
  (let [out (async/chan)]
    (async/go-loop []
      (let [msg (async/<! out)
            pkt-text (format "Echoing: %s\n" (:data msg))
            pkt-data (common/str->bytes pkt-text)
            pkt (packet/create pkt-data
                               (count pkt-data)
                               (:remote-addr msg)
                               (:remote-port msg))]
        (socket/send sock pkt))
      (recur))
    out))

(defn -main
  "You can start the server like this:
  ```
  $ lein run -m examples.udp.echo-server.server
  ```

  To connect to the server:
  ```
  $ nc -u localhost 15099
  ```

  Then type away, and enjoy the endless echo chamber ;-)"
  [& [port & args]]
  (println "Starting server ...")
  (let [sock (socket/create (common/get-port port))]
    (println (format "Listening on udp://%s:%s ..."
                     (inet/host-address (socket/local-address sock))
                     (socket/local-port sock)))
    (async/go
      (echo-service
        (packet-reader sock)
        (echo-writer sock)))
    (.join (Thread/currentThread))))
