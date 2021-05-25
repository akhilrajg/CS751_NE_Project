//! Simulation: 2-node network real-time simulation for json trace file demo.

import org.arl.fjage.*
import org.arl.unet.*
import org.arl.unet.phy.*
import org.arl.unet.sim.*
import org.arl.unet.sim.channels.*
import static org.arl.unet.Services.*
import static org.arl.unet.phy.Physical.*

platform = org.arl.fjage.RealTimePlatform

trace = new PrettyJSONTracer()

trace.open('logs/own_trace.json')

simulate 1.minutes, {	//runs simulation for 1 minute in realtime
  def myNode = node '1', address: 1, web: 1101, location: [0, 0, 0]
  myNode.startup = {
        		def phy = agentForService PHYSICAL
			add new TickerBehavior(10000, {		//to send packets at an interval of 10 secs
				phy << new TxFrameReq(to:2, type: DATA)
	  		});
        
	}
  node '2', address: 2, web: 1102, location: [1.km, 0, 0]
}
