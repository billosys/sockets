(ns systems.billo.datagram.socket
  (:import (java.net DatagramSocket)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Protocol   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defprotocol Socket
  (bind [this addr]
    "Binds this DatagramSocket to a specific address and port.")
  (close [this]
    "Closes this datagram socket.")
  (connect [this addr] [this addr port]
    "For the 1-arity function, connects this socket to a remote socket address
    (IP address + port number; must be an instance or subclass of
    `SocketAddress`). For the 2-arity function, connects the socket to a remote
    address amd port for this socket (the address must be an instance or
    subclass of `InetAddress`).")
  (disconnect [this]
    "Disconnects the socket.")
  (broadcast? [this]
    "Tests if SO_BROADCAST is enabled.")
  (channel [this]
    "Returns the unique `DatagramChannel` object associated with this datagram
    socket, if any.")
  (inet-address [this]
    "Returns the address to which this socket is connected.")
  (local-address [this]
    "Gets the local address to which the socket is bound.")
  (local-port [this]
    "Returns the port number on the local host to which this socket is bound.")
  (local-socket-address [this]
    "Returns the address of the endpoint this socket is bound to.")
  (port [this]
    "Returns the port number to which this socket is connected.")
  (receive-buffer-size [this]
    "Get value of the `SO_RCVBUF option for this `DatagramSocket`, that is the
    buffer size used by the platform for input on this `DatagramSocket`.")
  (remote-socket-address [this]
    "Returns the address of the endpoint this socket is connected to, or null
    if it is unconnected.")
  (reuse-address [this]
    "Tests if `SO_REUSEADDR` is enabled.")
  (send-buffer-size [this]
    "Get value of the `SO_SNDBUF` option for this `DatagramSocket`, that is the
    buffer size used by the platform for output on this `DatagramSocket`.")
  (so-timeout [this]
    "Retrieve setting for `SO_TIMEOUT`.")
  (traffic-class [this]
    "Gets traffic class or type-of-service in the IP datagram header for
    packets sent from this `DatagramSocket`.")
  (bound? [this]
    "Returns the binding state of the socket.")
  (closed? [this]
    "Returns whether the socket is closed or not.")
  (connected? [this]
    "Returns the connection state of the socket.")
  (receive [this packet]
    "Receives a datagram packet from this socket.")
  (send [this packet]
    "Sends a datagram packet from this socket.")
  (broadcast! [this bool]
    "Enable/disable `SO_BROADCAST`.")
  (receive-buffer-size! [this size]
    "Sets the `SO_RCVBUF` option to the specified value for this
    `DatagramSocket`.")
  (reuse-address! [this bool]
    "Enable/disable the `SO_REUSEADDR` socket option.")
  (send-buffer-size! [this size]
    "Sets the `SO_SNDBUF` option to the specified value for this
    `DatagramSocket`.")
  (so-timeout! [this timeout]
    "Enable/disable `SO_TIMEOUT` with the specified timeout, in milliseconds.")
  (traffic-class! [this class-int]
    "Sets traffic class or type-of-service octet in the IP datagram header for
    datagrams sent from this `DatagramSocket`."))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def behaviour
  {:bind (fn [this addr] (.bind this addr))
   :close (fn [this] (.close this))
   :connect (fn ([this addr]
                 (.connect this addr))
                ([this addr port]
                 (.connect this addr port)))
   :disconnect (fn [this] (.disconnect this))
   :broadcast? (fn [this] (.getBroadcast this))
   :channel (fn [this] (.getChannel this))
   :inet-address (fn [this] (.getInetAddress this))
   :local-address (fn [this] (.getLocalAddress this))
   :local-port (fn [this] (.getLocalPort this))
   :local-socket-address (fn [this] (.getLocalSocketAddress this))
   :port (fn [this] (.getPort this))
   :receive-buffer-size (fn [this] (.getReceiveBufferSize this))
   :remote-socket-address (fn [this] (.getRemoteSocketAddress this))
   :reuse-address (fn [this] (.getReuseAddress this))
   :send-buffer-size (fn [this] (.getSendBufferSize this))
   :so-timeout (fn [this] (.getSoTimeout this))
   :traffic-class (fn [this] (.getTrafficClass this))
   :bound? (fn [this] (.isBound this))
   :closed? (fn [this] (.isClosed this))
   :connected? (fn [this] (.isConnected this))
   :receive (fn [this packet] (.receive this packet))
   :send (fn [this packet] (.send this packet))
   :broadcast! (fn [this bool] (.setBroadcast this bool))
   :receive-buffer-size! (fn [this size] (.setReceiveBufferSize this size))
   :reuse-address! (fn [this bool] (.setReuseAddress this bool))
   :send-buffer-size! (fn [this size] (.setSendBufferSize this size))
   :so-timeout! (fn [this timeout] (.setSoTimeout this timeout))
   :traffic-class! (fn [this class-int] (.setTrafficClass this class-int))})

(extend DatagramSocket Socket behaviour)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constructors   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create
  "A constructor for datagram sockets. This function my take 0, 1, or two args.

  * 0-arity: Constructs a datagram socket and binds it to any available port
    on the local host machine.
  * 1-arity: If the argument is an instance of the `DatagramSocketImpl` class,
    Creates an unbound datagram socket with the specified `DatagramSocketImpl`.
    If the argument is an integer, it is interpreted as a port, in which
    caseC, datagram socket will be constructed and bound to the specified port
    on the local host machine. If the argument is an instance of
    `SocketAddress`, the constructor creates a datagram socket, bound to the
    specified local socket address.
  * 2-arity: Creates a datagram socket, bound to the specified local address."
  ([]
    (new DatagramSocket))
  ([arg]
    (new DatagramSocket arg))
  ([port local-addr]
    (new DatagramSocket) port local-addr))
