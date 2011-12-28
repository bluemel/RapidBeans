@echo off
setlocal

set RAPIDBEANS_FRAMEWORK_VERSION=0.9.1

:: determine working directory for absolute paths
set wd=%~dp0
set wdslash=%wd:\=/%

:: set up Java classpath
set CLASSPATH=%wdslash%model
set CLASSPATH=%CLASSPATH%;%wdslash%res
set CLASSPATH=%CLASSPATH%;%wdslash%classes
set CLASSPATH=%CLASSPATH%;%wdslash%lib/rapidbeans-framework-%RAPIDBEANS_FRAMEWORK_VERSION%.jar

set MAINCLASS=org.rapidbeans.presentation.Application

:: set up vm arguments

:: set up locale
set VMARGS=-Duser.language=en
set VMARGS=%VMARGS% -Duser.country=
set VMARGS=%VMARGS% -Duser.variant=
::set VMARGS=%VMARGS% -Djava.util.logging.config.file=logging.properties

:: set up program arguments
set ARGS=-appname MyAddressbook
set ARGS=%ARGS% -approotpackage org.me.addressbook
set ARGS=%ARGS% -docroottype org.me.addressbook.Addressbook

@echo on
java %VMARGS% -classpath "%CLASSPATH%" %MAINCLASS% %ARGS%
@endlocal
