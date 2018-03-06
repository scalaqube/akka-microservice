package com.cffex.ms

import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props, SupervisorStrategy, Terminated}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.seed.ZookeeperClusterSeed
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}

/**
  * Created by Ming on 2016/10/9.
  */
object RootApp extends App {
    //
    //    RootNode.initiate(2551,9001)
    //
    //    RootNode.initiate(2552,9002)
    //
    //    RootNode.initiate(2560,9003)
    //
    //    RootNode.initiate(2561,9004)
    val system = ActorSystem("akka-cluster")
    val root = system.actorOf(RootNode.props(9001), name = "Root")
    sys.addShutdownHook(system.terminate())
}


object RootNode {

    def props(httpPort: Int): Props = Props(new RootNode(httpPort))

    //    def initiate(port: Int,httpPort:Int) = {
    //        val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
    //            withFallback(ConfigFactory.load())
    //
    //        val system = ActorSystem("dev-tool-api", config)
    //
    //        val root = system.actorOf(RootNode.props(httpPort), name = "Root")
    //    }
}

class RootNode(httpPort: Int) extends Actor with ActorLogging {

    override val supervisorStrategy = SupervisorStrategy.stoppingStrategy

    val cluster = ZookeeperClusterSeed(context.system)
        cluster.join()

    val commandHandler = context.actorOf(ClusterSingletonManager.props(
        singletonProps = Props[CommandHandler],
        terminationMessage = PoisonPill,
        settings = ClusterSingletonManagerSettings(context.system).withRole(None)
    ), name = "commandHandler")

    val commandHandlerProxy = context.actorOf(ClusterSingletonProxy.props(
        singletonManagerPath = "/user/Root/commandHandler",
        settings = ClusterSingletonProxySettings(context.system).withRole(None)
    ), name = "commandHandlerProxy")


    private val httpServer = {
        val config = context.system.settings.config

        context.actorOf(HttpServer.props(httpPort, commandHandlerProxy), HttpServer.Name)
    }

    context.watch(commandHandlerProxy)
    context.watch(httpServer)


    // subscribe to cluster changes, MemberUp
    // re-subscribe when restart
    override def preStart(): Unit = {
        cluster.subscribe(self, classOf[MemberUp])
    }

    override def postStop(): Unit = {
        cluster.unsubscribe(self)
    }

    override def receive = {
        case MemberUp(member) =>
            log.info(
                s"Member ${member} is up!"
            )

        case Terminated(actor) =>
            log.error(
                s"Terminating the system because ${actor.path} terminated!"
            )
            context.system.terminate()
    }
}