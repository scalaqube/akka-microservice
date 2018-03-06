package com.cffex.ms

/**
  * Created by Ming on 2016/10/12.
  */
import akka.actor._
import eventstore.tcp.ConnectionActor
import eventstore.{IndexedEvent, LiveProcessingStarted, SubscriptionActor}

import scala.concurrent.duration._

object CountAll extends App {
    val system = ActorSystem()
    val connection = system.actorOf(ConnectionActor.props(), "connection")
    val countAll = system.actorOf(Props[CountAll], "count-all")
    system.actorOf(SubscriptionActor.props(connection, countAll), "subscription")
}

class CountAll extends Actor with ActorLogging {
    context.setReceiveTimeout(1.second)

    def receive = count(0)

    def count(n: Long, printed: Boolean = false): Receive = {
        case x: IndexedEvent       => context become count(n + 1)
            println(x.event.data)
            println(x.event.streamId)
        case LiveProcessingStarted => log.info("live processing started")
        case ReceiveTimeout if !printed =>
            log.info("count {}", n)
            context become count(n, printed = true)
    }
}
