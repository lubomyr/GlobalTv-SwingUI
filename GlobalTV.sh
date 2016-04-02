#!/bin/sh

# Absolute path to this script, e.g. /home/user/bin/foo.sh
SCRIPT=$(readlink -f "$0")
# Absolute path this script is in, thus /home/user/bin
SCRIPTPATH=$(dirname "$SCRIPT")
echo $SCRIPTPATH
cd $SCRIPTPATH
java -cp lib/json-20160212.jar:out/production/GlobalTv-SwingUI atua.anddev.globaltv.MainActivity