(ns theseus.core)

(def test-asn
  {:asn 64501
   :name "core-sw1"
   :peers [64500 65402]
   :routes-in
   {"10.0.0.0/24"
    [{:peer 64500
      :as-path [64500 64499]
      :local-pref 100
      :med 0
      :communities #{"no-export"}
      :origin :igp}]}

   :policies
   {:inbound
    {64500 {:set-local-pref 100
            :deny-communities #{"blackhole"}}}
    :outbound
    {64502 {:allow true}}}

   :best-paths {}
   :adj-rib-out {}})

(defn init-asn
  [{:keys [asn name peers policies routes-in]
    :or {peers []
         policies {:inbound {} :outbound {}}
         routes-in {}}}]
  {:asn asn
   :name name
   :peers peers
   :policies policies
   :routes-in routes-in
   :best-paths {}
   :adj-rib-out {}})

(defn add-peer 
  "Adds a peering entry for a given asn to the peer-asn"
  [asn peer-asn]
  (update asn :peers #(vec (distinct (conj % peer-asn)))))

(defn add-rib-out
  "Adds/updates a route in :adj-rib-out for a peer and prefix pair"
  [asn peer prefix route]
  (assoc-in asn [:adj-rib-out peer prefix] route))

(def test-asn
  (-> (init-asn {:asn 4333 :name "test-sw"})
      (add-peer 8888)
      (add-rib-out 8888 "192.168.0.0/24"
                   {:as-path [4333 4444 3333 2222]
                    :local-pref 120
                    :oring :incomplete})))



