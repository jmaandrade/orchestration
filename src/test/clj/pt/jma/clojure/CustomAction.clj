(ns pt.jma.clojure.CustomAction)
(defn hello-world[r] { :outcome "sucess" :sayHi (str "Hello " (get r :name))})

