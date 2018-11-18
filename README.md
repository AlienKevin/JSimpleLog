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
        // instantiate logger
        JSimpleLog log = new JSimpleLog();
        // categorize types under the category "foo"
        log.categorize("foo", foo1", "foo2");
        // set a category's logging behavior
        log.setFormLog("foo", false);
        //override category setting using more precise type setting
        log.setFormLog("foo1", true);
    }
    public int foo1(){
        log.setType("foo1");
        log.out("foo1 starts!");
        //do something
        log.out("foo1 ends!");
        log.reset();
    }
    public int foo2(){
        log.setType("foo2");
        log.out("foo2 starts!");
        //do something
        log.out("foo2 ends!");
        log.reset();
    }
```
This code should output:
```java
foo1 starts!
foo1 ends!
```
As you can see, `log.setFormLog("type/category name", logOrNot)` is applicable for both *type* and *category*. In fact, in JSimpleLog's lingo, *type* and *category* are types of *form*. More importantly, type specific logging behavior will always *override* category's logging behavior. In this example, `log.setFormLog("foo", false)` which disables the logging of type "foo1" and "foo2" is overrided by `log.setFormLog("foo1", true)` which enables the logging of type "foo1". Notice however, "foo2" is still disabled because it has no specific type setting to override its category setting.
#### 3. Disable logging for all unspecified forms
Maybe there's sometime when your console screen is clustered with irrelevant log messages and you want to only see the logging messages for several *types*. You can obviously turn off all logging using `off()`, but that will disable the log messages you want to see as well. Another choice is to categorize all *types* under a main *category* and then disable that *category*. Since you can override the *category* setting with individual *type* setting, you can still selectivly *specify* the logging behavior for the *types* you want (turning them on/off). To save you from keeping track of all *types* and writing tediously long log settings every time, JSimpleLog provides a method to do precisely that. To disable all unspecified *forms* (types/categories), call `log.setFormLog(JSimpleLog.UNSPECIFIED, false)`.
Here's an example of usage:
```java
    public static void main(String[] args){
        // instantiate logger
        JSimpleLog log = new JSimpleLog();
        // categorize types
        log.categorize("foo", foo1", "foo2");
        // diable logging for all unspecified forms
        log.setFormLog(JSimpleLog.UNSPECIFIED, false);
        // enable logging for type "foo1"
        log.setFormLog("foo1", true);
    }
    //same method as last example
    public int foo1(){
        log.setType("foo1");
        log.out("foo1 starts!");
        //do something
        log.out("foo1 ends!");
        log.reset();
    }
    //same method as last example
    public int foo2(){
        log.setType("foo2");
        log.out("foo2 starts!");
        //do something
        log.out("foo2 ends!");
        log.reset();
    }
```
This code should output:
```java
foo1 starts!
foo1 ends!
```
Even though you set the types for methods `foo1()` and `foo2() ` and categorize type "foo1" and "foo2" under the category "foo", only the logging behavior for type "foo1" is specified. 
Don't confuse **specification** with form **declaration**. 
- **Declaration** is when you set the *type* for a code block (using `setType("type name")`) or *categorize* several *types* together to form a *category* (using `categorize("category name", "type 1",...)`). 
- **Specification** is when you specify the logging behavior of a *type* or *category* (using `setFormLog("type/category name", logOrNot)`)

In this example, both type "foo1" and "foo2" are declared and even categorized. However, only type "foo1" is specified. Since `log.setFormLog(JSimpleLog.UNSPECIFIED, false)` disables logging for all *unspecified* forms, type "foo2" is disabled.
## License
This project is licensed under the terms of the MIT license.
