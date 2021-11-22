# Math
Math 类为开发者提供了⼀系列的数学⽅法，同时还提供了两个静态常量 E（⾃然对数的底数）和 PI（圆周率）。
```sql
public class Test {
 public static void main(String[] args) {
 System.out.println("常量E"+Math.E);
 System.out.println("常量PI"+Math.PI);
 System.out.println("9的平⽅根"+Math.sqrt(9));
 System.out.println("8的⽴⽅根"+Math.cbrt(8));
 System.out.println("2的3次⽅"+Math.pow(2,3));
 System.out.println("较⼤值"+Math.max(6.5,1));
 System.out.println("-10.3的绝对值"+Math.abs(-10.3));
 System.out.println(Math.ceil(10.000001));
 System.out.println(Math.floor(10.999999));
 System.out.println((int)(Math.random()*10));
 System.out.println(Math.rint(5.4));
 }
}
```

# int 取整
- 向上取整用Math.ceil(double a)
- 向下取整用Math.floor(double a)
```java
//小明在森林里遇到了老虎，森林的长度是l，小明的速度是s厘米每秒，但小明每点燃一根
//火柴就可以拖延5秒，因为老虎害怕明火。问最少需要多少根火柴才能走出森林
class test {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int l = in.nextInt();
        int s = in.nextInt();

        int z= l*100/s;
        int count = z/5;
        Math.ceil(count);  //向上取整；
        System.out.println(count);
    }
    }
```


