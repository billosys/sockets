# Development Planning

## First Iteration

1. Take a slightly different approach to Java wrapping than what
   [clj-sockets](https://github.com/atroche/clj-sockets) and
   [async-sockets](https://github.com/bguthrie/async-sockets) have done:
   * Follow the Java class taxonomy closely for low-level protocols
      and implementations
   * Provide a higher-level, more deveoloper-facing (developer-friendly)
      API
   * Take inspiration from:
      * [pyr's mesos Java-wrapper library](https://github.com/pyr/mesomatic)
      * [clojang jiface Java-wrapper library](https://github.com/clojang/jiface)
      * [bjorstrom's inet-address library](https://github.com/billosys/inet-address)
1. Support both TCP and UDP sockets
   * Other libraries have focused on supporting TCP primarily
   * We have a need for UDP servers
   * We'll focus there first, then then catch up with TCP later
1. Read the Java docs carefully
   * [SocketImpl](https://docs.oracle.com/javase/8/docs/api/java/net/SocketImpl.html)
     * [Socket](https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html)
     * [SSLSocket](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLSocket.html)
     * [DatagramSocketImpl](https://docs.oracle.com/javase/8/docs/api/java/net/DatagramSocketImpl.html)
     * [DatagramSocket](https://docs.oracle.com/javase/8/docs/api/java/net/DatagramSocket.html)
     * [ServerSocket](https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html)
     * [SSLServerSocket](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLServerSocket.html)
   * [SocketOptions](https://docs.oracle.com/javase/8/docs/api/java/net/SocketOptions.html)
   * [InetAddress](https://docs.oracle.com/javase/8/docs/api/java/net/InetAddress.html)
     * [Inet4Address](https://docs.oracle.com/javase/8/docs/api/java/net/Inet4Address.html)
     * [Inet6Address](https://docs.oracle.com/javase/8/docs/api/java/net/Inet6Address.html)
   * [SocketAddress](https://docs.oracle.com/javase/8/docs/api/java/net/SocketAddress.html)
   * [InetSocketAddress](https://docs.oracle.com/javase/8/docs/api/java/net/InetSocketAddress.html)
   * [Proxy](https://docs.oracle.com/javase/8/docs/api/java/net/Proxy.html)
   * [DatagramPacket](https://docs.oracle.com/javase/8/docs/api/java/net/DatagramPacket.html)
1. Document a Java example of creating:
   * A UDP socket server
   * A UDP socket client
   * See [Writing a Datagram Client and Server](https://docs.oracle.com/javase/tutorial/networking/datagrams/clientServer.html)
     * [Server code](https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/networking/datagrams/examples/QuoteServer.java)
     * [Server thread code](https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/networking/datagrams/examples/QuoteServerThread.java)
     * [Client code](https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/networking/datagrams/examples/QuoteClient.java)
1. Sketch out how this would be matched in Clojure
   * Check out these:
     * [Echo server with core.async](https://gist.github.com/dokkarr/acd175677672454499d76110f1fe354e)
     * [async-sockets socket servers](https://github.com/bguthrie/async-sockets/blob/master/src/com/gearswithingears/async_sockets.clj)
     * [core.async talk](https://github.com/halgari/clojure-conj-2013-core.async-examples/blob/master/src/clojure_conj_talk/core.clj) - see the section of code titled "Limited Access to a Shared Resource"
1. Wrap the necessary Java classes
1. Provide a working example in Clojure of client and server
1. Update the example (or create a new one) that would show instead how this
   would be done ideally in Clojure
   * Possibly wrap the Clojure wrapper in a higher-level namespace
   * Provide a new example that's simpler to use than the example that is a
     straight translation of the Java example
1. Introduce command channels with `core.async`

### Clojure Sketch

**A pre-implementation imagining of creating UDP servers**

```clj
(ns examples.udp.echo-server.server
  (:require
    [clojure.core.async :as async]
    [clojure.java.io :as io]
    [inet.address :as inet]
    [sockets.datagram.packet :as packet]
    [sockets.datagram.socket :as socket]))

(def max-packet-size 4096)
(def default-port 15099)

(defn str->bytes
  [text]
  (byte-array (map byte text)))

(defn bytes->str
  [data]
  (new String data))

(defn get-port
  [port]
  (case port
    nil default-port
    (Integer/parseInt port)))

(defn echo-service
  "For any in-coming message, simply return the same data."
  [in out]
  (async/go-loop []
    (let [dest (async/<! in)]
      (async/>! out dest)
      (recur))))

(defn packet-reader
  [sock]
  (let [in (async/chan)]
    (async/go-loop []
      (let [pkt (socket/receive sock max-packet-size)]
        (async/>! in {:remote-addr (packet/address pkt)
                      :remote-port (packet/port pkt)
                      :data (bytes->str (packet/data pkt))}))
      (recur))
    in))

(defn echo-writer
  [sock]
  (let [out (async/chan)]
    (async/go-loop []
      (let [msg (async/<! out)
            pkt-text (format "Echoing: %s\n" (:data msg))
            pkt-data (str->bytes pkt-text)
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
  (let [sock (socket/create (get-port port))]
    (println (format "Listening on udp://%s:%s ..."
                     (inet/host-address (socket/local-address sock))
                     (socket/local-port sock)))
    (async/go
      (echo-service
        (packet-reader sock)
        (echo-writer sock)))
    (.join (Thread/currentThread))))
```

## Second Iteration

TCP

## Third Iteration

SSL

## Fourth Iteration

`java.nio`
