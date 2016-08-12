#include <stdio.h>
#include <unistd.h>

#include "binderClient.h"

//wait 500 milliseconds
#define TIME_WAIT_FOR_REPLY 500

sp<IDemo> demo;

void printReplay(const char *tkn) {
    usleep(1000*TIME_WAIT_FOR_REPLY);
    printf("%s\n", demo->readReply(tkn));
}

int main(int argc, char **argv)
{
    demo = getDemoServ();
    demo->openRS485("/dev/ttyUSB0", 0, 9600);
    demo->sendCommand("token0", "#010001\r", 0, 0);
    demo->sendCommand("token1", "#010002\r", 0, 0);
    demo->sendCommand("token2", "#010003\r", 0, 0);
    demo->sendCommand("token3", "#010004\r", 0, 0);
    
    printReplay("token0");
    printReplay("token1");
    printReplay("token2");
    printReplay("token3");

    return 0;
}
