### **线程的同步  synchronized关键字**
Java 中允许多线程并⾏访问，同⼀时间段内多个线程同时完成各⾃的操作。
多个线程同时操作同⼀个共享数据时，可能会导致数据不准确的问题。
使⽤线程同步可以解决上述问题。
可以通过 synchronized 关键字修饰⽅法实现线程同步，每个 Java 对象都有⼀个内置锁，内置锁会保护
使⽤ synchronized 关键字修饰的⽅法，要调⽤该⽅法就必须先获得锁，否则就处于阻塞状态。
- ⾮线程同步
```java
package com.southwind.test;
public class Account implements Runnable {
 private static int num;
 public void run() {
 num++;    //1.num++操作
 try {
 Thread.currentThread().sleep(1000);   //2.休眠1毫秒
 } catch (InterruptedException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }
 //3.打印输出
 System.out.println(Thread.currentThread().getName()+"是当前的第"+num+"位访
问");
 }
}
```
- 线程同步
```java
package com.southwind.test;
public class Account implements Runnable {
private static int num;

 public synchronized void run() {
 //1.num++操作
 num++;
 //2.休眠1毫秒
 try {
 Thread.currentThread().sleep(1000);
 } catch (InterruptedException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }
 //3.打印输出
 System.out.println(Thread.currentThread().getName()+"是当前的第"+num+"位访
问");
 } 
}
--------------------------------------------------------------------------------------
package com.southwind.test;
public class Test {
 public static void main(String[] args) {
 Account account = new Account();
 Thread t1 = new Thread(account,"张三");
 Thread t2 = new Thread(account,"李四");
 t1.start();
 t2.start();
 }
}
```
synchronized 关键字可以修饰实例⽅法，也可以修饰静态⽅法，两者在使⽤的时候是有区别的。
```java
 public synchronized static void test() {....}
 public synchronized void test() {....}
```
给实例⽅法（⾮静态⽅法）添加 synchronized 关键字并不能实现线程同步。
线程同步的本质是锁定多个线程所共享的资源，synchronized 还可以修饰代码块，会为代码块加上内
置锁，从⽽实现同步。
```java
synchronized (SynchronizedTest3.class) {
 System.out.println("start...");
 try {
 Thread.currentThread().sleep(1000);
 } catch (InterruptedException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }
 System.out.println("end...");
 }
```
如何判断线程同步或是不同步？
找到关键点：锁定的资源在内存中是⼀份还是多份？⼀份⼤家需要排队，线程同步，多份（⼀⼈⼀
份），线程不同步。
⽆论是锁定⽅法还是锁定对象，锁定类，只需要分析这个⽅法、对象、类在内存中有⼏份即可。
对象⼀般都是多份
类⼀定是⼀份
⽅法就看是静态⽅法还是⾮静态⽅法，静态⽅法⼀定是⼀份，⾮静态⽅法⼀般是多份。

### **Tips**
```java
class Account{
    private static Integer num = 0;
    private static Integer id = 0;
    public void count() {
        synchronized (num) {
            num++;
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"是当前的第"+num+"位访客");
        }
    }
}
```
如果锁定 num 不能同步，锁定 id 可以同步，原因是什么？
synchronized 必须锁定唯⼀的元素才可以实现同步
num 的值每次都在变，所以 num 所指向的引⽤⼀直在变，所以不是唯⼀的元素，肯定⽆法实现同步。
id 的值永远不变，所以是唯⼀的元素，可以实现同步。