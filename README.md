## MCU Emulator


### Instruction set
|Instruction|OpCode|Meaning|
|:---:|:---:|:---:|
Halt| 0x00|Stop the clock
LDC x|0x01|Load AC constant x
LDA x|0x02|Load AC with (x)
STA x|0x03|Store AC in (x)
ADDA x|0x04|ADD(x) to AC
ADDC x| 0x05|ADD constant x to AC
SUBA x|0x06|Subtract(x) to AC
SUBC x|0x07|Subtract constant x to AC
MULA x|0x08| Multiply(x) to AC
DIVC x|0x09| Division constant x to AC
ANDA x| 0x0A|Logical and (x) to AC
NOTA x|0x0B|2's complement the AC
JMPZ x|0x0C|If Z-flag is true, Jump to x
JMPBZEQ x|0x0D|If BZEQ-flag is true, Jump to x
JMP x| 0x0E| Jump to x
JMPBZ x|0x0F| If BZ-flag is true, Jump to x
