package com.cffex.ms.resource

import akka.http.scaladsl.server._
import akka.pattern.{ask, pipe}
import com.cffex.ms.model.api.Protocol.{EntityCreated, EntityDeleted, EntityUpdated, FindAll}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import com.cffex.ms.dao.UserDao
import com.cffex.ms.model.persistence.UserEntity

/**
  * Created by Ming on 2016/9/26.
  */
trait UserResource extends MyResource {
    private def queryById(id: String) = BSONDocument("_id" -> BSONObjectID(id))

    def userRoutes: Route =
        pathPrefix("register") {
            pathEnd {
                post {
                    entity(as[UserEntity]) { entity =>
                        // println("======" + entity)
                        complete(UserDao.create(entity))
                    }
                }~
                get{
                    complete((command ? FindAll("users"))(timeout,command))
                }
            } ~
            path(Segment) { id =>
                get {
                    complete(UserDao.findById(id))
                } ~
                put {
                    entity(as[UserEntity]) { entity =>
                         complete(UserDao.update(id,entity))
                    }
                } ~
                delete {
                    complete(UserDao.deleteById(id))
                }
            }
        }
}
