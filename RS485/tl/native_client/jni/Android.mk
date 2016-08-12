LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := client
LOCAL_SRC_FILES := binderClient.cpp client.cpp
LOCAL_LDLIBS := -L$(LOCAL_PATH)/
LOCAL_LDLIBS += -lutils -lcutils -lbinder
include $(BUILD_EXECUTABLE)
