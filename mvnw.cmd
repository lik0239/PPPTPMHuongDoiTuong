@echo off
setlocal

rem -- Xác định thư mục gốc project (chứa pom.xml và .mvn)
set "MAVEN_PROJECTBASEDIR=%~dp0"
if "%MAVEN_PROJECTBASEDIR:~-1%"=="\" set "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%"

rem -- Đường dẫn wrapper jar
set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"

rem -- Lớp launcher của Maven Wrapper
set "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

rem -- Tìm java.exe
if defined JAVA_HOME (
  set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
  set "JAVA_EXE=java"
)

rem -- Chạy Maven Wrapper, CHÚ Ý tham số -Dmaven.multiModuleProjectDirectory
"%JAVA_EXE%" ^
  -classpath "%WRAPPER_JAR%" ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% ^
  %WRAPPER_LAUNCHER% %*

endlocal & exit /b %errorlevel%

