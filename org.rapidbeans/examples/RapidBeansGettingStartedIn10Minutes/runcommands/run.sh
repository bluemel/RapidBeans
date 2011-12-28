#!/bin/sh

RAPIDBEANS_FRAMEWORK_VERSION=0.9.1

# determine working directory for absolute paths
wd=`pwd`

# up Java classpath
CLASSPATH="${wd}/model"
CLASSPATH="${CLASSPATH}:${wd}/res"
CLASSPATH="${CLASSPATH}:${wd}/classes"
CLASSPATH="${CLASSPATH}:${wd}/lib/rapidbeans-framework-${RAPIDBEANS_FRAMEWORK_VERSION}.jar"

MAINCLASS="org.rapidbeans.presentation.Application"

# up vm arguments

# up locale
VMARGS="-Duser.language=@locale@"
VMARGS="${VMARGS} -Duser.country="
VMARGS="${VMARGS} -Duser.variant="
#VMARGS="${VMARGS} -Djava.util.logging.config.file=logging.properties"

# up program arguments
ARGS="-appname MyAddressbook"
ARGS="${ARGS} -approotpackage org.me.addressbook"
ARGS="${ARGS} -docroottype org.me.addressbook.Addressbook"
if [ ! -r "${HOME}/.myaddressbook/uistate.xml" ]; then
  ARGS="${ARGS} -loaddoc testdata/myaddressbook.xml"
fi

set -x
java ${VMARGS} -classpath "${CLASSPATH}" ${MAINCLASS} ${ARGS}
set +x
