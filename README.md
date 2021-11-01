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


## Status Register
ALU와  CU는  Status Register를 통해 Flow of control를 할 수 있다.</br>
Flag register에는 다양한 종류가 있는데 여기서 사용한 flag는 3종류이다.
- *Zero Flag* : A-B의 값이 0인 경우, 1(true)을 return
- *Below Zero Flag*: A-B의 값이 음수인 경우, 1(true)을 return
- *Below Zero Equal Flag*: A-B의 값이 0이거나 음수인 경우, 1(true)을 return

 |16 bit에서 Zero Flag의 상태를 확인하는 경우| 16 bit에서 Carry Flag의 상태를 확인하는 경우|
 |:---:|:---:|
![image](https://user-images.githubusercontent.com/79343830/139642840-377d2bbf-91e5-4478-b07b-ecc27c531f89.png)|![image](https://user-images.githubusercontent.com/79343830/139643198-40f4438e-428d-45ab-a879-b3d384fec7ec.png)

