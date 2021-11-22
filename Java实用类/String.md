# String
Java 通过 String 类来创建和操作字符串数据。
String 实例化
1、直接赋值                            String str = "Hello World";
2、通过构造函数创建对象     String str = new String("Hello World");

```sql
//String对象 （java库）
public class String{
  private char[] value;            //本质为字符数组
  private int offset;              //字符串数组中的偏移量
  private int count;               //字符串的长度
  private int hash;                //hash散列值
  ...
}
```

### **字符串常量池**
1. JVM为了提高性能和减少内存开销，在实例化字符串常量的时候进行了一些优化
- 为字符串开辟一个字符串常量池，类似于缓存区
- 创建字符串常量时，首先坚持字符串常量池是否存在该字符串
- 存在该字符串，返回引用实例，不存在，实例化该字符串并放入池中

2. 实现的基础
- 实现该优化的基础是因为字符串是不可变的，可以不用担心数据冲突进行共享
- 运行时实例创建的全局字符串常量池中有一个表，总是为池中每个唯一的字符串对象维护一个引用,这就意味着它们一直引用着字符串常量池中的对象，所以，在常量池中的这些字符串不会被垃圾收集器回收
```sql
  String str1 = “hello”;
  String str2 = “hello”;
  
  System.out.printl（"str1 == str2" : str1 == str2 ) //true 
```

![Image](E:\学习/medley/resources/BJbKCBykUt_ry52O1J8F.png)

**1. 堆**
- 存储的是对象，每个对象都包含一个与之对应的class
- JVM只有一个堆区(heap)被所有线程共享，堆中不存放基本类型和对象引用，只存放对象本身
- 对象的由垃圾回收器负责回收，因此大小和生命周期不需要确定

**2. 栈**
- 每个线程包含一个栈区，栈中只保存基础数据类型的对象和自定义对象的引用(不是对象)
- 每个栈中的数据(原始类型和对象引用)都是私有的
- 栈分为3个部分：基本类型变量区、执行环境上下文、操作指令区(存放操作指令)
- 数据大小和生命周期是可以确定的，当没有引用指向数据时，这个数据就会自动消失

**3. 方法区**
- 静态区，跟堆一样，被所有的线程共享
- 方法区中包含的都是在整个程序中永远唯一的元素，如class，static变量
- 字符串常量池则存在于方法区
```sql
String str1 = “abc”;
String str2 = “abc”;
String str3 = “abc”;
String str4 = new String(“abc”);
String str5 = new String(“abc”);
```


### **String 常⽤⽅法**
```sql
public String()               //创建⼀个空的字符串对象
public String(String value)   //创建⼀个值为 value 的字符串对象
public String(char value[])   //将⼀个char数组转换为字符串

//将⼀个指定范围的char数组转为字符串对象
public String(char value[],int offset, int count) 
public String(byte value[])    //将⼀个byte数组转换为字符串对象

//将⼀个指定范围的byte数组转为字符串对象
public String(byte value[],int offset, int count)
public int length()            //获取字符串的⻓度
public boolean isEmpty()       //判断字符串是否为空
public char charAt(int index)  //返回指定下标的字符
public byte[] getBytes()       //返回字符串对应的byte数组
public boolean equals(Object anObject)  //判断两个字符串值是否相等

//判断两个字符串值是否相等（忽略⼤⼩写）
public boolean equalsIgnoreCase(Object anObject)

public int compareTo(String value)   //对字符串进⾏排序
public int compareToIgnoreCase(String value)  //忽略⼤⼩写进⾏排序
public boolean startsWith(String value)  //判断字符串是否以 value开头
public boolean endsWith(String value)    //判断字符串是否以 value结尾
public int hashCode()                 //返回字符串的 hash 值
public int indexOf(String str)        //返回 str 在字符串中的下标
public int indexOf(String str,int formIndex)  //从指定位置查找字符串的下标
public String subString(int beginIndex)  //从指定位置开始截取字符串
public String subString(int beginIndex,int endIndex)  //截取指定区间的字符串
public String concat(String str)      //追加字符串
public String replaceAll(String o,String n)  //将字符串中所有的 o 替换成 n
public String[] split(String regex)   //⽤指定的字符串对⽬标进⾏分割，返回数组
public String toLowerCase()           //转⼩写
public String toUpperCase()           //转⼤写
public char[] toCharArray()           //将字符串转为字符数组
```

# split()用法
1、如果用“.”作为分隔的话,必须是如下写法,String.split("\\."),这样才能正确的分隔开,不能用String.split(".");
2、如果用“|”作为分隔的话,必须是如下写法,String.split("\\|"),这样才能正确的分隔开,不能用String.split("|");
3.String.split()就是按照特定的字符将字符串转换成数组。
```java
//输入时间计算具体秒数
//hh:mm:ss  h*360+m*60*s
class Solution {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String duration = in.next();      
        String[] arr = duration.split(":");     
        int h = Integer.parseInt(arr[0]);
        int m = Integer.parseInt(arr[1]);
        int s = Integer.parseInt(arr[2]);
        System.out.print(i*360+x*60+s);
    }
}
```






