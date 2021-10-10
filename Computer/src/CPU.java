

import java.io.IOException;

public class CPU {
    
    // declaration
    private enum ERegister {
        ePC,
		eSP,
		eAC,
		eIR,
		eStatus,
		eMAR,
		eMBR
    }

    private enum EOpCode {
		eHalt,
		eLDACC, // AC <- constant
		eLDACA,
		eLDIRA,
		eSTA,
		eADD,
		eSub,
		eEQ,
		eGT,
		eBEQ,
		eBGT,
		eBranch
	}

    private class ALU {
		public void add() {
			// TODO Auto-generated method stub	
		}
		public void subtract() {
			// TODO Auto-generated method stub	
		}
		public void equal() {
			// TODO Auto-generated method stub	
		}
		public void graterThan() {
			// TODO Auto-generated method stub
		}
    }

    private class CU {
    }

    private class Register {
        protected short value;
        public short getValue() { return this.value;}
        public void setValue(short value){ this.value = value; }
    }

    class IR extends Register{
        public short getOpCode() { return (short) (this.value >> 8);}      // 8비트 밀어서 IR의 Opcode를 가져온다
        public short getOperand() { return (short) (this.value & 0x00FF);} // &연신자를 이용해 IR의 Operand 값을 가져온다
    }


    //component
    private CU cu;
    private ALU alu;
    private Register registers[];

    // association
    private Memory memory;

    // status
    private boolean bPowerOn = true;
    private boolean isPowerOn() { return this.bPowerOn; }
    public void setPowerOn(){
        this.bPowerOn = true; // 임의로 true로 설정
        this.run();
    }
    public void shutDown() { this.bPowerOn = false; }

    //constructor
    public CPU() {
        this.cu = new CU();
        this.alu = new ALU();
        this.registers = new Register[ERegister.values().length];
        for(ERegister eRegister: ERegister.values()){
            this.registers[eRegister.ordinal()] = new Register();
        }
    }

    public void associate(Memory memory) throws IOException{
        this.memory = memory;
		memory.read();
		System.out.println("dataSegment memory:" + memory.dataSegment[0]);

    }

	private void fetch() {
		// pc -> mar 
		this.registers[ERegister.eMAR.ordinal()].setValue(this.registers[ERegister.ePC.ordinal()].getValue());
		//memory -> mbr
	   ////// this.memory.load();
		// mbr -> IR
		this.registers[ERegister.eIR.ordinal()].setValue(this.registers[ERegister.eMBR.ordinal()].getValue());
	}

	private void loadACC() {
		short operand = (short) ((IR) this.registers[ERegister.eIR.ordinal()]).getOperand();
		this.registers[ERegister.eAC.ordinal()].setValue(operand);
	}

	private void loadACA() {
		short address = (short) ((IR) this.registers[ERegister.eIR.ordinal()]).getOperand();
		this.registers[ERegister.eMAR.ordinal()].setValue(address);
		short data = this.memory.load(this.registers[ERegister.eMAR.ordinal()].getValue());
		this.registers[ERegister.eMBR.ordinal()].setValue(data);
		this.registers[ERegister.eAC.ordinal()].setValue(this.registers[ERegister.eMBR.ordinal()].getValue());
	}
	
	private void loadIRA() { 
		short address = this.registers[ERegister.ePC.ordinal()].getValue();
		this.registers[ERegister.eMAR.ordinal()].setValue(address);
		short data = this.memory.load(this.registers[ERegister.eMAR.ordinal()].getValue());
		this.registers[ERegister.eMBR.ordinal()].setValue(data);
		this.registers[ERegister.eIR.ordinal()].setValue(this.registers[ERegister.eMBR.ordinal()].getValue());
	}

	private void store(){ //memory에 mar값과 mbr값을 저장한다.
 		this.memory.store(this.registers[ERegister.eMAR.ordinal()].getValue(), this.registers[ERegister.eMBR.ordinal()].getValue());
	}

	private void add(){
		this.registers[ERegister.eAC.ordinal()].setValue(this.registers[ERegister.eMBR.ordinal()].getValue());
			// this.load();
		this.alu.add();
	}

	private void subtract(){
	}

	private void equal(){
	}

	private void graterThan(){
	}

	private void halt(){ // stop the clock

	}

	
	private void execute() {
		switch(EOpCode.values()[((IR) this.registers[ERegister.eIR.ordinal()]).getOpCode()]) {
		case eHalt:
			this.halt();
			break;
        case eLDACC:
			this.loadACC();
			break;
		case eLDACA:
			this.loadACA();
			break;
		case eLDIRA:
			this.loadIRA();
			break;
		case eSTA:
			this.store();
			break;
		case eADD:
			this.add();
			break;
		case eSub:
			this.subtract();
			break;
		case eEQ:
			this.equal();
			break;
		case eGT:
			this.graterThan();
			break;
		case eBEQ:
			break;
		case eBGT:
			break;
		case eBranch:
			break;
		default:
			break;
		}
	}

	private void checkInterrupt() {
		
	}

	public void run() {
		while (this.isPowerOn()) {
		this.fetch();
		this.execute();
		this.checkInterrupt();
		}
	}
	
	public static void main(String args[]) throws Exception{
		CPU cpu = new CPU();
		Memory memory = new Memory();
		cpu.associate(memory);
		cpu.setPowerOn();
}

}