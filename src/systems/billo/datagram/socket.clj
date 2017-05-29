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
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX")
  (xxx [this]
    "XXX"))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
