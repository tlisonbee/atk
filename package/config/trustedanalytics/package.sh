#!/bin/bash
workDir=$(pwd)
baseDir=${workDir##*/}
gitRoot="."
if [ "$baseDir" == "package" ]; then
	source common.sh
	gitRoot=".."
	else
	source package/common.sh
	gitRoot="."
fi

packageName=$1
VERSION=$VERSION
BUILD_DIR=$BUILD_DIR

echo $packageName
echo $VERSION
echo $BUILD_DIR

echo "$SCRIPTPATH"

pwd

mkdir -p  ${BUILD_DIR}/etc/trustedanalytics/rest-server
mkdir -p  ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/lib

#copy example scripts
mkdir -p ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/examples/
cp -Rv ../python-client/examples/end-user/* ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/examples/

if [ -d /home/agent/datasets ]; then
    #copy datasets from agent home if it exists into the rpm tar.gz source
    cp -Rv /home/agent/datasets ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/examples
fi


cp -v ../conf/examples/application.conf.tpl ${BUILD_DIR}/etc/trustedanalytics/rest-server
cp -v ../conf/examples/parcel.conf.tpl      ${BUILD_DIR}/etc/trustedanalytics/rest-server
cp -v ../conf/examples/application.conf.single-system.tpl ${BUILD_DIR}/etc/trustedanalytics/rest-server

# DAAL jar and dynamic libraries
cp -v ../engine-plugins/daal-plugins/lib/daal.jar ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/lib/
cp -v ../engine-plugins/daal-plugins/lib/libAtkDaalJavaAPI.so ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/lib/
cp -v ../engine-plugins/daal-plugins/lib/libiomp5.so ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/lib/
cp -v ../engine-plugins/daal-plugins/lib/libJavaAPI.so ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/lib/
cp -v ../engine-plugins/daal-plugins/lib/libtbb.so.2 ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/lib/

pushd $SCRIPTPATH
    cp -Rv assets/* ${BUILD_DIR}
popd

#excluded jars are now combined in deploy.jar
# giraph-plugins.jar graphon.jar
jars=" rest-server.jar interfaces.jar engine-core.jar deploy.jar scoring-models.jar scoring-engine.jar scoring-interfaces.jar"

pushd ..
for jar in $jars
do
	jarPath=$(find .  -path ./package -prune -o -name $jar -print )
	echo $jarPath
	cp -v $jarPath ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/lib/

done

jarPath=$(find .  -path ./package -prune -o -name launcher.jar -print)

echo $jarPath
#enable this to copy the regular launcher.jar to the correct place
cp -v $jarPath ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/launcher.jar

mkdir -p ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/client
ls -l package/config/trustedanalytics-python-client/trustedanalytics/dist
cp -v package/config/trustedanalytics-python-client/trustedanalytics/dist/trustedanalytics*.tar.gz ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/client
ls -l python-client/target
cp -v python-client/target/trustedanalytics.zip ${BUILD_DIR}/usr/lib/trustedanalytics/rest-server/lib

popd

log "createArchive $packageName"
createArchive $packageName
