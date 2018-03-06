package com.cffex.ms.resource

import akka.http.scaladsl.server._
import akka.pattern.{ask, pipe}
import com.cffex.ms.model.api.Protocol._
import com.cffex.ms.model.persistence.{QuizEntity, RolesEntity}

import scala.concurrent.Future

/**
  * Created by Ming on 2016/10/11.
  */
trait QuizResource extends MyResource {
    import scala.concurrent.ExecutionContext.Implicits.global
    def quizRoutes: Route =
        pathPrefix("api" / "w"/ "quiz") {
            pathEnd {
                post {
                    entity(as[QuizEntity]) { entity =>
                        complete( (command ? CreateEntity("quiz",entity)).mapTo[EntityCreated].map(f=>f))
                    }
                } ~
                get {
                    // (query ? "test").mapTo[String].map(str => println(str + "======"))
                    complete((command ? FindAll("quiz")).mapTo[List[QuizEntity]].map(f=>f))
                }
            } ~
            path(Segment) { id =>
                get {
                    println(s"$id",command)
                    complete((command ? FindById("quiz",id)).mapTo[Option[QuizEntity]].map(f=>f))
                } ~
                put {
                    entity(as[QuizEntity]) { entity =>
                        complete((command ? UpdateEntity("quiz",id,entity)).mapTo[EntityUpdated].map(f=>f))
                    }
                } ~
                delete {
                    complete((command ?DeleteEntity("quiz",id)).mapTo[EntityDeleted].map(f=>f))
                }
            }
        }

}
