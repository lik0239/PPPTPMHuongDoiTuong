#!/bin/sh
set -e
PRG="$0"
while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then PRG="$link"; else PRG=`dirname "$PRG"`/"$link"; fi
done
BASEDIR=`dirname "$PRG"`
WRAPPER_JAR="$BASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES="$BASEDIR/.mvn/wrapper/maven-wrapper.properties"
if [ ! -f "$WRAPPER_JAR" ]; then
  WRAPPER_URL=`sed -n 's/^wrapperUrl=\(.*\)$/\1/p' "$WRAPPER_PROPERTIES"`
  echo "Downloading Maven Wrapper jar from: $WRAPPER_URL"
  mkdir -p "$BASEDIR/.mvn/wrapper"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR"
  else
    echo "Need curl or wget to download Maven Wrapper." >&2; exit 1
  fi
fi
exec java -classpath "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"
