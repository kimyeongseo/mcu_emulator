
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Memory {
	private short memory[] = new short[256];

	int[] header = {};
	int[] dataSegment = {};
	int[] codeSegment = {};
	
	public void read() throws IOException{
	  List<String> ret = fileLineRead("C:/Users/USER/Desktop/java/Computer/exe/exe");
		for(int i = 0; i<ret.size(); i++){
		  System.out.println(ret.get(i));
		}
	}

	public static List<String> fileLineRead(String name) throws IOException{
		List<String> retStr = new ArrayList<String>();
	   BufferedReader in = new BufferedReader(new FileReader(name));
		String s;
		  while ((s = in.readLine()) != null) {
			   retStr.add(s);
		  }
		in.close();
		return retStr;  
	 }

	public short load(short mar) {
		return this.memory[mar];
		
	}

	public void store(short mar, short mbr){ 
		this.memory[mar] = mbr;
	}
 
}