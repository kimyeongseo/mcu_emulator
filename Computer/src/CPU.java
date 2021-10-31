public class CPU {
	
	// declaration
	private enum EOpCode {
		eHalt((short) 0x00), // 0x00 -> 00000000
		eLDC((short) 0x01),  // 0x01 -> 00000001
		eLDA((short) 0x02),  // 0x02 -> 00000010
		eSTA((short) 0x03),  // 0x03 -> 00000011
		eADDA((short) 0x04), // 0x04 -> 00000100
		eADDC((short) 0x05), // 0x05 -> 00000101
		eSUBA((short) 0x06), // 0x06 -> 00000110
		eSUBC((short) 0x07), // 0x07 -> 00000111
		eMULA((short) 0x08), // 0x08 -> 00001000
		eDIVC((short) 0x09), // 0x09 -> 00001001
		eANDA((short) 0x0A), // 0x0A -> 00001010
		eNOTA((short) 0x0B), // 0x0B -> 00001011
		eJMPZ((short) 0x0C), // 0x0C -> 00001100
		eJMPBZEQ((short) 0x0D),// 0x0D -> 00001101
		eJMP((short) 0x0E),  // 0x0E -> 00001110
		eJMPBZ((short) 0x0F); 

		short opcode;
		private EOpCode(short opcode) {
			this.opcode = opcode;
		}
		
		public short getValue() {
			return opcode;
		}
	}
	
	private enum ERegister {
        ePC((short) 0x0000),
		eSP((short) 0x0000),
		eAC((short) 0x0000),
		eIR((short) 0x0000),
		eSR((short) 0x0000),
		eStatus((short) 0x0000),
		eMAR((short) 0x0000),
		eMBR((short) 0x0000);
		
		short value;
		private ERegister(short value) {
			this.value = value;
		}
		
		public short getValue() {
			return value;
		}
		
		public void setValue(short value) {
			this.value = value;
		}
    }
	

    //component
    private ALU alu;
    private CU cu;

    
    // association
    private Memory memory;
    
    public void associate(Memory memory) {
        this.memory = memory;		
    }
    
    
	public class ALU  {
		private enum EALU {
			eStore((short) 0x0000),
			eAdd((short) 0x0000),
			eDivision((short) 0x0000),
			eSubtract((short) 0x0000),
			eMultiply((short) 0x0000),
			eAnd((short) 0x0000),
			eNot((short) 0x0000);
			
			short value;
			private EALU(short value) {
				this.value = value;
			}
			
			public short getValue() {
				return value;
			}
			
			public void setValue(short value) {
				this.value = value;
			}
		}
	}
	
	private class CU {
		public boolean isZero(short value) {
			if((short) (value & 0x8000) == 0) { // 1000 0000 0000 0000
				return false;
			} else {
				return true;
			}
		}
		
		public boolean isBZ(short value) {
			if((short) (value & 0x4000) == 0) { // 0100 0000 0000 0000
				return false;
			} else {
				return true;
			}
		}
		
		public boolean isBZEQ(short value) {
			if((short) (value & 0xC000) == 0) { // 1100 0000 0000 0000
				return false;
			} else {
				return true;
			}
		}
	}
	
    // Instructions
    private void Halt() {
    	System.out.println("halt");
    	shutDown();
    }
    private void LDC(short ir_operand) {
    	System.out.println("load c");
    	ERegister.eMBR.setValue(ir_operand);
    	ERegister.eAC.setValue(ERegister.eMBR.getValue());
    	ERegister.ePC.value++;
    }
    private void LDA(short ir_operand) {
    	System.out.println("load a");
    	ERegister.eMAR.setValue((short)(ir_operand + ERegister.eSP.getValue()));
    	ERegister.eMBR.setValue(this.memory.load(ERegister.eMAR.getValue()));
    	ERegister.eAC.setValue(ERegister.eMBR.getValue());
    	ERegister.ePC.value++;
    }
    private void STA(short ir_operand) {
    	System.out.println("store");
    	ERegister.eMAR.setValue((short)(ir_operand + ERegister.eSP.getValue()));
    	ERegister.eMBR.setValue(ERegister.eAC.getValue());
    	this.memory.store(ERegister.eMAR.getValue(), ERegister.eMBR.getValue());
    	ERegister.ePC.value++;
    }
    private void ADDA(short ir_operand) {
    	System.out.println("add a");
    	ALU.EALU.eStore.setValue(ERegister.eAC.getValue());
    	this.LDA(ir_operand);
    	ALU.EALU.eAdd.setValue((short)(ALU.EALU.eStore.getValue() + ERegister.eAC.getValue()));
    	ERegister.eAC.setValue(ALU.EALU.eAdd.getValue());
    }
    
    private void ADDC(short ir_operand) {
    	System.out.println("add a");
    	ALU.EALU.eStore.setValue(ERegister.eAC.getValue());
    	this.LDC(ir_operand);
    	ALU.EALU.eAdd.setValue((short)(ALU.EALU.eStore.getValue() + ERegister.eAC.getValue()));
    	ERegister.eAC.setValue(ALU.EALU.eAdd.getValue());
    }
    
    private void SUBA(short ir_operand) {
    	System.out.println("sub a");
    	ALU.EALU.eStore.setValue(ERegister.eAC.getValue());
    	this.LDA(ir_operand);
    	ALU.EALU.eSubtract.setValue((short)(ALU.EALU.eStore.getValue() - ERegister.eAC.getValue()));
    	ERegister.eAC.setValue(ALU.EALU.eSubtract.getValue());
    	
    	if((short)(ERegister.eAC.getValue()) < 0) {
    		ERegister.eSR.setValue((short)16384); // 4000
    	}else if((short)(ERegister.eAC.getValue()) == 0) {
    		ERegister.eSR.setValue((short) 32768); // 8000
    	} else {
    		ERegister.eSR.setValue((short) 0);
    	}
    }
    
    private void SUBC(short ir_operand) {
    	System.out.println("sub c");
    	ALU.EALU.eStore.setValue(ERegister.eAC.getValue());
    	this.LDC(ir_operand);
    	ALU.EALU.eSubtract.setValue((short)(ALU.EALU.eStore.getValue() - ERegister.eAC.getValue()));
    	ERegister.eAC.setValue(ALU.EALU.eSubtract.getValue());
    	
    	if((short)(ERegister.eAC.getValue()) < 0) {
    		ERegister.eSR.setValue((short)16384); // 4000
    	}else if((short)(ERegister.eAC.getValue()) == 0) {
    		ERegister.eSR.setValue((short) 32768); // 8000
    	} else {
    		ERegister.eSR.setValue((short) 0);
    	}
    }
    
    private void MULA(short ir_operand) {
    	System.out.println("multiply a");
    	ALU.EALU.eStore.setValue(ERegister.eAC.getValue());
    	this.LDA(ir_operand);
    	ALU.EALU.eMultiply.setValue((short)(ALU.EALU.eStore.getValue() * ERegister.eAC.getValue()));
    	ERegister.eAC.setValue(ALU.EALU.eMultiply.getValue());
    }
    
    private void DIVC(short ir_operand) {
    	System.out.println("div c");
    	ALU.EALU.eStore.setValue(ERegister.eAC.getValue());
    	this.LDC(ir_operand);
    	ERegister.eAC.setValue((short)(ALU.EALU.eStore.getValue() / ERegister.eAC.getValue()));
    }
    
    private void ANDA(short ir_operand) {
    }
    private void NOTA(short ir_operand) {
    }
    private void JMPZ(short ir_operand) {
    	CU cu = new CU();
    	System.out.println("JMPZ");
    	if(cu.isZero(ERegister.eSR.value)) {
    		ERegister.ePC.setValue(ir_operand);
    	} else ERegister.ePC.value++;
    }
    
    private void JMPBZEQ(short ir_operand) {
    	CU cu = new CU();
    	System.out.println("JMPBZEQ");
    	if(cu.isBZEQ(ERegister.eSR.value)) {
    		ERegister.ePC.setValue(ir_operand);
    	} else ERegister.ePC.value++;
    }
    
    private void JMP(short ir_operand) {
    	ERegister.ePC.setValue(ir_operand);
    	System.out.println("jmp");
    }
    
    private void JMPBZ(short ir_operand) {
    	CU cu = new CU();
    	System.out.println("JMPBZ");
    	if(cu.isBZ(ERegister.eSR.value)) {
    		ERegister.ePC.setValue(ir_operand);
    	} else ERegister.ePC.value++;
    }

	
    // status
    private boolean bPowerOn ;
    private boolean isPowerOn() { return this.bPowerOn; }
    public void setPowerOn(){
        this.bPowerOn = true;
        this.run();}
    public void shutDown() { this.bPowerOn = false; }
   
    
    public void fetch() {
    	System.out.println("<<<<<<<<fetch>>>>>>>>");
    	System.out.println("Program Counter: "+ERegister.ePC.getValue());
    	ERegister.eMAR.setValue(ERegister.ePC.getValue());
    	ERegister.eMBR.setValue(this.memory.load(ERegister.eMAR.getValue()));
    	ERegister.eIR.setValue(ERegister.eMBR.getValue());
    }
    
    public void execute() {
    	System.out.println("-------execute-------");
    	short ir_opcode =  (short)(ERegister.eIR.value >> 8);
    	short ir_operand = (short)(ERegister.eIR.value & 0x00ff);
    	System.out.println("opcode: " + ir_opcode + ", operand: " + ir_operand);
    	
    	if((short) ir_opcode == EOpCode.eHalt.getValue()){
        	this.Halt();
    	}else if((short) ir_opcode == EOpCode.eLDC.getValue()){
    		this.LDC(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eLDA.getValue()){
        	this.LDA(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eSTA.getValue()){
    		this.STA(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eADDA.getValue()){
        	this.ADDA(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eADDC.getValue()){
    		this.ADDC(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eSUBA.getValue()){
        	this.SUBA(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eSUBC.getValue()){
    		this.SUBC(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eMULA.getValue()){
        	this.MULA(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eDIVC.getValue()){
    		this.DIVC(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eANDA.getValue()){
    		this.ANDA(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eNOTA.getValue()){
        	this.NOTA(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eJMPZ.getValue()){
    		this.JMPZ(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eJMPBZEQ.getValue()){
        	this.JMPBZEQ(ir_operand);
    	}else if((short) ir_opcode == EOpCode.eJMP.getValue()){
    		this.JMP(ir_operand);
    	} else if((short) ir_opcode == EOpCode.eJMPBZ.getValue()){
    		this.JMPBZ(ir_operand);
    	} else {
    	 	shutDown();
    	}
    }
	
    
	public void run() {
		while (this.isPowerOn()) {
			this.fetch();
			this.execute();
		}
	}
	
	public static void main(String args[]) {
		CPU cpu = new CPU();
		Memory memory = new Memory();
		Loader loader = new Loader();
		cpu.associate(memory);
		loader.loadHeader();
		memory.readExe("exe2");
		cpu.setPowerOn();
//		System.out.println((short)(32768 & 0x8000));
//		System.out.println((short)(32768 & 0x4000));
//		System.out.println((short)(32768 & 0xc000));
//		System.out.println((short)(16384 & 0xc000));
//		System.out.println((short)(8192 & 0xc000));
	}
	
	public void setPC(short value) {
		ERegister.ePC.setValue(value);
		System.out.println("setPC: " + value);
	}
	public void setSP(short value) {
		ERegister.eSP.setValue(value);
		System.out.println("setSP: " + value);
	}
	

}

