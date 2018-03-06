package com.cffex.ms.resource

import akka.http.scaladsl.server._
import akka.pattern.{ask, pipe}
import com.cffex.ms.db.MongoContext
import com.cffex.ms.model.api.Protocol._
import com.cffex.ms.model.persistence.{Entity, RolesEntity, UserEntity}
import reactivemongo.bson.BSONDocument
import com.cffex.ms.dao._

import scala.util.{Failure, Success}
import scala.concurrent.Future
import scala.util.Success

/**
  * Created by Ming on 2016/10/5.
  */
trait RolesResource extends MyResource {
    import scala.concurrent.ExecutionContext.Implicits.global

    def rolesRoutes: Route =
        pathPrefix("api" / "roles") {
            pathEnd {
                post {
                    entity(as[RolesEntity]) { entity =>
                        complete( (command ? CreateEntity("roles",entity)).mapTo[Option[RolesEntity]].map(f=>f))
                    }
                } ~
                get {
//                    complete((query ? "test").mapTo[Future[String]].flatMap[String](str => str))
//                    complete((query ? FindAll("roles")).mapTo[Future[List[RolesEntity]]].flatMap(f=>f))
                    complete((command ? FindAll("roles")).mapTo[List[RolesEntity]].map(f=>f))
                }
            } ~
            path(Segment) { id =>
                get {
                    println(s"$id",command)
                    complete((command ? FindById("roles",id)).mapTo[Option[EntityCreated]].map(f=>f))
                } ~
                put {
                    entity(as[RolesEntity]) { entity =>
                        complete((command ? UpdateEntity("roles",id,entity)).mapTo[Option[EntityUpdated]].map(f=>f))
                    }
                } ~
                delete {
                    complete((command ?DeleteEntity("roles",id)).mapTo[Option[EntityDeleted]].map(f=>f))
                }
            }
        }


}

