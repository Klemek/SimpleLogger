# Simple Logger
[![Maven Central](https://img.shields.io/maven-central/v/com.github.klemek/simple-logger.svg)](https://search.maven.org/search?q=g:%22com.github.klemek%22%20AND%20a:%22simplelogger%22)
[![Build Status](https://img.shields.io/travis/Klemek/SimpleLogger.svg?style=popout)](https://travis-ci.org/Klemek/SimpleLogger)
[![Scc Count Badge](https://sloc.xyz/github/klemek/simplelogger/?category=code)](https://github.com/boyter/scc/#badges-beta)
[![Coverage Status](https://img.shields.io/coveralls/github/Klemek/SimpleLogger.svg)](https://coveralls.io/github/Klemek/SimpleLogger?branch=master)
![License](https://img.shields.io/github/license/Klemek/SimpleLogger.svg)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Klemek/SimpleLogger.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Klemek/SimpleLogger/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/Klemek/SimpleLogger.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Klemek/SimpleLogger/alerts/)

A simple but useful Java logger to use everywhere.

Current version v1.3.1

## Download

[simplelogger-1.3.1.jar](../../releases/download/simplelogger-1.3.1/simplelogger-1.3.1.jar)

## How to use

Initialization :
```Java
Logger.init("logging.properties", Level.WARNING);
// or
Logger.init("logging.properties");
Logger.setLevel(Level.WARNING);
```

Configuration file as follow (example) :
```
# Parameters for good behavior
handlers=java.util.logging.ConsoleHandler
.level=WARNING
java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter

# Customize date/time shown with this line
java.util.logging.SimpleFormatter.format=[%1$tF %1$tT][%4$s]%5$s %n

# Specify your app name here
app_name=YourApp
# (Optional, specify your app package)
default_package=com.your.app
# (Optional, specify an output file pattern)
output_file=output.log
```

The default package works as follow : When logging stack trace of an error, it will stop when the package doesnt't match your app anymore. It helps having small and relevant stack trace in logs.

Usage :
```Java
//general logging
Logger.log("my message"); //INFO log
Logger.log("my message : {0}", 123); //INFO log with parameters
Logger.log(Level.WARNING, "my message"); //custom level log
Logger.log(Level.SEVERE, "my message : {0}-{1}", 123, 456); //custom level log with parameters
//exception logging
Logger.log(exception1); //SEVERE logging with stack trace
Logger.log(exception2, "exception when doing task"); //SEVERE logging with stack trace and message
Logger.log(Level.WARNING, exception3); //custom level logging without stack trace
Logger.log(Level.INFO, exception4, "exception when doing task"); //custom level logging with message
```
This will show the following :
```
[INFO][MyApp-CallingClass] my message
[INFO][MyApp-CallingClass] my message : 123
[WARNING][MyApp-CallingClass] my message
[SEVERE][MyApp-CallingClass] my message : 123-456
[SEVERE][MyApp-CallingClass] (exception1.toString)
[SEVERE][MyApp-CallingClass]    (exception1.stackTrace)
[SEVERE][MyApp-CallingClass]    (exception1.stackTrace)
[SEVERE][MyApp-CallingClass]    ...
[SEVERE][MyApp-CallingClass] exception when doing task : (exception2.toString)
[SEVERE][MyApp-CallingClass]    (exception2.stackTrace)
[SEVERE][MyApp-CallingClass]    (exception2.stackTrace)
[SEVERE][MyApp-CallingClass]    ...
[WARNING][MyApp-CallingClass] (exception3.toString)
[INFO][MyApp-CallingClass] exception when doing task : (exception4.toString)
```

## Maven

You can use this project as a maven dependency with this :
```XML
<dependency>
    <groupId>com.github.klemek</groupId>
    <artifactId>simple-logger</artifactId>
    <version>1.3.1</version>
</dependency>
```
