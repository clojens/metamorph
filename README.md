# metamorph

A Clojure wrapper around the Apache Kafka APIs

## Usage

Add the dependency to your project.clj:

    [com.bioniccletus/metamorph "0.1.0"]

### Send a message to a Kafka broker with zookeeper broker discovery:

This assumes you have an instance of zookeeper running on localhost:2181

    (use com.bioniccletus.metamorph.core)
    
    ;; Register producer config
    (register-producer
      :my-producer
      {:zk.connect       "127.0.0.1:2181"
       :serializer.class "kafka.serializer.StringEncoder"})

    ;; Post a message to the "test" topic with a body of "hello, kafka"
    (with-producer [p :my-producer]
      (send-message p "test" "hello, kafka"))

## License

Copyright © 2012 

Distributed under the Eclipse Public License, the same as Clojure.

