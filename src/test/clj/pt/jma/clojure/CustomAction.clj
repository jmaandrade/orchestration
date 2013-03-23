(ns pt.jma.clojure.CustomAction (:gen-class))
					
 (defn hello-world[r] 
  { :outcome "sucess" :sayHi (str "Hello " (get r :name))})
					
(defn -main [arg]
	(println (get (hello-world { :name arg}) :sayHi)))
