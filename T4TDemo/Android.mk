LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

# Only compile source java files in this apk.
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := T4TDemo
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_CERTIFICATE := platform
LOCAL_USES_LIBRARIES := com.nxp.nfc
LOCAL_JAVA_LIBRARIES := com.nxp.nfc
LOCAL_STATIC_JAVA_LIBRARIES += android-support-v7-appcompat

include $(BUILD_PACKAGE)
