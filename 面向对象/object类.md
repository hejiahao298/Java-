# object类
### **Object**
Object 是 Java 官⽅提供的类，存放在 java.lang 包中，该类是所有类的直接⽗类或者间接⽗类，⽆论是
Java 提供的类还是开发者⾃定义的类，都是 Object 的直接⼦类或间接⼦类，Java 中的任何⼀个类都会
继承 Object 中的 public 和 protected ⽅法。
```java
hashCode();
getClass();
equals(null);
clone();
toString();
notify();
notifyAll();
wait();
wait(1000L);
wait(1000L, 100);
```



**Object 类中经常被⼦类重写的⽅法：**
1、public String toString() 以字符串的形式返回对象的信息
2、public boolean equals(Object obj) 判断两个对象是否相等
3、public native int hashCode() 返回对象的散列码

- toString
Object
```java
public String toString() {
 return getClass().getName() + "@" + Integer.toHexString(hashCode());
}
```
重写之后

```java
public String toString() {
 return "People [id=" + id + ", name=" + name + ", score=" + score + "]";
}
```

- equals
object
```java
public boolean equals(Object obj) {
 return (this == obj);
}
```
重写之后
```java
public boolean equals(Object obj) {
 // TODO Auto-generated method stub
 People people = (People)obj;
 if(this.id == people.id && this.name.equals(people.name) &&
this.score.equals(people.score)) {
 return true;
 }
 return false;
}
```

- hashCode
object
```java
public native int hashCode();
```
重新之后
```java
public int hashCode() {
 // TODO Auto-generated method stub
 return (int) (id*name.hashCode()*score);
}
```
