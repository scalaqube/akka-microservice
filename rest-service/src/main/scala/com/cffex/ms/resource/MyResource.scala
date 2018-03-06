package com.cffex.ms.resource

import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.{ToResponseMarshallable, ToResponseMarshaller}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import com.cffex.ms.{CommandHandler, QueryHandler}
import com.cffex.ms.serializers.JsonSupport

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.{FiniteDuration, _}
import scala.concurrent.ExecutionContext.Implicits.global

trait MyResource extends Directives with JsonSupport {

//  implicit def executionContext: ExecutionContext

  def completeWithLocationHeader[T](resourceId: Future[Option[T]], ifDefinedStatus: Int, ifEmptyStatus: Int): Route =
    onSuccess(resourceId) {
      case Some(t) => completeWithLocationHeader(ifDefinedStatus, t)
      case None => complete(ifEmptyStatus, None)
    }

  def completeWithLocationHeader[T](status: Int, resourceId: T): Route =
    extractRequestContext { requestContext =>
      val request = requestContext.request
      val location = request.uri.copy(path = request.uri.path / resourceId.toString)
      respondWithHeader(Location(location)) {
        complete(status, None)
      }
    }

  def complete[T: ToResponseMarshaller](resource: Future[Option[T]]): Route =
    onSuccess(resource) {
      case Some(t) => complete(ToResponseMarshallable(t))
      case None => complete(404, None)
    }

  def complete(resource: Future[Unit]): Route = onSuccess(resource) { complete(204, None) }





  def query: ActorRef

  def command: ActorRef
//
//  def commandSender: ActorRef

  implicit val timeout: Timeout = 30.seconds

}
