# 重入锁  ReentrantLock
JUC 勾优C
java.util.concurrent
Java 并发编程⼯具包，Java 官⽅提供的⼀套专⻔⽤来处理并发编程的⼯具集合（接⼝+类）
并发：单核 CPU，多个线程“同时”运⾏，实际是交替执⾏，只不过速度太快，看起来是同时执⾏。
两个厨师⼀⼝锅
并⾏：多核 CPU，真正的多个线程同时运⾏。
两个厨师两⼝锅
重⼊锁是 JUC 使⽤频率⾮常⾼的⼀个类 ReentrantLock
- ReentrantLock 就是对 synchronized 的升级，⽬的也是为了实现线程同步。
- ReentrantLock 是⼀个类，synchronized 是⼀个关键字。
- ReentrantLock 是 JDK 实现，synchronized 是 JVM 实现。
- synchronized 可以⾃动释放锁，ReentrantLock 需要⼿动释放。
- ReentrantLock 是 Lock 接⼝的实现类。
公平锁和⾮公平锁的区别
公平锁：线程同步时，多个线程排队，依次执⾏
⾮公平锁：线程同步时，可以插队
线程的实现有两种⽅式
继承 Thread
实现 Runnable
实现 Runnable 的耦合度更低

- lambel表达式
![](https://raw.githubusercontent.com/hejiahao298/Myimg/master/ReentranLock.png)

```java
public class Test {
    public static void main(String[] args) {
        Account account = new Account();
        new Thread(()->{
            account.count();
        },"A") .start();
        new Thread(()->{
            account.count();
        },"B") .start();
    }
}
// 将资源和 Runnable 进⾏解耦合
class Account{
    private static int num;
    public void count() {
        num++;
    }
}
```


### **ReentrantLock**
```java
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
public class Test3 {
    public static void main(String[] args) {
        Account3 account = new Account3();
        new Thread(()->{
            account.count();
        },"A").start();
        new Thread(()->{
            account.count();
        },"B").start();
    }
}
class Account3{
    private static int num;
    private ReentrantLock reentrantLock = new ReentrantLock();

    public void count() {
        //上锁
        reentrantLock.lock();
        reentrantLock.lock();
        num++;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"是当前的第"+num+"位访客");
                //解锁
        reentrantLock.unlock();
        reentrantLock.unlock();
    }
}
```

- Lock 上锁和解锁都需要开发者⼿动完成。
- 可以重复上锁，上⼏把锁就需要解⼏把锁。
ReentrantLock 除了可以重⼊之外，还有⼀个可以中断的特点，可中断是指某个线程在等待获取锁的过
程中可以主动过终⽌线程。
reentrantLock.lockInterruptibly();       //可中断锁
Thread t2 =new Thread()
t2.interrupt();              //如果一秒后t2没有获取cpu支援就中断线程
```java
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
public class Test5 {
    public static void main(String[] args) {
        StopLock stopLock = new StopLock();
        Thread t1 = new Thread(()->{
            stopLock.service();
        },"A");
        Thread t2 =new Thread(()->{
            stopLock.service();
        },"B");
        t1.start();
        t2.start();
        try {
            TimeUnit.SECONDS.sleep(1);      
            t2.interrupt();              //如果一秒后t2没有获取cpu支援就中断线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class StopLock{
    private ReentrantLock reentrantLock = new ReentrantLock();
    public void service() {
        try {
            reentrantLock.lockInterruptibly();       //可中断锁
            System.out.println(Thread.currentThread().getName() + "get lock");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }
```
