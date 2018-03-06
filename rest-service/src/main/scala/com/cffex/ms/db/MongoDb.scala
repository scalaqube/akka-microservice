package com.cffex.ms.db

import com.typesafe.config.ConfigFactory
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{DB, MongoConnectionOptions, MongoDriver}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global



trait MongoDb {

  val config = ConfigFactory.load()
  val database = config.getString("mongodb.database")
  val servers = config.getStringList("mongodb.servers").asScala

  val driver = new MongoDriver
//  val connection = driver.connection(servers)
  val conOpts=MongoConnectionOptions(connectTimeoutMS=10000,nbChannelsPerNode=5,keepAlive=true)
  val connection = driver.connection(servers,options = conOpts)

  val db: DB  = connection(database)
  def getCollection(name:String): BSONCollection = db.collection(name)
}
