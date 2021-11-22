# StringBuffer
- String 对象⼀旦创建，值不能修改（原来的值不能修改，⼀旦修改就是⼀个新的对象，只要⼀改动，就会创建⼀个新的对象）
- 修改之后会重新开辟内存空间来存储新的对象，会修改 String 的引⽤。
- 因为 String 底层是⽤数组来存值的，数组⻓度⼀旦创建就不可修改，所以导致上述问题。
- StringBuffer 可以解决 String 频繁修改造成的空间资源浪费的问题。
- StringBuffer 底层也是使⽤数组来存值。
- StringBuffer 数组的默认⻓度为 16，使⽤⽆参构造函数来创建对象。

```sql
public final class StringBuffer
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence
{
   public StringBuffer() {
          super(16);
      }
    
    public StringBuffer(String str) {
        super(str.length() + 16);
        append(str);
    }
    
    //线程安全的
    public synchronized int length() {
        return count;
    }

    public synchronized int capacity() {
        return value.length;
    }
}
```

- StringBuffer ⼀旦创建，默认会有 16 个字节的空间去修改，但是⼀旦追加的字符串⻓度超过 16，如何处理？
- StringBuffer 不会重新开辟⼀块新的内存区域，⽽是在原有的基础上进⾏扩容，通过调⽤⽗类 **ensureCapacityInternal()** ⽅法对底层数组进⾏扩容，保持引⽤不变。
**其它用法基本和String一致**
