import com.cffex.ms.db.MongoDb
import com.cffex.ms.model.api.Protocol.{EntityCreated, EntityDeleted, EntityUpdated}
import com.cffex.ms.model.persistence.{RolesEntity, UserEntity}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.Future

//package com.cffex.devtool.dao
//
//import com.cffex.devtool.db.MongoDb
//import com.cffex.devtool.extension.{BsonDao, BsonDsl}
//import com.cffex.devtool.model.api.QuizProtocol._
//import com.cffex.devtool.model.persistence.{ RolesEntity, UserEntity}
//import reactivemongo.api.{DB, DefaultDB, ReadPreference}
//import reactivemongo.api.collections.bson.BSONCollection
//import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID, BSONReader, BSONWriter}
//
//import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
//import com.cffex.devtool.model.persistence._
//
///**
//  * Created by Ming on 2016/9/9.
//  */
//trait Identity[E, ID] {
//    def name: String
//    def of(entity: E): Option[ID]
//    def set(entity: E, id: ID): E
//    def clear(entity: E): E
//    def next: ID
//}
////trait Format[A] extends BSONReader[B] with BSONWriter[A]
//
//trait CRUDService[E, ID] {
//
//    def findById(id: ID): Future[Option[E]]
//    def findAll: Future[Traversable[E]]
//    def findByCriteria(criteria: Map[String, Any], limit: Int): Future[Traversable[E]]
//    def create(entity: E): Future[Either[String, ID]]
//    def update(id: ID, entity: E): Future[Either[String, ID]]
//    def delete(id: ID): Future[Either[String, ID]]
//
//}
////abstract class MongoCRUDService[E: Format, ID: Format](implicit identity: Identity[E, ID])
////    extends CRUDService[E, ID] {
////
////
////    /** Mongo collection deserializable to [E] */
////    def collection: BSONCollection
////
////    override def findById(id: ID): Future[Option[E]] = collection.
////        find(Json.obj(identity.name -> id)).
////        one[E]
////
////    override def findByCriteria(criteria: Map[String, Any], limit: Int): Future[Traversable[E]] =
////        findByCriteria(CriteriaJSONWriter.writes(criteria), limit)
////
////    private def findByCriteria(criteria: JsObject, limit: Int): Future[Traversable[E]] =
////        collection.
////            find(criteria).
////            cursor[E](readPreference = ReadPreference.primary).
////            collect[List](limit)
////
////    override def create(entity: E): Future[Either[String, ID]] = {
////        findByCriteria(Json.toJson(identity.clear(entity)).as[JsObject], 1).flatMap {
////            case t if t.size > 0 =>
////                Future.successful(Right(identity.of(t.head).get)) // let's be idempotent
////            case _ => {
////                val id = identity.next
////                val doc = Json.toJson(identity.set(entity, id)).as[JsObject]
////                collection.
////                    insert(doc).
////                    map {
////                        case le if le.ok == true => Right(id)
////                        case le => Left(le.message)
////                    }
////            }
////        }
////    }
////
////    override def update(id: ID, entity: E): Future[Either[String, ID]] = {
////        val doc = Json.toJson(identity.set(entity, id)).as[JsObject]
////        collection.update(Json.obj(identity.name -> id), doc) map {
////            case le if le.ok == true => Right(id)
////            case le => Left(le.message)
////        }
////    }
////
////    override def delete(id: ID): Future[Either[String, ID]] = {
////        collection.remove(Json.obj(identity.name -> id)) map {
////            case le if le.ok == true => Right(id)
////            case le => Left(le.message)
////        }
////    }
////
////}
//
//
//
//
//
//
//trait CrudDao[E] extends  MongoDb {
//
//    def collection: BSONCollection //= getCollection(collectionName)
//
//    def create(entity: E): Future[Option[EntityCreated]] = collection.insert(entity)
//        .map(_ => Some(EntityCreated(entity.toString)))
//
//    def update(id: String, entity: E): Future[Option[EntityUpdated]] =
//        collection.update(queryById(id), entity)
//            .map(_ => Some(EntityUpdated(id)))
//
//    def findById(id: String): Future[Option[E]] = collection.
//        find(queryById(id)).one[E]
//
//    def deleteById(id: String) = collection.remove(queryById(id)).map(_ => EntityDeleted)
//
//    def findAll(selector: BSONDocument = BSONDocument.empty,
//                   sort: BSONDocument = BSONDocument("_id" -> 1)): Future[List[E]] =
//        collection.find(selector).sort(sort).cursor[E].collect[List]()
//
//    private def queryById(id: String) = BSONDocument("_id" -> BSONObjectID(id))
//}
////



////class RoleDao(_db: DB)
////    extends BsonDao[RolesEntity, String](_db, "persons") with BsonDsl
