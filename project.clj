(defproject systems.billo/sockets "0.1.1"
  :description "A Clojure wrapper for the family of Java Socket classes"
  :url "https://github.com/billosys/sockets"
  :license {
    :name "Eclipse Public License"
    :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
    [org.clojure/clojure "1.9.0"]
    [org.clojure/core.async "0.4.474"]
    [potemkin "0.4.5"]
    [systems.billo/inet-address "0.1.1"]]
  :profiles {
    :docs {
      :dependencies [
        [systems.billo/superhero-codox-theme "0.3.0"]]
      :plugins [
        [lein-codox "0.10.4"]
        [lein-marginalia "0.9.1"]]
      :source-paths ^:replace ["src"]
      :codox {
        :project {:name "sockets"}
        :themes [:superhero]
        :output-path "docs/current"
        :doc-paths ["resources/docs"]
        :metadata {:doc/format :markdown}}}
    :dev {
      :dependencies [
        [clojusc/trifl "0.3.0"]
        [org.clojure/tools.namespace "0.2.11"]]
      :source-paths [
        "dev-resources/src"
        "test"
        "examples/src"]
      :repl-options {
        :init-ns sockets.dev
        :prompt (fn [ns] (str "\u001B[35m[\u001B[34m"
                              ns
                              "\u001B[35m]\u001B[33m λ\u001B[m=> "))
        :welcome ~(do
                    (println (slurp "resources/text/banner.txt"))
                    (println (slurp "resources/text/loading.txt")))}}
    :test {
      :plugins [
        [jonase/eastwood "0.2.8" :exclusions [org.clojure/clojure]]
        [lein-kibit "0.1.6" :exclusions [org.clojure/clojure]]
        [lein-ancient "0.6.15"]]}}
  :aliases {
    "check-deps" ["with-profile" "+test" "ancient" "check" "all"]
    "lint" ["with-profile" "+test" "kibit"]
    "docs" ["with-profile" "+docs" "do"
      ["codox"]
      ["marg" "--dir" "docs/current"
              "--file" "marginalia.html"
              "--name" "sockets"]]
    "build" ["with-profile" "+test" "do"
      ["check-deps"]
      ["lint"]
      ["test"]
      ["compile"]
      ["docs"]
      ["uberjar"]]})


