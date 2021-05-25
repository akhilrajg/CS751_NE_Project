import org.arl.unet.sim.*

import java.util.*;
import com.google.gson.*;

public class PrettyJSONTracer extends Tracer {

   protected void trace(int event, int node, Transmission tx, String msg) 
	{
    		int hop = (event == 3 || event == 4) ? node : tx.getTo();

      		Traces tr = new Traces(Character.valueOf("+-rd".charAt(event - 1)), Double.valueOf(getTime()), Integer.valueOf(tx.getFrom()), Integer.valueOf(hop), Integer.valueOf(tx.getID().hashCode()));
      		Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
      		String prettyJson = gson.toJson(tr);

		this.out.printf(prettyJson as String);
   	}

	protected void track(int node, double[] location, String msg) {	}


	protected void print(String msg) { 	this.out.printf("# %s\n", [ msg] as String);  }

}


// Traces class
class Traces {
   
     	private char event;
     	private double timestamp
	private int from, to, pid;

   public Traces(char event, double timestamp, int from, int to, int pid) {
      super();
      this.event = event;
      this.timestamp = timestamp;
      this.from = from;
      this.to = to;
      this.pid = pid;
   }
   public char getEvent() {
      return event;
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
}
