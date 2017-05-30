(ns sockets.datagram.packet
  (:refer-clojure :exclude [bound? send])
  (:import (java.net DatagramPacket)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Protocol   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defprotocol Packet
  (address [this]
    "Returns the IP address of the machine to which this datagram is being sent
    or from which the datagram was received.")
  (data [this]
    "Returns the data buffer.")
  (length [this]
    "Returns the length of the data to be sent or the length of the data
    received.")
  (offset [this]
    "Returns the offset of the data to be sent or the offset of the data
    received.")
  (port [this]
    "Returns the port number on the remote host to which this datagram is being
    sent or from which the datagram was received.")
  (socket-address [this]
    "Gets the SocketAddress (usually IP address + port number) of the remote
    host that this packet is being sent to or is coming from.")
  (address! [this addr]
    "Sets the IP address of the machine to which this datagram is being sent.")
  (data! [this bytes] [this bytes offset len]
    "Set the data buffer for this packet.")
  (length! [this len]
    "Set the length for this packet.")
  (port! [this port]
    "Sets the port number on the remote host to which this datagram is being
    sent.")
  (receive-buffer-size [this]
    "Sets the `SocketAddress` (usually IP address + port number) of the remote
    host to which this datagram is being sent."))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def behaviour
  {:address (fn [this] (.getAddress this))
   :data (fn [this] (.getData this))
   :length (fn [this] (.getLength this))
   :offset (fn [this] (.getOffset this))
   :port (fn [this] (.getPort this))
   :socket-address (fn [this] (.getSocketAddress this))
   :address! (fn [this addr] (.setAddress this addr))
   :data! (fn ([this bytes]
                (.setData this bytes))
              ([this bytes offset len]
                (.setData this bytes offset len)))
   :length! (fn [this len] (.setLength this len))
   :port! (fn [this port] (.setPort this port))
   :socket-address! (fn [this addr] (.setSocketAddress this addr))})

(extend DatagramPacket Packet behaviour)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constructors   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create
  "A constructor for datagram packets. This function my take 1, 2, 3, 4, or 5
  args.

  * 1-arity - This is a Clojure-only convenience constructor that creates a
    `byte-array` of the desired length, suitable for receiving packets.
  * 2-arity - Constructs a `DatagramPacket` for receiving packets of length
    `len`.
  * 3-arity - Either of:
    * Constructs a `DatagramPacket` for receiving packets of length `arg3`,
      specifying an offset of `arg2` into the buffer;
    * Constructs a datagram packet for sending packets of length `arg2`
      to the specified `SocketAddress` of `arg3`.
  * 4-arity - Either of:
    * Constructs a datagram packet for sending packets of length `arg2`
      to port `arg4` on the specified host (`InetAddress`) `arg3`.
    * Constructs a datagram packet for sending packets of length `arg3`
      with offset `arg2` to the specified `SocketAddress` of `arg4`.
  * 5-arity - Constructs a datagram packet for sending packets of length
    `len` with offset `offset` to the specified port `port` on the specified
    host (`InetAddress`) `addr`."
  ([len]
    (new DatagramPacket (byte-array len) len))
  ([buf len]
    (new DatagramPacket buf len))
  ([buf arg2 arg3]
    (new DatagramPacket buf arg2 arg3))
  ([buf arg2 arg3 arg4]
    (new DatagramPacket buf arg2 arg3 arg4))
  ([buf offset len addr port]
    (new DatagramPacket buf offset len addr port)))
