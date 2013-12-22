(ns hashlog.core)

(declare apply-cond)

(defn exists [key]
  (fn [hash] (contains? hash key)))

(defn is [key v]
  (fn [hash] (= (hash key) v)))

(defn every [cond-coll]
  (fn [hash] (every? #(apply-cond % hash) cond-coll)))

(defn multiply [key num]
  (fn [hash] (* (hash key) num)))

(defn sum [keys]
  (fn [hash] (apply + (map #(hash %) keys))))

(defn value [key]
  (fn [hash] (hash key)))


(defn apply-cond [f hash]
  (let [pred (cond (vector? f)   (every f)
                   (not (fn? f)) (exists f)
                   :else         f)]
    (pred hash)))

(defn next-hash [input-hash logs]
  (reduce (fn [hash {condf :cond, key :key, val :val}]
            (let [v (cond (fn? val) (val hash)
                          :else     val)]
              (if (apply-cond condf hash)
                (assoc hash key v)
                hash)))
          input-hash
          logs))

(defn hash-seq [init-hash logs]
  (let [eval-seq (iterate #(let [next (next-hash (:hash %) logs)]
                             {:hash next
                              :before (conj (:before %) (:hash %))})
                     {:hash init-hash
                      :before #{}})]
    (map :hash
         (take-while #(not (contains? (:before %) (:hash %)))
                     eval-seq))))

(defn query [hseq q]
  (if-let [hash (last (filter #(contains? % q) hseq))]
    (hash q)
    'no-answer))



(comment

(def buy-fruits
  {:apple  2
   :orange 4
   })

(def fruits-price
  [
   {:cond :apple
    :key :priceOfApple
    :val (multiply :apple 100)
    }
   {:cond :orange
    :key :priceOfOrange
    :val (multiply :orange 80)
    }
   {:cond [:priceOfApple :priceOfOrange]
    :key :price
    :val (sum [:priceOfApple :priceOfOrange])
    }
   ])

(def fruits-price-discount
  (conj fruits-price
        {:cond [:price (is :hasCoupon true)]
         :key :discountPrice
         :val (multiply :price 0.9)
         }
        {:cond [:price (is :hasCoupon false)]
         :key :discountPrice
         :val (value :price)
         }
        ))


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
