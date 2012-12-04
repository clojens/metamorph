(ns com.bioniccletus.metamorph.core
  (:import [kafka.javaapi.producer ProducerData Producer]
           [kafka.javaapi.message ByteBufferMessageSet]
           [kafka.message Message]
           [kafka.producer ProducerConfig]
           [java.util Arrays List Properties]))


(def *config*
     (atom
      {:test
       {:zk.connect       "127.0.0.1:2181"
        :serializer.class "kafka.serializer.StringEncoder"}}))


(def ^:dynamic *producer* nil)

(defn with-producer* [id body-fn]
  (let [config   (get @*config* id)
        props    (reduce (fn [props [k v]]
                           (.put props (name k) v)
                           props)
                         (Properties.)
                         config)
        config   (ProducerConfig. props)]
    (with-open [producer (Producer. config)]
      (binding [*producer* producer]
        (body-fn)))))


(defmacro with-producer [id & body]
  `(with-producer* ~id (fn [] ~@body)))

(defn send-message [topic message]
  (.send *producer* (ProducerData. topic message)))

(comment
  (with-producer :test
    (send-message "test" "foo"))

  )