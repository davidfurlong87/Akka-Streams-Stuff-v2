//#full-example
package com.example

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.example.Notifier.Notification
import com.example.Shipper.Shipment
import org.scalatest.wordspec.AnyWordSpecLike

//#definition
class AkkaQuickstartSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {
//#definition

  "A Shipper" must {
    //#test
    "notify the Notifier" in {
      val replyProbe = createTestProbe[Notification]()
      val underTest = spawn(Shipper())
      underTest ! Shipment(0, "Jacket", 1, replyProbe.ref)
      replyProbe.expectMessage(Notification(0, true))
    }
    //#test
  }

}
//#full-example
