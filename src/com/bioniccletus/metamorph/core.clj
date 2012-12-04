(ns com.bioniccletus.metamorph.core
  (:require
   [rn.clorine.pool :as pool])
  (:import
   [kafka.javaapi.producer ProducerData Producer]
   [kafka.javaapi.message ByteBufferMessageSet]
   [kafka.message Message]
   [kafka.producer ProducerConfig]
   [java.util Arrays List Properties]))


(defn register-producer [producer-name config]
  (let [props (reduce (fn [props [k v]]
                        (.put props (name k) v)
                        props)
                      (Properties.)
                      config)]
    (pool/register-pool
     producer-name
     (pool/make-factory
      {:make-fn
       (fn [pool-impl]
         (Producer. (ProducerConfig. props)))
       :destroy-fn
       (fn [p]
         (try
          (.close p)
          (catch Exception ex
            ;; NB: log? warn?
            )))}))))

(defmacro with-producer [[inst-name pool-name] & body]
  `(pool/with-instance [~inst-name ~pool-name] ~@body))

(defn send-message [topic message]
  (.send *producer* (ProducerData. topic message)))

(comment
  (def config
       {:zk.connect       "127.0.0.1:2181"
        :serializer.class "kafka.serializer.StringEncoder"})

  (register-producer :test config)

  (with-producer [the-dood :test]
    :got-object-dood)

  )