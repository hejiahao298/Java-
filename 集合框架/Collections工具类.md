# Collections工具类
- Collection 接⼝，List 和 Set 的⽗接⼝。
- Collections 不是接⼝，它是⼀个⼯具类，专⻔提供了⼀些对集合的操作。
- Colletions 针对集合的⼯具类，Collection
- Arrays 针对数组的⼯具类，Array
```sql
//对集合进⾏排序
public static sort()    
 
//查找 v 在 list 中的位置，集合必须是⽣序排列
public static int binarySearch(List list,Object v)

//返回 list 中 index 位置的值
public static get(List list,int index)   

//对 list 进⾏反序输出
public static void reverse(List list) 

//交换集合中指定位置的两个元素
public static void swap(List list,int i,int j)

//将集合中所有元素替换成 obj
public static void fill(List list,Object obj)

//返回集合中的最⼩值
public static Object min(List list) 

//返回集合中的最⼤值
public static Object max(List list) 

//在 list 集合中⽤ new 替换 old
public static boolean replaceAll(List list,Object old,Object new)

//向集合中添加元素
public static boolean addAll(List list,Object... obj) 
```
