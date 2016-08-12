//
//  binderClient.cpp
//  
//
//  Created by qic on 2016/3/14.
//
//

#include "binderClient.h"

void assert_fail(const char *file, int line, const char *func, const char *expr) {
    abort();
}

#define ASSERT(e) \
do { \
if (!(e)) \
assert_fail(__FILE__, __LINE__, __func__, #e); \
} while(0)


// Where to print the parcel contents: aout, alog, aerr. alog doesn't seem to work.
#define PLOG aout


BpDemo::BpDemo(const sp<IBinder>& impl) : BpInterface<IDemo>(impl) {
}

void BpDemo::openRS485(const char *path, int32_t port, int32_t baud_rate) {
    Parcel data, reply;
    data.writeInterfaceToken(IDemo::getInterfaceDescriptor());
    data.writeString16(String16(path));
    data.writeInt32(port);
    data.writeInt32(baud_rate);
    remote()->transact(OPEN_RS485, data, &reply);
}

void BpDemo::sendCommand(const char *token, const char *cmd, int32_t port, int32_t priority) {
    Parcel data, reply;
    data.writeInterfaceToken(IDemo::getInterfaceDescriptor());
    data.writeString16(String16(token));
    data.writeString16(String16(cmd));
    data.writeInt32(port);
    data.writeInt32(priority);
    remote()->transact(SEND_COMMAND, data, &reply);
}

const char* BpDemo::readReply(const char *token) {
    Parcel data, reply;
    data.writeInterfaceToken(IDemo::getInterfaceDescriptor());
    data.writeString16(String16(token));
    remote()->transact(READ_REPLY, data, &reply);
    reply.readExceptionCode();
    String16 msg = reply.readString16();
    return String8(msg).string();
}

//IMPLEMENT_META_INTERFACE(Demo, "Demo");
// Macro above expands to code below. Doing it by hand so we can log ctor and destructor calls.
const android::String16 IDemo::descriptor("Demo");
const android::String16& IDemo::getInterfaceDescriptor() const {
    return IDemo::descriptor;
}
android::sp<IDemo> IDemo::asInterface(const android::sp<android::IBinder>& obj) {
    android::sp<IDemo> intr;
    if (obj != NULL) {
        intr = static_cast<IDemo*>(obj->queryLocalInterface(IDemo::descriptor).get());
        if (intr == NULL) {
            intr = new BpDemo(obj);
        }
    }
    return intr;
}
IDemo::IDemo() {}
IDemo::~IDemo() {}
// End of macro expansion

sp<IDemo> getDemoServ() {
    sp<IServiceManager> sm = defaultServiceManager();
    ASSERT(sm != 0);
    sp<IBinder> binder = sm->getService(String16("Demo"));
    // TODO: If the "Demo" service is not running, getService times out and binder == 0.
    ASSERT(binder != 0);
    sp<IDemo> demo = interface_cast<IDemo>(binder);
    ASSERT(demo != 0);
    return demo;
}
