# sockets
[![Build Status][travis-badge]][travis][![Dependencies Status][deps-badge]][deps][![Clojars Project][clojars-badge]][clojars][![Tag][tag-badge]][tag][![Clojure version][clojure-v]](project.clj)

[![][logo]][logo-large]

*A Clojure wrapper for the family of Java Socket classes*


#### Contents

* [Documentation](#documentation-)
* [Examples](#examples-)
* [Usage](#usage-)
* [License](#license-)


## Documentation [&#x219F;](#contents)

* [Source code, docstrings, and code comments](http://billo.systems/sockets/current/marginalia) - published using [Marginalia](https://github.com/gdeer81/marginalia)
* [API Reference Docs](http://billo.systems/sockets/current/index) - published using [Codox](https://github.com/weavejester/codox)


## Examples [&#x219F;](#contents)

For running examples, be sure to look in the `examples` directory. The servers
can be run with the following:

```
$ lein run -m examples.udp.echo-server.server
```
and
```
$ lein run -m examples.udp.quote-server.server
```

You can use `nc` (netcat) to connect to these:

```
$ nc -u localhost 15099
```

In addition, the quote server example has a client:

```
$ lein run -m examples.udp.quote-server.client
```


## Usage [&#x219F;](#contents)

The following were taken from the examples mentioned above; for the full
context, please see that code.


Creating a datagram socket:

```clj
(require '[sockets.datagram.socket :as socket])

(def sock (socket/create))
```

Creating a datagram packet implicitly with `receive`:

```clj
(def pkt (socket/receive sock 256))
```

Using the update methods with a threading macro:

```clj
(require '[inet.address :as inet]
         '[sockets.datagram.socket :as socket])

(socket/send sock
             (-> (packet/create 1)
                 (packet/update-address (inet/create [127 0 0 1]))
                 (packet/update-port port)))
```


## License [&#x219F;](#contents)

Copyright © 2017 BilloSystems, Ltd. Co.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/billosys/sockets
[travis-badge]: https://travis-ci.org/billosys/sockets.png?branch=master
[deps]: http://jarkeeper.com/billosys/sockets
[deps-badge]: http://jarkeeper.com/billosys/sockets/status.svg
[logo]: resources/images/socket-250x.png
[logo-large]: resources/images/socket-2400x.png
[tag-badge]: https://img.shields.io/github/tag/billosys/sockets.svg
[tag]: https://github.com/billosys/sockets/tags
[clojure-v]: https://img.shields.io/badge/clojure-1.8.0-blue.svg
[clojars]: https://clojars.org/systems.billo/sockets
[clojars-badge]: https://img.shields.io/clojars/v/systems.billo/sockets.svg
