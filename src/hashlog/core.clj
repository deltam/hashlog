(ns hashlog.core)


(defn exists [key]
  (fn [hash] (contains? hash key)))

(defn is [key v]
  (fn [hash] (= (hash key) v)))

(defn every [cond-coll]
  (fn [hash] (every? #(% hash) cond-coll)))

(defn multiply [key num]
  (fn [hash] (* (hash key) num)))

(defn value [key]
  (fn [hash] (hash key)))


(defn next-hash [input-hash logs]
  (reduce (fn [hash {pred :cond, key :key, val-func :val}]
            (if (pred hash)
              (assoc hash key (val-func hash))
              hash))
          input-hash
          logs))

(defn evaluate [init-hash blocks]
  (loop [current init-hash]
    (let [next (next-hash current blocks)]
      (if (not= (.hashCode current) (.hashCode next))
        (recur next)
        next))))

(defn hash-seq [init-hash log]
  (iterate #(next-hash % log) init-hash))



(comment

(def buy-fruits
  {:apple  2
   :orange 4
   })

(def fruits-price
  [
   {:cond (exists :apple)
    :key :priceOfApple
    :val (multiply :apple 100)
    }
   {:cond (exists :orange)
    :key :priceOfOrange
    :val (multiply :orange 80)
    }
   {:cond (every [(exists :priceOfApple) (exists :priceOfOrange)])
    :key :price
    :val (multiply :orange 80)
    }
   {:cond (every [(exists :price) (is :hasCoupon true)])
    :key :discountPrice
    :val (multiply :price 0.9)
    }
   {:cond (every [(exists :price) (is :hasCoupon false)])
    :key :discountPrice
    :val (value :price)
    }
   ]
)

)
