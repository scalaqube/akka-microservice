package com.cffex.ms

import java.net.InetSocketAddress

import akka.actor.Status.Failure
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.Http
import akka.pattern.{ask, pipe}
import akka.routing.FromConfig
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.cffex.ms.resource.{RestApi, RolesResource, UserResource}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{FiniteDuration, _}

/**
  * Created by Ming on 2016/10/9.
  */
class HttpServer( port: Int, commandActor: ActorRef)(
    implicit timeout: Timeout
) extends Actor
    with ActorLogging {

    import context.dispatcher

    private implicit val mat = ActorMaterializer()


    def command = commandActor

    val queryRouter:ActorRef=context.actorOf(FromConfig.props(Props[QueryHandler]), name = "queryRouter")

    val QueryHandler = context.actorOf(Props[QueryHandler], name = "query")

    println(QueryHandler)


    Http(context.system)
        .bindAndHandle(handler = RestApi(commandActor,queryRouter).routes, interface="0.0.0.0", port = port)
        .pipeTo(self)

    override def receive = {
        case Http.ServerBinding(a) => handleBinding(a)
        case Failure(c) => handleBindFailure(c)
    }

    private def handleBinding(address: InetSocketAddress) = {
        log.info(s"Listening on $address")
        context.become(Actor.emptyBehavior)
    }

    private def handleBindFailure(cause: Throwable) = {
        log.error(cause, s"Can't bind to $port!")
        context.stop(self)
    }
}

object HttpServer {
    final val Name = "api-server"
    def props(port:Int,commandHandler: ActorRef, timeout: FiniteDuration = 3.seconds): Props =
        Props(new HttpServer(port,commandHandler)(timeout))

//    private def apply(commandHandler: ActorRef)(implicit timeout: Timeout, ec: ExecutionContext) = {
//        import akka.http.scaladsl.server.Directives._
//        // format: OFF
//        def users = pathPrefix("users") {
//            pathEnd {
//                get {
//                    complete {
//                        ("hello world")
//                    }
//                }
//            }
//        }
//
//        users
//    }
}
