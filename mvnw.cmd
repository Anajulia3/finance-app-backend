@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------
@echo off

set MAVEN_PROJECTBASEDIR=%~dp0
set MVNW_REPOURL=https://repo.maven.apache.org/maven2

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
set DOWNLOAD_URL="%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

if exist %WRAPPER_JAR% goto execute

echo Downloading Maven Wrapper...
powershell -Command "Invoke-WebRequest -Uri %DOWNLOAD_URL% -OutFile %WRAPPER_JAR%"
if %errorlevel% neq 0 goto error

:execute
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if not exist "%JAVA_EXE%" set JAVA_EXE=java

%JAVA_EXE% -jar %WRAPPER_JAR% %WRAPPER_LAUNCHER% %*
goto end

:error
echo Failed to download Maven Wrapper
exit /b 1

:end
