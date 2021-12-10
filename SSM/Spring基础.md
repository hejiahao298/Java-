# Spring基础

- IoC（控制反转）/ DI（依赖注入）
- AOP（面向切面编程）

**Spring原理**

spring解析xml配置的第三方库需要的是dom4j,使用的技术是java,代码布局会按照Document、Element、BeanCreator的方式进行，首先定义相关的接口，然后定义子类去实例化，这里采用的是状态设计模式。

![1066538-20170511225145676-757358487](C:\Users\27600\Desktop\1066538-20170511225145676-757358487.png)

**一，Spring的作用**

Spring作为Bean的管理容器，在我们的项目构建中发挥了举足轻重的作用，尤其是控制反转(IOC)和依赖(DI)注入的特性，将对象的创建完全交给它来实现，当我们把与其他框架进行整合时，比如与Mybatis整合，可以把sqlMapClientTemplate、数据源等Bean交给它来管理，这样在我们程序需要的时候，只需要调用它的getBean(String id)方法就可以获取它的一个实例。

**二，Spring的配置文件**

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:tx="http://www.springframework.org/schema/tx" xmlns:p="http://www.springframework.org/schema/p"
    xmlns:util="http://www.springframework.org/schema/util" xmlns:jdbc="http://www.springframework.org/schema/jdbc"
    xmlns:cache="http://www.springframework.org/schema/cache"
    xsi:schemaLocation="  
    http://www.springframework.org/schema/context  
    http://www.springframework.org/schema/context/spring-context.xsd  
    http://www.springframework.org/schema/beans  
    http://www.springframework.org/schema/beans/spring-beans.xsd  
    http://www.springframework.org/schema/tx  
    http://www.springframework.org/schema/tx/spring-tx.xsd  
    http://www.springframework.org/schema/jdbc  
    http://www.springframework.org/schema/jdbc/spring-jdbc-3.1.xsd  
    http://www.springframework.org/schema/cache  
    http://www.springframework.org/schema/cache/spring-cache-3.1.xsd  
    http://www.springframework.org/schema/aop  
    http://www.springframework.org/schema/aop/spring-aop.xsd  
    http://www.springframework.org/schema/util  
    http://www.springframework.org/schema/util/spring-util.xsd">

    <bean id="pad1" class="com.wyq.Bean.Pad" scope="singleton">
        <constructor-arg>
            <value type="java.lang.double">1999.9</value>
        </constructor-arg>
    </bean>
    <!-- 懒加载 -->
    <bean id="pad2" class="com.wyq.Bean.Pad" lazy-init="true"
        autowire="no"></bean>

    <bean id="person" class="com.wyq.Bean.Person" autowire="byName">
        <property name="name" value="Yrion"></property>
    </bean>
    

</beans>
```



> <bean> :  映射entity对象
>
> > id : 对象名 - - applicationcontext(id)
> >
> > class : 对象的模版类（通过反射机制forName()拿到的运行时类，在通过class.getConstructor拿到无参构造,在通过constructor.newInstance()拿到对象）
> >
> > scope : bean的作用域 
> >
> > > singleton：单例，表示通过 IoC 容器获取的 bean 是唯一的。
> > >
> > > prototype：原型，表示通过 IoC 容器获取的 bean 是不同的。
> >
> > lazy-init：懒加载
> >
> > > true: 当创建IoC容器时，bean中对象不会实例化，当IoC容器需要实例化某个bean中的对象，对象才被实例化
> > >
> > > false: 为默认，启动Spring框架会默认加载整个对象实例图
> >
> > autowire：自动装载，当对象的属性里有其它引用变量时，可以不用ref赋值，通过自动装载，可以完成自动赋值
> >
> > > byName: 通过属性名自动装载
> > >
> > > byType: 通过属性的数据类型自动装载，需要注意，如果同时存在两个及以上的符合条件的 bean 时，自动装载会抛出异常。
> >
> > depends-on: 设置依赖关系
> >
> > parent：设置继承关系
> >
> > factory-bean="shapeFactroy"  : 实例工厂的id
> >
> > factory-method="getShape" ：工厂获取产品的方法
>
> <property> : 对象的属性
>
> > name : 成员变量名。
> >
> > value : 成员变量值(不能给引用变量赋值，引用变量通过ref)
> >
> > ref : 将 IoC 中的另外一个 bean 赋给当前的成员变量（DI）
>
> <constructor-arg>：给构造函数或方法传入形参
>
> > value : type



## IOC  控制反转

**Inversion of Control**，java程序的一种设计思想，Ioc意味着将你设计好的对象交给容器控制，而不是传统的在你的对象内部直接控制。

**IoC对编程带来的最大改变不是从代码上，而是从思想上，发生了“主从换位”的变化。应用程序原本是老大，要获取什么资源都是主动出击，但是在IoC/DI思想中，应用程序就变成被动的了，被动的等待IoC容器来创建并注入它所需要的资源了。**

### 如何使用IOC

1. 创建 Maven 工程，pom.xml 添加依赖

```xml
   <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.0.11.RELEASE</version>
        </dependency>
    </dependencies>
```

2. 创建实体类 Student

```java

@Data
public class Student {
    private long id;
    private String name;
    private int age;
}
```

3. 通过 IoC 创建对象，在配置文件中添加需要管理的对象(xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
	http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd
">

    <bean id="student" class="com.southwind.entity.Student">
        <property name="id" value="1"></property>
        <property name="name" value="张三"></property>
        <property name="age" value="22"></property>
    </bean>
</beans>
```

4. 从 IoC 中获取对象，通过 id 获取。

```java
//加载配置文件
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
Student student = (Student) applicationContext.getBean("student");
System.out.println(student);
```



### IoC 底层原理

- 读取配置文件，解析 XML。
- 通过反射机制实例化配置文件中所配置所有的 bean。

```java
public class ClassPathXmlApplicationContext implements ApplicationContext{
     private Map<String,Object> ioc = new HashMap<String,Object>();
     public ClassPathXmlApplicationContext(String path){
         try{
             SAXReader reader = new SAXReader();  //借用dom4j的解析器
             //获取到xml文件
             Document document = reader.read("./src/mian/resources/"+path);
             //获取到根节点
             Element root = document.getRootElement();
             //获取到根节点元素的遍历器，遍历根节点
             Iterator<Element> iteraor = root.elementIterator();
             while(iterator.hasNext()){
                 //获取到根节点的元素（bean）
                 Element element = iterator.next();
                 String id = element.attributeValue("id");
                 String className = element.attributeValue("class");
                 //通过反射拿到对象
                 Class clazz = Class.forName(className);
                 Constructor constructor = clazz.getConstructor();
                 Object object = constructor.newInstance();
                 //获取到子节点的遍历器，遍历子节点
                 Iterator<Element> beanIter = element.elementIterator();
                 while(beanIter.hasNext()){
                     Element property = beanIter.next();
                     String name = property.attributeValue("name");
                     String valueStr = property.attributeValue("value");
                     String ref = property.attributeValue("ref");
                     if(ref == null){
                         String methodName = "set"+name.subString(0,1).toUpperCase()+name.substring(1);
                         Field field = clazz.getDeclaredField(name);
                         Method method = clzz.getDeclaredMethod(methodName,field.getType());
                         //根据成员变量的数据类型将 value 进行转换
                        Object value = null;
                        if(field.getType().getName() == "long"){
                            value = Long.parseLong(valueStr);
                        }
                        if(field.getType().getName() == "java.lang.String"){
                            value = valueStr;
                        }
                        if(field.getType().getName() == "int"){
                            value = Integer.parseInt(valueStr);
                        }
                        method.invoke(object,value);
                    }
                    ioc.put(id,object);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (NoSuchMethodException e){
            e.printStackTrace();
        } catch (InstantiationException e){
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        } catch (InvocationTargetException e){
            e.printStackTrace();
        } catch (NoSuchFieldException e){
            e.printStackTrace();
        }
    }

    public Object getBean(String id) {
        return ioc.get(id);
    }
}
                     }
                 }
             }
         }
     }
}
```



### 通过有参构造创建 bean

- 在实体类中创建对应的有参构造函数。
- 配置文件

```xml
<bean id="student3" class="com.southwind.entity.Student">
    <constructor-arg name="id" value="3"></constructor-arg>
    <constructor-arg name="name" value="小明"></constructor-arg>
    <constructor-arg name="age" value="18"></constructor-arg>
    <constructor-arg name="address" ref="address"></constructor-arg>
</bean>
```



```xml
<bean id="student3" class="com.southwind.entity.Student">
    <constructor-arg index="0" value="3"></constructor-arg>
    <constructor-arg index="2" value="18"></constructor-arg>
    <constructor-arg index="1" value="小明"></constructor-arg>
    <constructor-arg index="3" ref="address"></constructor-arg>
</bean>
```



**给 bean 注入集合**

```xml
<bean id="student" class="com.southwind.entity.Student">
    <property name="id" value="2"></property>
    <property name="name" value="李四"></property>
    <property name="age" value="33"></property>
    <property name="addresses">
        <list>
            <ref bean="address"></ref>
            <ref bean="address2"></ref>
        </list>
    </property>
</bean>
```



### scope 作用域

Spring 管理的 bean 是根据 scope 来生成的，表示 bean 的作用域，共4种，默认值是 singleton。

- singleton：单例，表示通过 IoC 容器获取的 bean 是唯一的。
- prototype：原型，表示通过 IoC 容器获取的 bean 是不同的。
- request：请求，表示在一次 HTTP 请求内有效。
- session：回话，表示在一个用户会话内有效。

request 和 session 只适用于 Web 项目，大多数情况下，使用单例和原型较多。

prototype 模式当业务代码获取 IoC 容器中的 bean 时，Spring 才去调用无参构造创建对应的 bean。

singleton 模式无论业务代码是否获取 IoC 容器中的 bean，Spring 在加载 spring.xml 时就会创建 bean。



### Spring 的继承

与 Java 的继承不同，Java 是类层面的继承，子类可以继承父类的内部结构信息；Spring 是对象层面的继承，子对象可以继承父对象的属性值。

```xml
<bean id="stu" class="com.southwind.entity.Student" parent="student">
    <property name="name" value="李四"></property>
</bean>
```

Spring 的继承关注点在于具体的对象，而不在于类，即不同的两个类的实例化对象可以完成继承，前提是子对象必须包含父对象的所有属性，同时可以在此基础上添加其他的属性。



### Spring 的依赖

与继承类似，依赖也是描述 bean 和 bean 之间的一种关系，配置依赖之后，被依赖的 bean 一定先创建，再创建依赖的 bean，A 依赖于 B，先创建 B，再创建 A。

```xml
<bean id="student" class="com.southwind.entity.Student" depends-on="user"></bean>

    <bean id="user" class="com.southwind.entity.User"></bean>
```



### Spring 的 p 命名空间

p 命名空间是对 IoC / DI 的简化操作，使用 p 命名空间可以更加方便的完成 bean 的配置以及 bean 之间的依赖注入。

```xml
 <bean id="student" class="com.southwind.entity.Student" p:id="1" p:name="张三" p:age="22" p:address-ref="address"></bean>

    <bean id="address" class="com.southwind.entity.Address" p:id="2" p:name="科技路"></bean>
```



Spring 的工厂方法

```xml
<!-- 配置实例工厂 bean -->
<bean id="shapeFactory" class="com.southwind.factory.InstanceCarFactory"></bean>

<!-- 赔偿实例工厂创建 Car -->
<bean id="circle" factory-bean="shapeFactroy" factory-method="getShape">
    <constructor-arg value="circle"></constructor-arg>
</bean>
```



### IoC 自动装载（Autowire）

IoC 负责创建对象，DI 负责完成对象的依赖注入，通过配置 property 标签的 ref 属性来完成，同时 Spring 提供了另外一种更加简便的依赖注入方式：自动装载，不需要手动配置 property，IoC 容器会自动选择 bean 完成注入。

自动装载有两种方式：

- byName：通过属性名自动装载
- byType：通过属性的数据类型自动装载

> byName

```xml
<bean id="cars" class="com.southwind.entity.Car">
    <property name="id" value="1"></property>
    <property name="name" value="宝马"></property>
</bean>

<bean id="person" class="com.southwind.entity.Person" autowire="byName">
    <property name="id" value="11"></property>
    <property name="name" value="张三"></property>
    <!-- 此处通过名字自动装载car，相当于ref：cars -->
</bean>
```

> byType

```xml
<bean id="car" class="com.southwind.entity.Car">
    <property name="id" value="2"></property>
    <property name="name" value="奔驰"></property>
</bean>

<bean id="person" class="com.southwind.entity.Person" autowire="byType">
    <property name="id" value="11"></property>
    <property name="name" value="张三"></property>
</bean>
```

byType 需要注意，如果同时存在两个及以上的符合条件的 bean 时，自动装载会抛出异常。



## AOP

AOP：Aspect Oriented Programming 面向切面编程。

AOP 的优点：

- 降低模块之间的耦合度。
- 使系统更容易扩展。
- 更好的代码复用。
- 非业务代码更加集中，不分散，便于统一管理。
- 业务代码更加简洁存粹，不参杂其他代码的影响。

AOP 是对面向对象编程的一个补充，在运行时，动态地将代码切入到类的指定方法、指定位置上的编程思想就是面向切面编程。将不同方法的同一个位置抽象成一个切面对象，对该切面对象进行编程就是 AOP。

![20180530172528617](C:\Users\27600\Desktop\20180530172528617.png)

| AOP                       | 概念                                                         |
| ------------------------- | ------------------------------------------------------------ |
| **Aspect**（切面）        | Aspect 声明类似于 Java 中的类声明，在 Aspect 中会包含着一些 Pointcut 以及相应的 Advice。 |
| **Joint point**（连接点） | 表示在程序中明确定义的点，典型的包括方法调用，对类成员的访问以及异常处理程序块的执行等等，它自身还可以嵌套其它 joint point。 |
| **Pointcut**（切点）      | 表示一组 joint point，这些 joint point 或是通过逻辑关系组合起来，或是通过通配、正则表达式等方式集中起来，它定义了相应的 Advice 将要发生的地方。 |
| **Advice**（动作）        | Advice 定义了在 Pointcut 里面定义的程序点具体要做的操作，它通过 before、after 和 around 来区别是在每个 joint point 之前、之后还是代替执行的代码。 |
| **Target**（目标对象）    | 织入 Advice 的目标对象.                                      |
| **Weaving**（织入）       | 将 Aspect 和其他对象连接起来, 并创建 Adviced object 的过程   |



### 例子

创建一个计算器接口 Cal，定义4个方法。

```java
public interface Cal {
    public int add(int num1,int num2);
    public int sub(int num1,int num2);
    public int mul(int num1,int num2);
    public int div(int num1,int num2);
}
```

创建接口的实现类 CalImpl。

```java
/**
* CalImpl: 目标对象（Target）
* 在方法里的所有语句都是 连接点（JointPoint）
* 要抽出来的输出语句就是 切点（PointCut）
*/
public class CalImpl implements Cal {
    public int add(int num1, int num2) {
        //连接点start
        System.out.println("add方法的参数是["+num1+","+num2+"]");
        int result = num1+num2;
        System.out.println("add方法的结果是"+result);
        return result;
        //连接点end，其它方法同理
    }

    public int sub(int num1, int num2) {
        System.out.println("sub方法的参数是["+num1+","+num2+"]");
        int result = num1-num2;
        System.out.println("sub方法的结果是"+result);
        return result;
    }

    public int mul(int num1, int num2) {
        System.out.println("mul方法的参数是["+num1+","+num2+"]");
        int result = num1*num2;
        System.out.println("mul方法的结果是"+result);
        return result;
    }

    public int div(int num1, int num2) {
        System.out.println("div方法的参数是["+num1+","+num2+"]");
        int result = num1/num2;
        System.out.println("div方法的结果是"+result);
        return result;
    }
}
```



- **用AOP优化**

```java
/**
* 这是一个切面类或者叫代理类（Aspect）：里面包含了PointCut和advice
* @Aspect表名这是一个切面类
* @Component将该类的对象注入到 IoC 容器
* @Before...等就是切点PointCut，表示方法执行的具体位置和时机。
* before() 就是advice抽出动作
*/ 
@Aspect     
@Component 
public class LoggerAspect {


    @Before(value = "execution(public int com.southwind.utils.impl.CalImpl.*(..))")
    public void before(JoinPoint joinPoint){
        //获取方法名
        String name = joinPoint.getSignature().getName();
        //获取参数
        String args = Arrays.toString(joinPoint.getArgs());
        System.out.println(name+"方法的参数是："+ args);
    }

    @After(value = "execution(public int com.southwind.utils.impl.CalImpl.*(..))")
    public void after(JoinPoint joinPoint){
        //获取方法名
        String name = joinPoint.getSignature().getName();
        System.out.println(name+"方法执行完毕");
    }

    @AfterReturning(value = "execution(public int com.southwind.utils.impl.CalImpl.*(..))",returning = "result")
    public void afterReturning(JoinPoint joinPoint,Object result){
        //获取方法名
        String name = joinPoint.getSignature().getName();
        System.out.println(name+"方法的结果是"+result);
    }

    @AfterThrowing(value = "execution(public int com.southwind.utils.impl.CalImpl.*(..))",throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint,Exception exception){
        //获取方法名
        String name = joinPoint.getSignature().getName();
        System.out.println(name+"方法抛出异常："+exception);
    }

}
```

**CalImpl 也需要添加 `@Component`，交给 IoC 容器来管理。**

```java
@Component
public class CalImpl implements Cal {
    public int add(int num1, int num2) {
        int result = num1+num2;
        return result;
    }

    public int sub(int num1, int num2) {
        int result = num1-num2;
        return result;
    }

    public int mul(int num1, int num2) {
        int result = num1*num2;
        return result;
    }

    public int div(int num1, int num2) {
        int result = num1/num2;
        return result;
    }
}
```

**spring.xml 中配置 AOP。**

```xml
   <!-- context:component-scan` 将 `com.southwind` 包中的所有类进行扫描，如果该类同时添加了 `@Component`，则将该类扫描到 IoC 容器中，即 IoC 管理它的对象。-->

   <!-- 自动扫描 -->
    <context:component-scan base-package="com.southwind"></context:component-scan>

    <!-- 是Aspect注解生效，为目标类自动生成代理对象 -->
    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>

```

| Advice类型            | 描述                                                         |
| --------------------- | ------------------------------------------------------------ |
| before advice         | 在 join point 前被执行的 advice.                             |
| after return advice   | 在一个 join point 正常返回后执行的 advice                    |
| after throwing advice | 当一个 join point 抛出异常后执行的 advice                    |
| after(final) advice   | 无论一个 join point 是正常退出还是发生了异常, 都会被执行的 advice. |
| around advice         | 在 join point 前和 joint point 退出后都执行的 advice. 这个是最常用的 advice |
| introduction          | introduction可以为原有的对象增加新的属性和方法               |

AOP技术是通过JDK动态代理实现的，而JDK动态代理对实现类对象做增强得到的增强类与实现类是兄弟关系，所以不能用实现类接收增强类对象，只能用接口接收。由于以上原因，如果将对象注入给实现类而非接口的话，在代理时就会报错。

### JDK动态代理：

> 也叫做分身代理，通过继承InvocationHandler接口的invoke方法实现代理业务，bind方法创建代理对象，代理对象用有实现类所有的方法，但不是实现类的本身，可以看作为接口的另一个实现类。

```java
public class MyInvocationHandler implements InvocationHandler {
    //接收委托对象
    private Object object = null;

    //返回代理对象
    public Object bind(Object object){
        this.object = object;
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),object.getClass().getInterfaces(),this);
    }

    //处理代理业务
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName()+"方法的参数是："+ Arrays.toString(args));
        Object result = method.invoke(this.object,args);
        System.out.println(method.getName()+"的结果是"+result);
        return result;
    }
}
```



JDK动态代理源码

```java
/**
* ClassLoader loader 类加载器：就是将class文件加载到类虚拟机中
* Class<?>... interfaces 目标类实现的所有接口
* InvocationHandler h 
*/
public static Object newProxyInstance(ClassLoader loader,          
                                          Class<?>[] interfaces,
                                          InvocationHandler h)
        throws IllegalArgumentException  
    {
        Objects.requireNonNull(h);  //检查InvocationHandler是否为空，为空抛出空指针异常

        final Class<?>[] intfs = interfaces.clone();      //克隆目标类接口
        final SecurityManager sm = System.getSecurityManager();  //进行安全校验
        if (sm != null) {
            checkProxyAccess(Reflection.getCallerClass(), loader, intfs);   //检查是否能被代理
        }

        /*
         * Look up or generate the designated proxy class.
         * 得到代理类
         */
        Class<?> cl = getProxyClass0(loader, intfs);

        /*
         * Invoke its constructor with the designated invocation handler.
         */
        try {
            if (sm != null) {
                checkNewProxyPermission(Reflection.getCallerClass(), cl);
            }
            //通过构造方法得到代理类的对象
            final Constructor<?> cons = cl.getConstructor(constructorParams);
            final InvocationHandler ih = h;
            if (!Modifier.isPublic(cl.getModifiers())) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() {
                        cons.setAccessible(true);
                        return null;
                    }
                });
            }
            return cons.newInstance(new Object[]{h});        //new得到代理对象
        } catch (IllegalAccessException|InstantiationException e) {
            throw new InternalError(e.toString(), e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new InternalError(t.toString(), t);
            }
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.toString(), e);
        }
    }
```

