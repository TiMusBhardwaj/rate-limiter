# rate-limiter
This Project demonstrate rate-limiting of an api

Its a springboot gradle project.

Project uses Modified sliding logs algorithm.
Normal sliding logs algorithm uses a queue to save all request timestamp from a source to an api.
Enteries from the queue are removed as the timewindow expires for them. For each request size of queue is checked to know whether a request should be blocked or not.

Modfied Sliding logs window:
Normal sliding logs algorithm uses a lot of space by storing time stamp for each request.
We can actually save space by accumulating all requests received at same time as using timestamp as key.
A counter is also used so that we need to caculate current request count from Map.
A rebalance or removing expired entry happens when threshold is reached.



Eclipse :
Import in eclipse as gradle project.

CMD:
build using below command:
gradlew build

Run:
java -jar <jarname>



