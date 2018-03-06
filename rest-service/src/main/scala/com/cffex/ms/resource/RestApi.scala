package com.cffex.ms.resource

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.server._
import akka.util.Timeout
import scala.concurrent.duration.{FiniteDuration, _}
import akka.pattern.{ask, pipe}
import scala.concurrent.duration.FiniteDuration
//import com.cffex.devtool.dao.UserDao
import scala.concurrent.ExecutionContext

/**
  * Created by Ming on 2016/9/26.
  */
case class RestApi(commandActor: ActorRef,queryRouter:ActorRef)
             (implicit timeout: Timeout, ec: ExecutionContext)
    extends UserResource with RolesResource with QuizResource with QueryResource{

//    implicit def executionContext: ExecutionContext

//    override lazy val userDao = UserDao

    def command=commandActor

    def query=queryRouter

    def routes: Route = rolesRoutes ~ userRoutes ~ quizRoutes ~ queryRoute

//    def receive = {
//        case _ =>println("message received")
//    }
}

//object RestApi{
//    def props(commandHandler: ActorRef,
//              queryHandler: ActorRef,
//              timeout: Timeout = 3.seconds): Props =
//        Props(new RestApi(commandHandler, queryHandler)(timeout))
//
//    def apply(commandHandler: ActorRef,
//              queryHandler: ActorRef)(implicit timeout: Timeout, ec: ExecutionContext) = {
//        import akka.http.scaladsl.server.Directives._
//
//        // format: OFF
//        def users = pathPrefix("users") {
//            pathEnd {
//                get {
//                    complete {
//
//                        (queryHandler ? "test").mapTo[String].map(str => println(str + "======"))
//
//                        ("hello world")
//                    }
//                }
//            }
//        }
//
//        users ~ RolesResource.rolesRoute
//    }
//
//
//}
