import org.arl.unet.sim.*
import java.io.IOException;
import java.io.PrintStream;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class MyJsonTracer extends Tracer
{
  private static final String abbr = "+-rd";
  FileWriter file;
  JSONObject jsonObject;
  File f;

	protected void trace(int event, int node, Transmission tx, String msg) 
	{
    		int hop = (event == 3 || event == 4) ? node : tx.getTo();	//for receive and drop events
		
		jsonObject = new JSONObject();
      		
      		jsonObject.put("Event", Character.valueOf("+-rd".charAt(event - 1)));
		jsonObject.put("Time", Double.valueOf(getTime()));
		jsonObject.put("From", Integer.valueOf(tx.getFrom()));
		jsonObject.put("To", Integer.valueOf(hop));
		jsonObject.put("Pid", Integer.valueOf(tx.getID().hashCode()));
		
		 try {
                      	f = new File("own_trace.json");
         		file = new FileWriter(f,true);
         		file.write(jsonObject.toJSONString());
         		file.close();
      		} catch (IOException e) {
         	
         	e.printStackTrace();
      		}	
      			

	} //end of trace method

	protected void track(int node, double[] location, String msg) {	}


	protected void print(String msg) { }


	public void close() { 	if(f!=null){f.delete()}		} 

} //end of class
