# Random
⽤来产⽣随机数的类，并且可以任意指定⼀个区间，在此区间范围内产⽣⼀个随机数。
```sql
public Random()               //创建⼀个⽆参的随机数构造器，使⽤系统时间作为默认种⼦
public Random(long seed)      //使⽤ long 数据类型的种⼦创建⼀个随机数构造器
public boolean nextBoolean()  //返回⼀个 boolean 类型的随机数
public double nextDouble()    //返回⼀个 double 类型的随机数，0.0 -1.0 之间
public float nextFloat()      //返回⼀个 float 类型的随机数，0.0 - 1.0之间
public int nextInt()          //返回⼀个 int 类型的随机数
public int nextInt(n)         //返回⼀个 int 类型的随机数，0-n之间
public long nextLong          //返回⼀个 long 类型的随机数，0-1 之间
```

```sql
public class Test {
 public static void main(String[] args) {
 Random random = new Random();
 //⽣成订单编号（时间戳+随机数）
 for (int i = 1; i <= 10000; i++) {
 //随机⽣成⼀个六位数
 System.out.println("订单"+i+"的编号是："+System.currentTimeMillis()+random.nextInt(10000
0)+100000);
 }
 }
 }

```
