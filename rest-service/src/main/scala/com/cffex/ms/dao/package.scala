package com.cffex.ms

import com.cffex.ms.db.MongoDb
import com.cffex.ms.model.api.Protocol.{EntityCreated, EntityDeleted, EntityUpdated}
import com.cffex.ms.model.persistence.{RolesEntity, UserEntity}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Ming on 2016/9/26.
  */
package object dao {

    trait CrudDao[E] extends MongoDb {

        def collection: BSONCollection

        def create(entity: E): Future[Option[EntityCreated]]

        def update(id: String, entity: E): Future[Option[EntityUpdated]]

        def findById(id: String): Future[Option[E]]

        def deleteById(id: String): Future[Option[EntityDeleted]] = collection.remove(queryById(id))
            .map(_ => Some(EntityDeleted(id)))

        def findAll(selector: BSONDocument = BSONDocument.empty,
                    sort: BSONDocument = BSONDocument("_id" -> 1)): Future[List[E]]

        def queryById(id: String) = BSONDocument("_id" -> BSONObjectID(id))
    }

    object UserDao extends CrudDao[UserEntity] {
        def collection: BSONCollection = getCollection("users")

        def create(entity: UserEntity): Future[Option[EntityCreated]] = collection.insert(entity)
            .map(_ => Some(EntityCreated(entity)))

        def update(id: String, entity: UserEntity): Future[Option[EntityUpdated]] =
            collection.update(queryById(id), entity)
                .map(_ => Some(EntityUpdated(id,entity)))

        def findById(id: String): Future[Option[UserEntity]] = collection.
            find(queryById(id)).one[UserEntity]


        def findAll(selector: BSONDocument = BSONDocument.empty,
                    sort: BSONDocument = BSONDocument("_id" -> 1)): Future[List[UserEntity]] =
            collection.find(selector).sort(sort).cursor[UserEntity].collect[List]()

    }

    object RoleDao extends CrudDao[RolesEntity] {
        def collection: BSONCollection = getCollection("roles")

        def create(entity: RolesEntity): Future[Option[EntityCreated]] = collection.insert(entity)
            .map(_ => Some(EntityCreated(entity)))

        def update(id: String, entity: RolesEntity): Future[Option[EntityUpdated]] =
            collection.update(queryById(id), entity)
                .map(_ => Some(EntityUpdated(id,entity)))

        def findById(id: String): Future[Option[RolesEntity]] = collection.
            find(queryById(id)).one[RolesEntity]


        def findAll(selector: BSONDocument = BSONDocument.empty,
                    sort: BSONDocument = BSONDocument("_id" -> 1)): Future[List[RolesEntity]] =
            collection.find(selector).sort(sort).cursor[RolesEntity].collect[List]()


    }

}
