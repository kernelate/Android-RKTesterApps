LOCAL_PATH      := $(call my-dir)

#include $(CLEAR_VARS)
#
#LOCAL_MODULE    := DT2led
#LOCAL_CFLAGS    := -Werror
#LOCAL_SRC_FILES := led_driver.c
#LOCAL_LDLIBS    := -llog
#
#include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := RKJNI
LOCAL_CFLAGS    := -Werror
LOCAL_SRC_FILES := rk_jni.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)


