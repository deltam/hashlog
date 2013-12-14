(ns hashlog.core)


(defn exists [key]
  (fn [hash] (contains? hash key)))

(defn is [key v]
  (fn [hash] (= (hash key) v)))

(defn every [cond-coll]
  (fn [hash] (every? #(% hash) cond-coll)))

(defn multiply [key num]
  (fn [hash] (* (hash key) num)))

(defn sum [keys]
  (fn [hash] (apply + (map #(hash %) keys))))

(defn value [key]
  (fn [hash] (hash key)))


(defn next-hash [input-hash logs]
  (reduce (fn [hash {pred :cond, key :key, val-func :val}]
            (if (pred hash)
              (assoc hash key (val-func hash))
              hash))
          input-hash
          logs))

;; 手続き的な書き方
;(defn evaluate [init-hash blocks]
;  (loop [current init-hash]
;    (let [next (next-hash current blocks)]
;      (if (not= (.hashCode current) (.hashCode next))
;        (recur next)
;        next))))

(defn hash-seq [init-hash logs]
  (let [eval-seq (iterate #(let [next (next-hash (:hash %) logs)]
                             {:hash next
                              :current (.hashCode next)
                              :before (:current %)})
                     {:hash init-hash
                      :current 1
                      :before -1})]
    (map :hash
         (take-while #(not= (:before %) (:current %))
                     eval-seq))))

(defn query [hseq q]
  ((first (filter #(contains? % q) hseq))
   q))

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
    :val (sum [:priceOfApple :priceOfOrange])
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

;; 価格計算HashMapのシーケンスを作る
(def price-seq (hash-seq buy-fruits fruits-price))
;; 2つ目以降は結果が変わらないのでdropされている
(take 3 price-seq)
;({:orange 4, :apple 2}
; {:orange 4,
;  :apple 2,
;  :priceOfApple 200,
;  :price 520,
;  :priceOfOrange 320})

;; 価格を知る
(query price-seq :price)
;=> 520


;; クーポン有りの価格計算
(def price-seq2 (hash-seq
                 (assoc buy-fruits :hasCoupon true)
                 fruits-price))
(take 3 price-seq2)
;({:orange 4, :hasCoupon true, :apple 2}
; {:orange 4,
;  :hasCoupon true,
;  :discountPrice 468.0,
;  :apple 2,
;  :priceOfApple 200,
;  :price 520,
;  :priceOfOrange 320})

(query price-seq2 :discountPrice)
;=> 468.0
;; 520の1割引

)
