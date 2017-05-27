# Development Planning

## First Iteration

1. Take a slightly different approach to Java wrapping than what
   [clj-sockets]() and [async-sockets]() have done:
   * Follow the Java class taxonomy closely for low-level protocols
      and implementations
   * Provide a higher-level, more deveoloper-facing (developer-friendly)
      API
   * Take inspiration from:
      * [pyr's mesos Java-wrapper library]()
      * [clojang jiface Java-wrapper library]()
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
1. Sketch out how this would be matched in Clojure
1. Wrap the necessary Java classes
1. Provide a working example in Clojure of client and server
1. Update the example (or create a new one) that would show instead how this
   would be done ideally in Clojure
   * Possibly wrap the Clojure wrapper in a higher-level namespace
   * Provide a new example that's simpler to use than the example that is a
     straight translation of the Java example
1. Introduce command channels with `core.async`

## Second Iteration

TCP

## Third Iteration

SSL

## Fourth Iteration

`java.nio`