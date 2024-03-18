## An example of a streaming pipeline using Apache Flink

It implements a streaming data pipeline for processing user actions on publisher
sites, using Apache Flink

* The data source is simulated using `SourceFunction` implementation of Flink
    * the generator uses a file with `JSON` events as its source
    * to decode the events, `Circe` library is used
* The processing includes the calculation of
  * moving average count for Tags depending on the event type (`TagMovingAverage`)
  * moving average count for Users depending on the events type (`UserMovingAverage`)
* After the processing, the results end up in a sink specifically in a `PrintSink`

### IMPROVEMENTS:

* Add randomness in the event generation, i.e. not fixed `Thread.sleep()`
* Set up a local Kafka implementation as the data source
* Use a `FileSink` as data sink to replace `PrintSink`
* Change the sink data type from `String` to some case class, e.g. `case class Result(...)`
* Set up a `NoSQL` solution as sink (consider time-series DB)
* Scaladoc
* Error handling and fault tolerance
* CI using GA
* Custom decoder to handle the `null` case in `tags` field as an empty List (to get rid of
  `Option[List]`) -
  NOTE: https://stackoverflow.com/questions/53178447/not-able-to-decode-when-using-circe-jsonkey

### LIMITATIONS:

* Scala
  API ([deprecated](https://cwiki.apache.org/confluence/display/FLINK/FLIP-265+Deprecate+and+remove+Scala+API+support))
  is not as rich as the Java API,
  e.g. `KeyedOneInputStreamOperatorTestHarness`
