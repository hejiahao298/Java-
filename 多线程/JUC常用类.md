# JUC常用类
1. **Volatile**
是 JVM 提供的轻量级同步机制，可⻅性，主内存对象线程可⻅。
⼀个线程执⾏完任务之后还，会把变量存回到主内存中，并且从主内存中读取当前最新的值，如果是⼀个空的任务，则不会重新读取主内存中的值。

![](https://raw.githubusercontent.com/hejiahao298/Myimg/master/JUC.png)

```java
import java.util.concurrent.TimeUnit;
public class Test {
    private volatile static int num = 0;
    public static void main(String[] args) {
        new Thread(()->{
            while(num == 0){
                System.out.println("---Thread---");
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        num = 1;
        System.out.println(num);
    }
}
```

**2.  Synchronized**
**3.  AtomicXXX**

**4.  Reentrantlock**
可以替代Synchronized，必须手动释放锁。而且可以指定公平锁。
```java
public class TestReentrantLock {
    // true为公平锁，false为非公平锁，默认为false
    private static ReentrantLock lock = new ReentrantLock(true);

    void run() {
        for (int i = 0; i < 100; i++) {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "---");
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        TestReentrantLock t = new TestReentrantLock();
        Thread t1 = new Thread(t::run);
        Thread t2 = new Thread(t::run);
        t1.start();
        t2.start();
    }
}
```

**5.CountDownLatch ：减法计数器**
java1.5被引入，使一个线程等待其他线程各自执行完毕后再执行。
可以⽤来倒计时，当两个线程同时执⾏时，如果要确保⼀个线程优先执⾏，可以使⽤计数器，当计数器
清零的时候，再让另⼀个线程执⾏。
```java
public class TestCountdownLatch {
    public static void main(String[] args) {
        usingCountDownLatch();
    }

    private static void usingCountDownLatch() {
        Thread[] threads = new Thread[100];
        CountDownLatch latch = new CountDownLatch(threads.length);
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                int result = 0;
                for (int j = 0; j < 10000; j++) result += j;
                latch.countDown();
            });
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end latch");
    }
}
```

**6.CyclicBarrier  ：加法计数器**
await()：在其他线程中试图唤醒计数器线程，当其他线程的执⾏次数达到计数器的临界值时，则唤醒计
数器线程，并且计数器是可以重复使⽤的，当计数器的线程执⾏完成⼀次之后，计数器⾃动清零，等待
下⼀次执⾏。
```java
public class TestCyclicBarrier {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(20, () -> {
            System.out.println(Thread.currentThread().getName() + ":完成最后任务");
        });

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "到达");
                    Thread.sleep(100);
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

**7.Phaser**

**8.ReadWriterLock**
JUC包主要是 ReentrantReadWriteLock。提供 readLock 和 writeLock 方法。

ReetrantReadWriteLock读写锁的实现中，读锁使用共享模式；写锁使用独占模式，换句话说，读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的
ReetrantReadWriteLock读写锁的实现中，需要注意的，当有读锁时，写锁就不能获得；而当有写锁时，除了获得写锁的这个线程可以获得读锁外，其他线程不能获得读锁
读写锁的实现必须确保写操作对读操作的内存影响。换句话说，一个获得了读锁的线程必须能看到前一个释放的写锁所更新的内容，读写锁之间为互斥
ReentrantReadWriteLock支持锁降级，不支持锁升级（同一个线程中，在没有释放读锁的情况下，就去申请写锁）。
————————————————
版权声明：本文为CSDN博主「怀瑾握瑜1117」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/raulism1117/article/details/105216213
```java
public class TestReadWriteLock {
    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    static Lock readLock = lock.readLock();
    static Lock writeLock = lock.writeLock();

    void read() {
        readLock.lock();
        if (!lock.isWriteLocked()) {
            System.out.println(Thread.currentThread().getName() + "获取到读锁");
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "正在读...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        readLock.unlock();
        System.out.println(Thread.currentThread().getName() + "释放读锁");
    }

    void write() {
        writeLock.lock();
        if (lock.isWriteLocked()) {
            System.out.println(Thread.currentThread().getName() + "获取到写锁");
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "正在写...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writeLock.unlock();
        System.out.println(Thread.currentThread().getName() + " 释放写锁");
    }

    public static void main(String[] args) {
        TestReadWriteLock t = new TestReadWriteLock();
        for (int i = 0; i < 3; i++) {
            new Thread(t::read).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(t::write).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(t::read).start();
        }
    }
}
```

**9.Semaphore：计数信号量**
实际开发中主要使⽤它来完成限流操作，限制可以访问某些资源的线程数量。
Semaphore 只有 3 个操作：
- 初始化
- 获取许可
- 释放
```java
public class SemaphoreTest {
    public static void main(String[] args) {
        //初始化
        Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < 15; i++) {
            new Thread(()->{
                //获得许可
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"进店购物");
                            TimeUnit.SECONDS.sleep(5);
                    System.out.println(Thread.currentThread().getName()+"出店");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    //释放
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
```

**10.Exchanger**
Exchanger 是 JDK 1.5 开始提供的一个用于两个工作线程之间交换数据的封装工具类，简单说就是一个线程在完成一定的事务后想与另一个线程交换数据，则第一个先拿出数据的线程会一直等待第二个线程，直到第二个线程拿着数据到来时才能彼此交换对应数据。
```java
public class TestExchanger {
    static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            String t = "t1";
            try {
                t = exchanger.exchange(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "-" + t);
        });
        Thread t2 = new Thread(() -> {
            String t = "t2";
            try {
                t = exchanger.exchange(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "-" + t);
        });
        t1.start();
        t2.start();
    }
}
```

**11.LockSupport**
主要有park()和unpark()方法。park()阻塞当前线程，unpark()恢复当前线程。

与wait和notify的主要区别表现在 1.不需要获取某个对象的锁。2.没有前后顺序的区别
```java
public class TestLockSupport {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                if (i == 5) {
                    LockSupport.park();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        try {
            // 保证先park后unpark
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(t1);
    }
}

```
