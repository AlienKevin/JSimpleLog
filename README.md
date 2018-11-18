# JSimpleLog
JSimpleLog is a friendly and structured logging API for Java. It has the capacity to easily filter log messages that you don't want to appear on your console to help you focus on the parts that really matter. You can assign type to a group of logging commands and categorize multiple types to form a category.

## Installation
Please download JSimpleLog.jar file from the repository and build that into your classpath.
If you are using Eclipse IDE, see this link for [how to import jar files](https://stackoverflow.com/questions/3280353/how-to-import-a-jar-in-eclipse).

## Usage
### Simplest use case
To turn **on** or **off** all loggings in a class
1. Instantiate a new JSimpleLog object
    ```java
    JSimpleLog log = new JSimpleLog();
    ```
2. When you want to log a message, instead of calling System.out.println, call:
    ```java
    log.out("Any message you want to output");
    ```
3. If you want to turn **off** all logging in a class, call:
    ```java
    log.off();
    ```
4. If you want to turn **on** all logging in a class, call:
    ```java
    log.on();
    ```
### More complex use case
#### 1. Types
If you want to control logging behavior for individual methods or a block or code, set the *type* of the logger at the beginning of the method/block:
```java
log.setType("foo");
```
**Always** remeber to add `log.reset()` before the block ends or the method returns:
```java
public int foo(){
    log.setType("foo");
    int a = 0;
    //do some calculations
    log.reset(); //always call reset() before returning
    return a;
}
```
If your method has multiple return statements, call `log.reset()` before each:
```java
public int foo(){
    log.setType("foo");
    int a = 0;
    //do some calculations
    if (a == 0){
        log.reset(); //always call reset() before returning
        return -1;
    } else{
        log.reset(); //always call reset() before returning
        return a;
    }
}
```
To turn on logging for a specific type, call
```java
setFormLog("foo", true);
```
To turn off logging for a specific type, call
```java
setFormLog("foo", false);
```
Now you know how to use **Types**, try avoid using too many `on()`s and `off()`s inside your code because too many of them will be very hard to manage as they are all scattered throughout the code. Instead, call `on()` or `off()` in a constructor or the beginning of a program to turn on/off *all* log messages.
#### 2. Categories
If you want to group multiple related *types*, create a category to manage them in one place. Syntax for creating a catelog is 
`log.categorize("category name", "type 1", "type 2", "type 3", ...)`
Let's see an example using categorization where two types - "foo1" and "foo2" - are categorized under "foo":
```java
    public static void main(String[] args){
        JSimpleLog log = new JSimpleLog();// instantiate logger
        log.categorize("foo", foo1", "foo2");// categorize types
        log.setFormLog("foo", false);// set a category's logging behavior
    }
    public int foo1(){
        log.setType("foo1");
        //do something
        log.reset();
    }
    public int foo2(){
        log.setType("foo2");
        //do something
        log.reset();
    }
```
As you can see, `log.setFormLog("type/category name", logOrNot)` is applicable for both *type* and *category*. In fact, in JSimpleLog's lingo, *type* and *category* are types of *form*.


