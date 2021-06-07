import org.arl.unet.sim.*

import java.util.*;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MyJsonTracer extends Tracer {

	protected static int level1 = 0, level2 = 0;
  	protected static boolean flag = false, flag2 = false;
	protected static PrintStream out2 = null;

   	protected void trace(int event, int node, Transmission tx, String msg) 
	{
    		int hop = (event == 3 || event == 4) ? node : tx.getTo();

		char ev = Character.valueOf("+-rd".charAt(event - 1));
		String type;
		switch(ev)
		{
			case '+':	type = "ENQUEUED"; break;
			case '-':	type = "DEQUEUED"; break;
			case 'r': 	type = "RECEIVED"; break;
			case 'd':	type = "DROPPED";  break;
		}

		Traces tr = new Traces(type, Double.valueOf(getTime()), Integer.valueOf(tx.getFrom()), Integer.valueOf(hop), 			Integer.valueOf(tx.getID().hashCode()));

      		Gson gson = new GsonBuilder().setPrettyPrinting().create(); // pretty printing is done, can be avoided if needed.
      		String jsonstring = gson.toJson(tr);

		this.out.printf(jsonstring as String);
		
   	}

	protected void print(String msg) { 	this.out.printf("# %s\n", [ msg] as String);  }
	
	public void begin(String name) {
    	synchronized (MyJsonTracer.class) {
      		
		level1++;      		
		try {
        		
			this.out.printf((flag ? ",\n" : "\n") as String);
        		for (int i = 0; i < level1 - 1; ) { this.out.write(32); i++; }
         		String verstr = (level1 == 1) ? "\"version\": \"1.0\"," : "";
			this.out.printf(("{" + verstr + "\"group\":\"" + name + "\",\"events\":[") as String);
        		flag = false;
      		    } 
		catch (IOException ex) {
        
        	level1 = 0;
      		} 
    		} 
  	}//end of begin method

	public void end(String msg) {
	synchronized (MyJsonTracer.class) {
      		
		if (level1 <= 0)
        		return;
  
		level1--;
		try {
        		if (flag) {
          				this.out.printf("\n" as String);
          				for (int i = 0; i < level1; ) { this.out.write(32); i++; }
        
        			   }  
			this.out.printf("]}" as String);
        		flag = true;
      		    } 
		catch (IOException ex) {
        		
        		level1 = 0;
     		} 

		/*----------------------------------------------------------------------------------------------------------------*/

		level2++;      		
		try {
        		
			this.out.printf((flag2 ? ",\n" : "\n") as String);
        		for (int i = 0; i < level2 - 1; ) { this.out.write(32); i++; }
         		String verstr2 = (level2 == 1) ? "\"version\": \"1.0\"," : "";
			this.out.printf(("{" + verstr2 + "\"group\":\"" + msg + "\",\"statistics\":[") as String);
        		flag2 = false;
      		    } 
		catch (IOException ex) {
        
        	level2 = 0;
		}
		
		/*--------------------------------------------------------------------------------------------------------------------*/

		Stats st = new Stats(this.getEnqueueCount(), this.getTxCount(), this.getRxCount(), this.getDropCount(), this.getOfferedLoad(), 			this.getLoad(), this.getThroughput(), this.getMeanDelay()); 

		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
      		String jsonstring = gson.toJson(st);

		this.out.printf(jsonstring as String);

		/*--------------------------------------------------------------------------------------------------------------------*/

		if (level2 <= 0)
        		return;  
		
		level2--;      		 
		try {
        		if (flag2) {
          				this.out.printf("\n" as String);
          				for (int i = 0; i < level2; ) { this.out.write(32); i++; }
        
        			    }  
			this.out.printf("]}" as String);
        		flag2 = true;
      		    } 
		catch (IOException ex) {
        		
        		level2 = 0;
     		} 
					
		/*----------------------------------------------------------------------------------------------------------------------*/

    		} //end of synchronizer
  	}//end of end method	

}// end of MyJsonTracer class


// Traces class
class Traces {

	private String status;
     	private double timestamp
	private int from, to, pid;

   	public Traces(String status, double timestamp, int from, int to, int pid) {
      	   super();
      	   this.status = status
      	   this.timestamp = timestamp;
      	   this.from = from;
      	   this.to = to;
      	   this.pid = pid;
   	}
   	public String getStatus() {
      		return status;
   	}
   	public double getTimeStamp() {
      		return timestamp;
   	}
   	public int getFrom() {
      		return from;
   	}
   	public int getTo() {
      		return to;
   	}
   	public int getPid() {
      		return pid;
   	}

}// end of Traces class
	
class Stats {
	
	int queued, transmitted, received, dropped;
	double offered_load, load, mean_delay, throughput;
	
	public Stats(int qcnt, int txcnt, int rxcnt, int dcnt, double offeredload, double load, double throughput, double meandelay) {
	   super();
	   this.queued = qcnt;
	   this.transmitted = txcnt;
           this.received = rxcnt;
           this.dropped = dcnt;
	   this.offered_load = offeredload;
	   this.load = load;
           this.throughput = throughput;
	   this.mean_delay = meandelay;
        }
	public int getQueued() {
		return queued;
	}
	public int getTransmitted() {
		return transmitted;
	}
	public int getReceived() {
		return received;
	}
	public int getDropped() {
		return dropped;
	}
	public double getOffrdLoad() {
		return offered_load;
	}
	public double getActLoad() {
		return load;
	}
	public double getMdelay() {
		return mean_delay;
	}
	public double getThrput() {
		return throughput;
	}	
			
}// end of Stats class
