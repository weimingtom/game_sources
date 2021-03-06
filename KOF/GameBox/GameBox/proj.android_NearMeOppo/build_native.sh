APPNAME="GameBox"

# options

buildexternalsfromsource=

usage(){
cat << EOF
usage: $0 [options]

Build C/C++ code for $APPNAME using Android NDK

OPTIONS:
-s	Build externals from source
-h	this help
EOF
}

while getopts "sh" OPTION; do
case "$OPTION" in
s)
buildexternalsfromsource=1
;;
h)
usage
exit 0
;;
esac
done

# paths

if [ -z "${NDK_ROOT+aaa}" ];then
echo "please define NDK_ROOT"
exit 1
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# ... use paths relative to current directory
COCOS2DX_ROOT="$DIR/../.."
APP_ROOT="$DIR/.."
APP_ANDROID_ROOT="$DIR"

echo "NDK_ROOT = $NDK_ROOT"
echo "COCOS2DX_ROOT = $COCOS2DX_ROOT"
echo "APP_ROOT = $APP_ROOT"
echo "APP_ANDROID_ROOT = $APP_ANDROID_ROOT"

# make sure assets is exist
if [ -d "$APP_ANDROID_ROOT"/assets ]; then
    rm -rf "$APP_ANDROID_ROOT"/assets
fi

mkdir "$APP_ANDROID_ROOT"/assets

# copy resources
for file in "$APP_ROOT"/Resources/*
do
if [ -d "$file" ]; then
    cp -rf "$file" "$APP_ANDROID_ROOT"/assets
fi

if [ -f "$file" ]; then
    cp "$file" "$APP_ANDROID_ROOT"/assets
fi
done

#mi file
file="$APP_ANDROID_ROOT"/MiGameCenterSDKService.apk

echo "get file $file"
if [ -f "$file" ]; then
cp "$file" "$APP_ANDROID_ROOT"/assets/MiGameCenterSDKService.apk
echo "file copy to $APP_ANDROID_ROOT/assets/MiGameCenterSDKService.apk"
fi



# copy icons (if they exist)
file="$APP_ANDROID_ROOT"/Icon-136.png
if [ -f "$file" ]; then
	cp "$file" "$APP_ANDROID_ROOT"/res/drawable-hdpi/icon.png
fi
file="$APP_ANDROID_ROOT"/Icon-90.png
if [ -f "$file" ]; then
	cp "$file" "$APP_ANDROID_ROOT"/res/drawable-mdpi/icon.png
fi
file="$APP_ANDROID_ROOT"/Icon-90.png
if [ -f "$file" ]; then
	cp "$file" "$APP_ANDROID_ROOT"/res/drawable-ldpi/icon.png
fi


if [[ "$buildexternalsfromsource" ]]; then
    echo "Building external dependencies from source"
    "$NDK_ROOT"/ndk-build -C "$APP_ANDROID_ROOT" $* \
        "NDK_MODULE_PATH=${COCOS2DX_ROOT}:${COCOS2DX_ROOT}/cocos2dx/platform/third_party/android/source"
else
    echo "Using prebuilt externals"
    "$NDK_ROOT"/ndk-build -C "$APP_ANDROID_ROOT" $* \
        "NDK_MODULE_PATH=${COCOS2DX_ROOT}:${COCOS2DX_ROOT}/cocos2dx/platform/third_party/android/prebuilt"
fi




file="$APP_ANDROID_ROOT"/libs/libchannel.so

echo "get file $file"
if [ -f "$file" ]; then
cp "$file" "$APP_ANDROID_ROOT"/libs/armeabi/libchannel.so
echo "file copy to $APP_ANDROID_ROOT/libs/armeabi/libchannel.so"
fi

file="$APP_ANDROID_ROOT"/libs/libwwslogin.so

echo "get file $file"
if [ -f "$file" ]; then
cp "$file" "$APP_ANDROID_ROOT"/libs/armeabi/libwwslogin.so
echo "file copy to $APP_ANDROID_ROOT/libs/armeabi/libwwslogin.so"
fi

file="$APP_ANDROID_ROOT"/wws_sdk_download.png

echo "get file $file"
if [ -f "$file" ]; then
cp "$file" "$APP_ANDROID_ROOT"/assets/wws_sdk_download.png
echo "file copy to $APP_ANDROID_ROOT/assets/wws_sdk_download.png"
fi

file="$APP_ANDROID_ROOT"/wws_sdk_logo.png

echo "get file $file"
if [ -f "$file" ]; then
cp "$file" "$APP_ANDROID_ROOT"/assets/wws_sdk_logo.png
echo "file copy to $APP_ANDROID_ROOT/assets/wws_sdk_logo.png"
fi

file="$APP_ANDROID_ROOT"/wwsdata.xml

echo "get file $file"
if [ -f "$file" ]; then
cp "$file" "$APP_ANDROID_ROOT"/assets/wwsdata.xml
echo "file copy to $APP_ANDROID_ROOT/assets/wwsdata.xml"
fi