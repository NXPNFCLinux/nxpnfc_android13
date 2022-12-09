#!/bin/bash
echo
echo "+++ Installing NXP-NCI NFC support for PN7160 +++"

echo
echo "- patching required files"
cd $ANDROID_BUILD_TOP/system/nfc
patch -p1 <$ANDROID_BUILD_TOP/vendor/nxp/nfc/patches/AROOT_system_nfc.patch
cd $ANDROID_BUILD_TOP/hardware/nxp/nfc
patch -p1 <$ANDROID_BUILD_TOP/vendor/nxp/nfc/patches/AROOT_hardware_nxp_nfc.patch
cd $ANDROID_BUILD_TOP/packages/apps/Nfc
patch -p1 <$ANDROID_BUILD_TOP/vendor/nxp/nfc/patches/AROOT_packages_apps_Nfc.patch
cd $ANDROID_BUILD_TOP/frameworks/native
patch -p1 <$ANDROID_BUILD_TOP/vendor/nxp/nfc/patches/AROOT_frameworks_native.patch
cd $ANDROID_BUILD_TOP/frameworks/base
patch -p1 <$ANDROID_BUILD_TOP/vendor/nxp/nfc/patches/AROOT_frameworks_base.patch
cd $ANDROID_BUILD_TOP/vendor/nxp
patch -p1 <$ANDROID_BUILD_TOP/vendor/nxp/nfc/patches/AROOT_vendor_nxp.patch
cd $ANDROID_BUILD_TOP/build/make
patch -p1 <$ANDROID_BUILD_TOP/vendor/nxp/nfc/patches/AROOT_build_make.patch
cd $ANDROID_BUILD_TOP/hardware/interfaces
patch -p1 <$ANDROID_BUILD_TOP/vendor/nxp/nfc/patches/AROOT_hardware_interfaces.patch
cd $ANDROID_BUILD_TOP/system/core
patch -p1 <$ANDROID_BUILD_TOP/vendor/nxp/nfc/patches/AROOT_system_core.patch
cd $ANDROID_BUILD_TOP

echo
echo "+++ NXP-NCI NFC support installation completed +++"
exit 0
