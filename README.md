# adamframework
This is POC of plain Dependency Injection and management


# how to use?

## 1. Initialize application
In order to create a new AdamFramework application create main method and initalize AdamContainer by providing base package directory.

```java
   public static void main(String[] args){
      AdamContainer.getInstance().init("com.company");
   }
```
## 2. Create AdamBeans
To use dependency management and injection provided by AdamFramework use annotations AdamBean and AdamInterface.
Firstly you create a POJO and its interface with all the method defined. Secondly you annotate both interface and class with @AdamBean and @AdamInterface annotations i.e.:


```java
@AdamInterface
public interface TestInterface {
    String sayHello();
}
```

```java
@AdamBean
public class TestBean implements TestInterface {

    @Override public String sayHello() {
        log.info("Saying hello");
        return "hello";
    }
}
```

## 3. Inject object instances via DI
There are two modes of objects: they can be singletons or prototypes. Singleton is just one instance for whole application. Prototype creates a new instance each time.

In order to get a singleton instance use i.e.:

```java
TestInterface testBean = (TestInterface) AdamContainer.getInstance().getSingleton(TestInterface.class);
testBean.sayHello();
```

Prototype:

```java
TestInterface testBean = (TestInterface) AdamContainer.getInstance().createPrototype(TestInterface.class);
testBean.sayHello();
```

## 4. Use AdamFramework Aspects
For now there is only @Transaction aspect configured. In the future there might be more annotations with AOP and its business logic.
Example usage:

```java
@AdamBean
public class TestBean implements TestInterface {

    @Transaction
    @Override public String sayHello() {
        log.info("Saying hello");
        return "hello";
    }
}
```
When calling sayHello method you should see in the logs:
159 [main] INFO org.adam.framework.aspect.transaction.TransactionAspect - Found TRANSACTION annotation: Doing custom logic here.


