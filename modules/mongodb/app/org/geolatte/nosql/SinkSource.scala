package org.geolatte.nosql

import org.geolatte.common._
import dataformats.json.jackson.JsonMapper
import java.io.{IOException, FileNotFoundException}
import org.geolatte.geom.Envelope

/**
 * A Sink for data
 */
trait Sink[A] {
  def in(iterator: Iterator[A]): Unit
}

trait Source[A] {
  def out(): Iterator[A]
}

/**
 * A stackable modification trait that can be mixed in
 * into a Source so that it allows for window querying
 *
 *
 */
// See ProgInScala, section 12.5 for this style
trait WindowQueryable[A] extends Source[A] {

  def query(window: Envelope): Iterator[A]

}

/*
 Creates Sinks that write to Standard output. Mainly used for testing  purposes.
 */
object StdOutSink {

  /**
   *
    *@param f a function that turns an instance of A into a printable representation
   * @tparam A Type of objects that go into the sink
   * @return
   */
 def apply[A](f: A => String): Sink[A] =
    new Sink[A] {
      def in(iterator: Iterator[A]) {
        iterator.foreach((a) => println( f(a)) )
      }

    }

}
