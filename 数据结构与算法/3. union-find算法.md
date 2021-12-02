# union-find算法

问题：输入一列整数对，其中每个整数都表示一个某种类型的对象，一对整数p,q可以理解为“p和q是相连的”。

**如果相连表示为等价关系，也就意味着它具有**：
- 自反性：p和p是相连的；
- 对称性：p和q是相连的，那么q和p也是相连的
- 传递性：p和q是相连的，q和r是相连的，那么p和r也是相连的

**union-find算法的API**
```java
public class UF{
  UF(int N)                       //初始化N个点
  void union(int p,int q)         //在p和q之间添加一条连接
  int find(int p)                 //p所在的分量标识符
  boolean connected(int p,int q)  //如果p和q在同一分量中则返回true
  int count()                     //连通分量的数量
}
```
基于此数据结构实现高效的union(),  find(), connect(), count() 方法

算法1、quick-find的实现
```java
public class UnionFind {
        private int[] id;    //分量id(以触点为索引)
        private int count;   //分量数量

    //初始化分量id数组
    public UnionFind(int N){
        count = N;
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    //连通分量数量
    public int count(){
        return count;
    }

    //判断是否在同一连通量
    public boolean connected(int p,int q){
        return find(p)==find(q);
    }

    //返回分量的标识符
    public int find(int p){
        return id[p];
    }

    //将p和q归并到同一连通分量
    public void union(int p,int q){
        int pID = find(p);
        int qID = find(q);
        if(pID == qID) return;

        //将p的分量重命名为q的名称
        for (int i = 0; i < id.length; i++) {
            if(id[i] == pID) id[i] = qID;
            count--;
        }
    }

    public static void main(String[] args) {
        int N = StdIn.readInt();
        UnionFind uf = new UnionFind(N);
        while (!StdIn.isEmpty()){
            int p = StdIn.readInt();
            int q = StdIn.readInt();

            if(uf.connected(p,q)) continue;
            uf.union(p,q);
            System.out.println(p+" "+q);
        }
        System.out.println(uf.count + "components");
    }
}
```

算法2、quick-union算法
该算法重点提升了union()方法的速度，和quick-find算法是互补的，它们基于相同的数据结构--以触点作为索引的id[]数组。
每个触点对应的id[]元素都是同一个分量中的另一个触点的名称(也可能是他自己)--我们将这种联系称为链接。在实现find()算法时，我们从给定的触点开始，由它链接到另一个触点，再由这个触点链接到下一个触点，直到到达根触点，即链接指向自己。
```java
//返回分量的标识符
    public int find(int p){
        while (p != id[p]) p = id[p];
        return p;
    }
    
    //将p和q归并到同一连通分量
    public void union(int p,int q){
        int pRoot = find(p);
        int qRoot = find(q);
        if(pRoot == qRoot) return;

        id[pRoot] = qRoot;
        count--;
    }
```



