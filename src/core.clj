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


(def test (init-asn {:asn 1234
                     :name "test"
                     :peers [1 2 3]}))
test
