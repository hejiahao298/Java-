Java 中线程的使⽤
### 
### **一.继承 Thread 类**
1、创建⾃定义类并继承 Thread 类。
2、重写 Thread 类中的 run ⽅法，并编写该线程的业务逻辑代码

```java
public class MyThread extends Thread {
 
 public void run() {
 //定义业务逻辑
    for(int i = 0;i<10;i++) {
         System.out.println("-------------MyThread");
 }
 }
}
```

3.使用
```java
public class Test {
 public static void main(String[] args) {
 //开启两个⼦线程
 MyThread thread1 = new MyThread();
 MyThread2 thread2 = new MyThread2();
 thread1.start();
 thread2.start()
 }
 }
```

### 二.实现 Runnable 接⼝
1、创建⾃定义类并实现 Runnable 接⼝。
2、实现 run ⽅法，编写该线程的业务逻辑代码

```java
public class MyRunnable implements Runnable {
 @Override
 public void run() {
 // TODO Auto-generated method stub
 for(int i=0;i<1000;i++) {
 System.out.println("========MyRunnable=======");
 }
 }
 }

```

3.使用
```java
MyRunnable runnable = new MyRunnable();
Thread thread = new Thread(runnable);
thread.start();
MyRunnable2 runnable2 = new MyRunnable2();
Thread thread2 = new Thread(runnable2);
thread2.start()
```

### 两种⽅式的区别：
1、MyThread，继承 Thread 类的⽅式，直接在类中重写 run ⽅法，使⽤的时候，直接实例化
MyThread，start 即可，因为 Thread 内部存在 Runnable。
2、MyRunnbale，实现 Runnable 接⼝的⽅法，在实现类中重写 run ⽅法，使⽤的时候，需要先创建
Thread 对象，并将 MyRunnable 注⼊到 Thread 中，Thread.start