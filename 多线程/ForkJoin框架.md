# ForkJoin框架
ForkJoin 是 JDK 1.7 后发布的多线程并发处理框架，功能上和 JUC 类似，JUC 更多时候是使⽤单个类完
成操作，ForkJoin 使⽤多个类同时完成某项⼯作，处理上⽐ JUC 更加丰富，实际开发中使⽤的场景并不
是很多，互联⽹公司真正有⾼并发需求的情况才会使⽤，⾯试时候会加分. 

本质上是对线程池的⼀种的补充，对线程池功能的⼀种扩展，基于线程池的，它的核⼼思想就是将⼀个
⼤型的任务拆分成很多个⼩任务，分别执⾏，最终将⼩任务的结果进⾏汇总，⽣成最终的结果。

![](https://raw.githubusercontent.com/hejiahao298/Myimg/master/ForkJoin1.png)

本质就是把⼀个线程的任务拆分成多个⼩任务，然后由多个线程并发执⾏，最终将结果进⾏汇总。
**工作窃取**
⽐如 A B 两个线程同时还执⾏，A 的任务⽐较多，B 的任务相对较少，B 先执⾏完毕，这时候 B 去帮助
A 完成任务（将 A 的⼀部分任务拿过来替 A 执⾏，执⾏完毕之后再把结果进⾏汇总），从⽽提⾼效率。

ForkJoin 框架，核⼼是两个类
- ForkJoinTask （描述任务）
- ForkJoinPool（线程池）提供多线程并发⼯作窃取
使⽤ ForkJoinTask 最重要的就是要搞清楚如何拆分任务，这⾥⽤的是递归思想。
1、需要创建⼀个 ForkJoinTask 任务，ForkJoinTask 是⼀个抽象类，不能直接创建 ForkJoinTask 的实例
化对象，开发者需要⾃定义⼀个类，继承 ForkJoinTask 的⼦类 RecursiveTask ，Recursive 就是递归的
意思，该类就提供了实现递归的功能。

![](https://raw.githubusercontent.com/hejiahao298/Myimg/master/ForkJoin2.png)

```java
/**
 * 10亿求和
 */
public class ForkJoinDemo extends RecursiveTask<Long> {
    private Long start;
    private Long end;
    private Long temp = 100_0000L;
    public ForkJoinDemo(Long start, Long end) {
        this.start = start;
        this.end = end;
    }
    @Override
    protected Long compute() {
        if((end-start)<temp){
            Long sum = 0L;
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        }else{
            Long avg = (start+end)/2;
            ForkJoinDemo task1 = new ForkJoinDemo(start,avg);
            task1.fork();
            ForkJoinDemo task2 = new ForkJoinDemo(avg,end);
            task2.fork();
            return task1.join()+task2.join();
        }
    }
}
---------------------------------------------------------------------------------------
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
public class Test {
    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinDemo(0L,10_0000_0000L);
        forkJoinPool.execute(task);
        Long sum = 0L;
        try {
            sum = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Long endTime = System.currentTimeMillis();
        System.out.println(sum+"，供耗时"+(endTime-startTime));
    }
}
```
