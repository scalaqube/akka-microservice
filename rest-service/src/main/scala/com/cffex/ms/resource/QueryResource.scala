package com.cffex.ms.resource

import akka.http.scaladsl.server._
import akka.pattern.{ask, pipe}
import com.cffex.ms.model.api.Protocol.{EntityCreated, FindAll, FindById}
import com.cffex.ms.model.persistence.{RolesEntity,Entity}

/**
  * Created by Ming on 2016/10/14.
  */
trait QueryResource extends MyResource{
    import scala.concurrent.ExecutionContext.Implicits.global
    val queryRoute:Route=
        pathPrefix("api"/"r"){
            path(Segment){ document=>

                pathEnd {
                    get {
                        complete((command ? FindAll(document)).mapTo[List[Entity]].map(f=>f))
                    }
                }~
                path(Segment) { id =>
                    get {
                        complete((command ? FindById(document, id)).mapTo[Option[EntityCreated]].map(f => f))
                    }
                }
            }

        }
}
