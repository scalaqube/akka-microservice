package com.cffex.ms.db

import com.typesafe.config.ConfigFactory
import reactivemongo.api.commands._
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by Ming on 2016/9/26.
  */
object MongoContext {

  val config = ConfigFactory.load()
  val database = config.getString("mongodb.database")
  val servers = config.getStringList("mongodb.servers").asScala
  val driver = new MongoDriver

//  val conOpts =MongoConnectionOptions(connectTimeoutMS=10000,None,true,true,CrAuthentication,false,true,20,WriteConcern.Default,ReadPreference.primary)
  val conOpts=MongoConnectionOptions(connectTimeoutMS=10000,nbChannelsPerNode=20,keepAlive=true)
  val connection = driver.connection(servers,options = conOpts)
  val db: DB = connection(database)

  def getCollection(name:String): BSONCollection = db.collection(name)
}