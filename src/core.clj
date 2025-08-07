(ns theseus.core)

(def r1 
  {:router-id "1.1.1.1"
   :hostname  "R1"
   :asn       65001


   :vrfs
   {"default"
    {:interfaces
     {"Ethernet1" {:ipv4 ["10.0.12.1/30"] :ipv6 [] :state :up}
      "Ethernet2" {:ipv4 ["10.0.13.1/30"] :ipv6 [] :state :up}
      "Loopback0" {:ipv4 ["1.1.1.1/32"]   :ipv6 [] :state :up}}


     :neighbors
     {
      "Ethernet1" {:type :p2p :peer "R2" :remote-if "Ethernet1"}
      "Ethernet2" {:type :p2p :peer "R3" :remote-if "Ethernet2"}}
     

     :arp {"10.0.12.2" "00:11:22:33:44:55"
           "10.0.13.2" "00:aa:bb:cc:dd:ee"}


     :ribs
     {:connected {"10.0.12.0/30" {:nh :connected :if "Ethernet1"}
                  "10.0.13.0/30" {:nh :connected :if "Ethernet2"}
                  "1.1.1.1/32"   {:nh :connected :if "Loopback0"}}

      :static    {} 

      :igp       {:protocol :isis
                  :routes {"1.1.1.2/32" {:nh "10.0.12.2" :if "Ethernet1" :metric 10}}}
      
      :bgp       {
                  "192.0.2.0/24" {:attrs {:local-pref 200 :as-path [65002]}
                                  :nh "10.0.12.2" :if "Ethernet1"
                                  :src-peer 65002
                                  :origin :igp :med 0}}}

     :bgp
     {:neighbors
      {65002 {:remote-as 65002 :session-type :ebgp
              :router-id "2.2.2.2"
              :import-policy {:set-local-pref 200}
              :export-policy {:set-communities-additive ["65001:99"]}
              :adj-rib-in  {"192.0.2.0/24" [{:attrs {:local-pref 100
                                                     :as-path [65002 64512]
                                                     :origin :igp
                                                     :communities ["64512:100"]}
                                             :nh "10.0.12.2" :if "Ethernet1"}]}
              :adj-rib-out {}}}}
     
     :fib
     {"192.0.2.0/24" [{:nh "10.0.12.2" :if "Ethernet1" :weight 1
                       :mac "00:11:22:33:44:55"}]
      "0.0.0.0/0"    [{:nh "10.0.12.2" :if "Ethernet1" :weight 1}]}}}})

(defn add-interface 
"Configure interface on a router"
[router vrf ifname ip-cidr]
(assoc-in router [:vrfs vrf :interfaces ifname :ipv4]
          (conj (get-in router [:vrfs vrf :interfaces ifname :ipv4] []) ip-cidr)))

(add-interface r1 "default" "Ethernet3" "172.20.4.1/30")
