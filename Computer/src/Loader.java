public class Loader {
		
	static final short sizeHeader = 8; // 2
	static final short indexPC = 0; 
	static final short indexSP = 1; 
		
	private short startAddress = 0;
	private short sizeData, sizeCode;
		
	public void loadHeader() {
		Memory memory = new Memory();
		CPU cpu = new CPU();
		this.sizeData = 24; // 6
		this.sizeCode = 192; // 48
			
		this.startAddress = memory.allocate((short)(sizeHeader/4 + this.sizeData/4 + this.sizeCode/4));
		cpu.setPC((short) (startAddress + sizeHeader/4));
		cpu.setSP((short) (startAddress + sizeHeader/4 + this.sizeCode/4 ));
	}
	
	
}
