package config

import nosql.mongodb.MongoDBRepository

import scala.language.implicitConversions
import play.api.Play._

/**
 * @author Karel Maesen, Geovise BVBA
 * creation-date: 7/23/13
 */
object ConfigurationValues {


  import play.api.Play.current

  //configuration keys
  val MONGO_CONNECTION_STRING_KEY = "fs.mongodb"
  val MONGO_SYSTEM_DB_KEY = "fs.system.db"
  val MAXIMUM_RESULT_SIZE_KEY= "max-collection-size"

  // Default values
  val DEFAULT_CREATED_DBS_COLLECTION = "createdDatabases"
  val DEFAULT_CREATED_DB_PROP = "db"
  val DEFAULT_SYS_DB = "featureServerSys"
  val DEFAULT_DB_HOST = "localhost"
  val DEFAULT_MAXIMUM_RESULT_SIZE = 10000

  class ConstantsEnumeration extends Enumeration {
    def unapply (s: String): Option[Value] = values.find(_.toString.toUpperCase == s.toUpperCase)
  }

  object Format extends ConstantsEnumeration {
    type Formats = Value
    val JSON, CSV = Value
    def stringify(v: Value) : String = v.toString.toLowerCase
  }

  object Version extends ConstantsEnumeration {
    type Versions = Value
    val v1_0 = Value
    def default = v1_0
    def stringify(v : Value) : String = v.toString.replace("_", ".").drop(1)
    override def unapply (s: String): Option[Value] = values.find(_.toString == "v" + s.replace(".","_"))
  }

  val configuredRepository = current.configuration.getString("fs.db") match {
    case Some(value) => value
    case None => MongoDBRepository //if nothing configured, then assume MongoDB
  }


  import scala.collection.JavaConversions._

  val MongoConnnectionString : List[String]= current.configuration.getStringList(MONGO_CONNECTION_STRING_KEY) match {
    case Some(strl) => strl.toList
    case None => List[String](DEFAULT_DB_HOST)
  }

  val MongoSystemDB = current.configuration.getString(MONGO_SYSTEM_DB_KEY).orElse(Some(DEFAULT_SYS_DB)).get

  val MaxReturnItems = current.configuration.getInt(MAXIMUM_RESULT_SIZE_KEY).getOrElse[Int](DEFAULT_MAXIMUM_RESULT_SIZE)
  /**
   * The separator to use between any two Json strings when streaming JSON
   */
  val jsonSeparator = "\n"

}
