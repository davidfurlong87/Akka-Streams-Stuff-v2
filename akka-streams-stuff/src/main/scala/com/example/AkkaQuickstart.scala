//#full-example
package com.example

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.Notifier.Notification
import com.example.OrderProcessor.Order
import com.example.Shipper.Shipment

object Notifier{
  final case class Notification(orderId: Int, shipmentSuccess: Boolean)

  def apply(): Behavior[Notification] = Behaviors.receive{
    (context, message) =>
      context.log.info(message.toString())

      Behaviors.same
  }
}

object Shipper{

  final case class Shipment(orderId: Int, product: String, number: Int, replyTo: ActorRef[Notification])

  def apply(): Behavior[Shipment] = Behaviors.receive{
    (context, message) =>
      context.log.info(message.toString())

      //Processes the shipment, sends a Notification message to the Notifier
      message.replyTo ! Notification(message.orderId, true)

      Behaviors.same
  }
}


object OrderProcessor{

  final case class Order(id: Int, product: String, number: Int)

  //Behavious.receiveMessage == Have access to messages as vals
  //Behavious.receive == Have access to context and to messages
  //Behaviors.setup == same as others, but behaviour setup is deferred until after the actor is started

  def apply(): Behavior[Order] = Behaviors.setup{
    context =>

      val shipperRef = context.spawn(Shipper(), "shipper")
      val notifierRef = context.spawn(Notifier(), "notifier")

      Behaviors.receiveMessage{
        message =>
          context.log.info(message.toString())

          //Process the order by sending a Shipment message to the Shipper
          //Tells the shipper to reply to the notifier
          shipperRef ! Shipment(message.id, message.product, message.number, notifierRef)

          Behaviors.same
      }

  }
}

//#main-class
object AkkaQuickstart extends App {
  //#actor-system
val orderProcessor: ActorSystem[OrderProcessor.Order] = ActorSystem(OrderProcessor(), "orders")
  //#actor-system

  //#main-send-messages
orderProcessor ! Order(0, "jacket", 2)
orderProcessor ! Order(1, "sneakers", 1)
orderProcessor ! Order(2, "socks", 10)
orderProcessor ! Order(3, "umbrella", 9)
  //#main-send-messages
}
//#main-class
//#full-example
