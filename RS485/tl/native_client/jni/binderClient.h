//
//  binderClient.hpp
//  
//
//  Created by qic on 2016/3/14.
//
//

#ifndef binderClient_h
#define binderClient_h

#include <stdio.h>

#include <utils/RefBase.h>
#include <utils/Log.h>
#include <binder/TextOutput.h>

#include <binder/IInterface.h>
#include <binder/IBinder.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>
#include <binder/IPCThreadState.h>

using namespace android;

// Interface (our AIDL) - Shared by server and client
class IDemo : public IInterface {
public:
public:
    enum
    {
        OPEN_RS485=IBinder::FIRST_CALL_TRANSACTION,
        SEND_COMMAND,
        READ_REPLY
    };
    //open or re-open RS485 port with new baud rate
    virtual void openRS485(const char *path, int32_t port, int32_t baud_rate) = 0;
    //send RS485 command
    virtual void sendCommand(const char *token, const char *cmd, int32_t port, int32_t priority) = 0;
    //read rs485 replys via tokens
    virtual const char* readReply(const char *token) = 0;
    
    DECLARE_META_INTERFACE(Demo);  // Expands to 5 lines below:
};

// Client
class BpDemo : public BpInterface<IDemo> {
public:
    BpDemo(const sp<IBinder>& impl );
    virtual void openRS485(const char *path, int32_t port, int32_t baud_rate);
    virtual void sendCommand(const char *token, const char *cmd, int32_t port, int32_t priority);
    virtual const char* readReply(const char *token);
};

sp<IDemo> getDemoServ();

#endif /* binderClient_h */
