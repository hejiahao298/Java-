Linked
hash
Tree

set和list区别
```java
 Set 接口实例存储的是无序的，不重复的数据。List 接口实例存储的是有序的，可以重复的元素。

2. Set检索效率低下，删除和插入效率高，插入和删除不会引起元素位置改变 <实现类有HashSet,TreeSet>。

3. List和数组类似，可以动态增长，根据实际存储的数据的长度自动增长List的长度。查找元素效率高，插入删除效率低，因为会引起其他元素位置改变 <实现类有ArrayList,LinkedList,Vector> 。
```

HashSet
是开发中经常使⽤的⼀个实现类，存储⼀组⽆序且唯⼀的对象。
⽆序：元素的存储顺序和遍历顺序不⼀致

LinkedHashSet
是 Set 的另外⼀个实现类，可以存储⼀组有序且唯⼀的元素.
有序：元素的存储顺序和遍历顺序⼀致。

TreeSet implement NavigableSet<E> extends SortedSet<E>
LinkedHashSet 和 TreeSet 都是存储⼀组有序且唯⼀的数据，但是
这⾥的两个有序是有区别的。
LinkedHashSet 的有序是指元素的存储顺序和遍历顺序是⼀致的。
6,3,4,5,1,2-->6,3,4,5,1,2
TreeSet 的有序是指集合内部会⾃动对所有的元素按照升序进⾏排
列，⽆论存⼊的顺序是什么，遍历的时候⼀定按照⽣序输出。

HashMap

TreeMap
相对于HashMap为线程安全，用法基本一样

Hashtable

