# Simple Logger
A simple but useful Java logger to use everywhere.

Current version v1.2.1

## Download

* [simple-logger-1.2.1.jar](../../raw/master/download/simple-logger-1.2.1.jar)

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
<repositories>
    ...
    <repository>
        <id>fr.klemek</id>
        <url>https://github.com/klemek/mvn-repo/raw/master</url>
    </repository>
</repositories>
...
<dependencies>
    ...
    <dependency>
        <groupId>fr.klemek</groupId>
        <artifactId>simple-logger</artifactId>
        <version>1.2.1</version>
    </dependency>
</dependencies>
```
