# MyBatis

Maven依赖

```xml
<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>3.4.5</version>
</dependency>
```

> 每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的
>
> 通过 SqlSessionFactoryBuilder 获得

```java
String resource = "../../mybatis-config.xml";
InputStream inputStream = Reasources.getResouceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);         //获取到SqlSessionFactory实例
SqlSession sqlSession = sqlSessionFactory.openSession();  //获取到sqlSession实例
Account account = new Account(1L,"张三","123123",22);
//第一种方法，使用原生接口sqlSesession
//String statement = "com.hjh.mapper.AccoutMapper.save";
//sqlSession.insert(statement,account);            //调用insert标签里id为save的方法

//第二种方法，通过过 Mapper 代理实现⾃定义接⼝,推荐使用
AcountRepository acountRepository = sqlSession.getMapper(AcountRepository.class)//通过AcountRepository接口，得到代理类
acountRepository.insert(acount);

sqlSession.commit()                                //提交事务
```



mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <environments default="development">
    <environment id="development">
      <transactionManager type="JDBC"/>
      <dataSource type="POOLED">
        <property name="driver" value="${driver}"/>
        <property name="url" value="${url}"/>
        <property name="username" value="${username}"/>
        <property name="password" value="${password}"/>
      </dataSource>
    </environment>
  </environments>
  <mappers>
    <mapper resource="com/hjh/example/readerRepository.xml"/>
  </mappers>
</configuration>
```



## Mapper.xml详解

> 基本的增删改查

```xml
StudentRepository.xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.hjh.repository.AcountRepository">
    <!-- 通过id查找account-->
    <select id="findById" parameterType="long" resultType="com.southwind.entity.Account">
        select * from account where id=#{id}  
    </select>
    
    <!-- 通过id插入account-->
    <insert id="save" parameterType="com.southwind.entity.Account">
        insert into account(username,password,age) values(#{username},#{password},#{age})
    </insert>
    
    <!-- 通过id删除account-->
    <delete id="deleteById" parameterType="int">
       delete from account where id=#{id};
    </delete>
    
    <!-- 通过id更改account-->
    <update id="updateNameById" paremeterType="int">
       update acount set name = '张三' where id =#{id};
    </update>

</mapper>
```

- namespace：命名空间，规范改mapper.xml的位置，使其标签id是唯一的
- parameterType：形参类型规范，使其sql语句知道参数的类型
- resultType：返回类型，sql语句执行完后返回的类型
- 标签：select，insert，delete，update；



## parameterType详解

```xml
<!-- 基本数据类型 -->
<select id="findById" parameterType="long" resultType="com.southwind.entity.Account">
        select * from account where id=#{id}  
</select>

<!-- 包装类 -->
<select id="findById" parameterType="java.lang.Long" resultType="com.southwind.entity.Account">
        select * from account where id=#{id}  
</select>

<!-- String类型 -->
<select id="findByName" parameterType="java.lang.String" resultType="com.southwind.entity.Account">
        select * from account where name=#{name}  
</select>

<!-- 实体类型 -->
<insert id="save" parameterType="com.southwind.entity.Account">
       insert into account(username,password,age) values(#{username},#{password},#{age})
</insert>

<!-- 多个参数类型-->
<select id="findByNameAndAge" resultType="com.southwind.entity.Account">
      select * from t_account where username = #{arg0} and age = #{arg1}
</select>
```



## resultType详解

```xml
<!-- 基本数据类型 -->
<select id="count" resultType="java.lang.int">
    select count(id) from account
</select>

<!-- 包装类 -->
<select id="count" resultType="java.lang.Interacting">
    select count(id) from account
</select>

<!-- String类 -->
<select id="findNameById" resultType="java.lang.String">
    select name from account where id=#{id}
</select>

<!-- 实体类型 ：sql语句查询出来的字段和entity实体类的属性一一比对，名相同则赋值-->
<select id="findById" parameterType="java.lang.Long" resultType="com.southwind.entity.Account">
        select * from account where id=#{id}  
</select>
```



## 及联查询

### `多对一查询`

> 通过学生的id查找学生的基本信息和所在班级的信息

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.southwind.repository.StudentRepository">
    <resultMap id="studentMap" type="com.southwind.entity.Student">
       <id column="id" property="id"></id>
       <result column="name" property="name"></result>
       <association property="classes" javaType="com.southwind.entity.Classes">
          <id column="cid" property="id"></id>
          <result column="cname" property="name"></result>
       </association>
    </resultMap>
    <select id="findById" parameterType="long" resultMap="studentMap">
        select s.id,s.name,c.id as cid,c.name as cname 
        from student s,classes c 
        where s.id = #{id} and s.cid = c.id
    </select>
</mapper>
```

- id标签：主键， column 为查询出来的字段名，property为实体属性名
- result：为普通返回值
- association：级联的实体类，javaType对应的实体类



### `一对多查询`

> 通过班级的id查找班级的基本信息，和班级内所包含的所有学生信息

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.southwind.repository.ClassesRepository">
    <resultMap id="classesMap" type="com.southwind.entity.Classes">
       <id column="cid" property="id"></id>
       <result column="cname" property="name"></result>
       <collection property="students" ofType="com.southwind.entity.Student">
          <id column="id" property="id"/>
          <result column="name" property="name"/>
       </collection>
    </resultMap>
 <select id="findById" parameterType="long" resultMap="classesMap">
    select s.id,s.name,c.id as cid,c.name as cname 
     from student s,classes 
     where c.id = #{id} and s.cid = c.id
 </select>
</mapper>
```

- collection标签：级联查询一对多，收到的集合，ofType：理解为集合的泛型



### `多对多查询`

> 多对多查询本质上就是两个互相一对多，所以只需要写两个一对多就行
>
> 实例：商品和顾客互相一对多。

- 通过顾客的id查找他所购买了哪些商品

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.southwind.repository.CustomerRepository">
    <resultMap id="customerMap" type="com.southwind.entity.Customer">
       <id column="cid" property="id"></id>
       <result column="cname" property="name"></result>
       <collection property="goods" ofType="com.southwind.entity.Goods">
          <id column="gid" property="id"/>
          <result column="gname" property="name"/>
       </collection>
    </resultMap>
 <select id="findById" parameterType="long" resultMap="customerMap">
    select c.id cid,c.name cname,g.id gid,g.name gname 
     from customer c,goods g,customer_goods cg 
     where c.id = #{id} and cg.cid = c.id and cg.gid = g.id
 </select>
</mapper>
```

- 通过商品的id，查找它被哪些顾客购买

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.southwind.repository.GoodsRepository">
    <resultMap id="goodsMap" type="com.southwind.entity.Goods">
       <id column="gid" property="id"></id>
       <result column="gname" property="name"></result>
       <collection property="customers" ofType="com.southwind.entity.Customer">
          <id column="cid" property="id"/>
          <result column="cname" property="name"/>
       </collection>
    </resultMap>
 <select id="findById" parameterType="long" resultMap="goodsMap">
    select c.id cid,c.name cname,g.id gid,g.name gname 
    from customer c,goods g,customer_goods cg 
    where g.id = #{id} and cg.cid = c.id and cg.gid = g.id
 </select>
</mapper>
```



## 逆向工程

> 优点： MyBatis 框架需要：实体类、⾃定义 Mapper 接⼝、Mapper.xml 传统的开发中上述的三个组件需要开发者⼿动创建，逆向⼯程可以帮助开发者来⾃动创建三个组件，减 轻开发者的⼯作量，提⾼⼯作效率。
>
> 缺点：当更改数据库的表结构时，需要删除已经生产的包，重新加载逆向工程

```xml
<dependency>
 <groupId>org.mybatis.generator</groupId>
 <artifactId>mybatis-generator-core</artifactId>
 <version>1.
```



### `步骤`

- 创建 MBG 配置⽂件 generatorConfig.xml 

   1、jdbcConnection 配置数据库连接信息。

   2、javaModelGenerator 配置 JavaBean 的⽣成策略。

   3、sqlMapGenerator 配置 SQL 映射⽂件⽣成策略。 

  4、javaClientGenerator 配置 Mapper 接⼝的⽣成策略。 

  5、table 配置⽬标数据表（tableName：表名，domainObjectName：JavaBean 类名）。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <context id="testTables" targetRuntime="MyBatis3">
        <!-- jdbcConnection 配置数据库连接信息。 -->
        <jdbcConnection
                driverClass="com.mysql.cj.jdbc.Driver"
                connectionURL="jdbc:mysql://localhost:3306/mybatis?useUnicode=true&amp;characterEncoding=UTF-8"
                userId="root"
                password="root"
        ></jdbcConnection>
        
        <!-- javaModelGenerator 配置 JavaBean 的⽣成策略。。 -->
        <javaModelGenerator targetPackage="com.southwind.entity"
                            targetProject="./src/main/java">
        </javaModelGenerator>
        
        <!-- sqlMapGenerator 配置 SQL 映射⽂件⽣成策略。 -->
        <sqlMapGenerator targetPackage="com.southwind.repository"
                         targetProject="./src/main/java">
        </sqlMapGenerator>
        
        <!-- javaClientGenerator 配置 Mapper 接⼝的⽣成策略。 -->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="com.southwind.repository" targetProject="./src/main/java">
        </javaClientGenerator>
        
        <!-- table 配置⽬标数据表（tableName：表名，domainObjectName：JavaBean 类名）。 -->
        <table tableName="t_user" domainObjectName="User"></table>
    </context>
</generatorConfiguration>
```



## MyBatis延迟加载

延迟加载也叫懒加载、惰性加载，使⽤延迟加载可以提⾼程序的运⾏效率，针对于数据持久层的操作， 在某些特定的情况下去访问特定的数据库，在其他情况下可以不访问某些表，从⼀定程度上减少了 Java 应⽤与数据库的交互次数。 

查询学⽣和班级的时，学⽣和班级是两张不同的表，如果当前需求只需要获取学⽣的信息，那么查询学 ⽣单表即可，如果需要通过学⽣获取对应的班级信息，则必须查询两张表。 不同的业务需求，需要查询不同的表，根据具体的业务需求来动态减少数据表查询的⼯作就是延迟加载

`在xml中的配置`

```xml
<settings>
 <!-- 打印SQL-->
 <setting name="logImpl" value="STDOUT_LOGGING" />
 <!-- 开启延迟加载 -->
 <setting name="lazyLoadingEnabled" value="true"/>
</settings>
```

将多表关联查询拆分成多个单表查询

- Student单表查询

```xml
<resultMap id="studentMapLazy" type="com.southwind.entity.Student">
   <id column="id" property="id"></id>
   <result column="name" property="name"></result>
   <association property="classes" javaType="com.southwind.entity.Classes"
     select="com.southwind.repository.ClassesRepository.findByIdLazy" column="cid">
   </association>
</resultMap>

<select id="findByIdLazyStudent" parameterType="long" resultMap="studentMapLazy">
     select * from student where id = #{id}
</select>
```

- classes单表查询

```xml
<select id="findByIdLazy" parameterType="long" resultType="com.southwind.entity.Classes">
   select * from classes where id = #{id}
</select>
```



## MyBatis缓存

什么是 MyBatis 缓存？

使⽤缓存可以减少 Java 应⽤与数据库的交互次数，从⽽提升程序的运⾏效率。⽐如查询出 id = 1 的对 象，第⼀次查询出之后会⾃动将该对象保存到缓存中，当下⼀次查询时，直接从缓存中取出对象即可， ⽆需再次访问数据库。

-  MyBatis 缓存分类 

 1、⼀级缓存：SqlSession 级别，默认开启，并且不能关闭。 操作数据库时需要创建 SqlSession 对象，在对象中有⼀个 HashMap ⽤于存储缓存数据，不同的 SqlSession 之间缓存数据区域是互不影响的。 

⼀级缓存的作⽤域是 SqlSession 范围的，当在同⼀个 SqlSession 中执⾏两次相同的 SQL 语句事，第⼀ 次执⾏完毕会将结果保存到缓存中，第⼆次查询时直接从缓存中获取。 需要注意的是，如果 SqlSession 执⾏了 DML 操作（insert、update、delete），MyBatis 必须将缓存 清空以保证数据的准确性。 

 2、⼆级缓存：Mapper 级别，默认关闭，可以开启。 使⽤⼆级缓存时，多个 SqlSession 使⽤同⼀个 Mapper 的 SQL 语句操作数据库，得到的数据会存在⼆ 级缓存区，同样是使⽤ HashMap 进⾏数据存储，相⽐较于⼀级缓存，⼆级缓存的范围更⼤，多个 SqlSession 可以共⽤⼆级缓存，⼆级缓存是跨 SqlSession 的。 

⼆级缓存是多个 SqlSession 共享的，其作⽤域是 Mapper 的同⼀个 namespace，不同的 SqlSession 两次执⾏相同的 namespace 下的 SQL 语句，参数也相等，则第⼀次执⾏成功之后会将数据保存到⼆级 缓存中，第⼆次可直接从⼆级缓存中取出数据。



### `一级缓存`

```java
public class Test4 {
    public static void main(String[] args) {
       InputStream inputStream = Test.class.getClassLoader().getResourceAsStream("config.xml");
       SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
       SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
       SqlSession sqlSession = sqlSessionFactory.openSession();
       AccountRepository accountRepository = sqlSession.getMapper(AccountRepository.class);
 //在一个session域中       
       //第一次调用findById
       Account account = accountRepository.findById(1L);
       //第二次调用findById,但后台只会显示执行了一次sql语句
       Account account1 = accountRepository.findById(1L);
       System.out.println(account);
       System.out.println(account1);
        
//在不同session域中
       //第一次调用findById
       Account account = accountRepository.findById(1L);
       //关闭session
       sqlSession.close();   
       //重新获取sqlSession
       SqlSession sqlSession = sqlSessionFactory.openSession();
       AccountRepository ar = sqlSession.getMapper(AccountRepository.class)
       //第二次调用findById , 这次后台会显示执行了两次sql语句
       Account account1 = accountRepository.findById(1L);
 }
}
```



### `二级缓存`

一：MyBatis ⾃带的⼆级缓存

- config.xml 配置开启⼆级缓存

```xml
<settings>
   <!-- 打印SQL-->
   <setting name="logImpl" value="STDOUT_LOGGING" />
   <!-- 开启延迟加载 -->
   <setting name="lazyLoadingEnabled" value="true"/>
   <!-- 开启⼆级缓存 -->
   <setting name="cacheEnabled" value="true"/>
</settings>
```

- Mapper.xml 中配置⼆级缓存

```xml
<cache></cache>
```

- 实体类实现序列化接⼝

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
 private long id;
 private String username;
 private String password;
 private int age;
}
```



二：ehcache ⼆级缓存

- pom.xml 添加相关依赖

```xml
<dependency>
 <groupId>org.mybatis</groupId>
 <artifactId>mybatis-ehcache</artifactId>
 <version>1.0.0</version>
</dependency>
<dependency>
 <groupId>net.sf.ehcache</groupId>
 <artifactId>ehcache-core</artifactId>
 <version>2.4.3</version>
</dependency>
```

- 添加 ehcache.xml

```xml
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
 <diskStore/>
 <defaultCache
    maxElementsInMemory="1000"
    maxElementsOnDisk="10000000"
    eternal="false"
    overflowToDisk="false"
    timeToIdleSeconds="120"
    timeToLiveSeconds="120"
    diskExpiryThreadIntervalSeconds="120"
    memoryStoreEvictionPolicy="LRU">
 </defaultCache>
</ehcache>
```

- config.xml 配置开启⼆级缓存

```xml
<settings>
 <!-- 打印SQL-->
 <setting name="logImpl" value="STDOUT_LOGGING" />
 <!-- 开启延迟加载 -->
 <setting name="lazyLoadingEnabled" value="true"/>
 <!-- 开启⼆级缓存 -->
 <setting name="cacheEnabled" value="true"/>
</settings>
```

- Mapper.xml 中配置⼆级缓存

```xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache">
 <!-- 缓存创建之后，最后⼀次访问缓存的时间⾄缓存失效的时间间隔 -->
 <property name="timeToIdleSeconds" value="3600"/>
 <!-- 缓存⾃创建时间起⾄失效的时间间隔 -->
 <property name="timeToLiveSeconds" value="3600"/>
 <!-- 缓存回收策略，LRU表示移除近期使⽤最少的对象 -->
 <property name="memoryStoreEvictionPolicy" value="LRU"/>
</cache>
```

- 实体类不需要实现序列化接⼝



## MyBatis动态Sql

使⽤动态 SQL 可简化代码的开发，减少开发者的⼯作量，程序可以⾃动根据业务参数来决定 SQL 的组 成。 

- `where , if 标签 `

```xml
<select id="findByAccount" parameterType="com.southwind.entity.Account"
resultType="com.southwind.entity.Account">
 select * from t_account
   <where>
      <if test="id!=0">
       id = #{id}
     </if>
     <if test="username!=null">
      and username = #{username}
     </if>
     <if test="password!=null">
      and password = #{password}
     </if>
     <if test="age!=0">
      and age = #{age}
     </if>
  </where>
</select>
```

if 标签可以⾃动根据表达式的结果来决定是否将对应的语句添加到 SQL 中，如果条件不成⽴则不添加， 如果条件成⽴则添加。

where 标签可以⾃动判断是否要删除语句块中的 and 关键字，如果检测到 where 直接跟 and 拼接，则 ⾃动删除 and，通常情况下 if 和 where 结合起来使⽤。

- `choose 、when 标签`

```xml
<select id="findByAccount" parameterType="com.southwind.entity.Account" resultType="com.southwind.entity.Account">
     select * from t_account
     <where>
        <choose>
     <when test="id!=0">
       id = #{id}
     </when>
     <when test="username!=null">
       username = #{username}
     </when>
     <when test="password!=null">
       password = #{password}
     </when>
     <when test="age!=0">
       age = #{age}
     </when>
       </choose>
 </where>
</select>
```



- `trim 标签`

 trim 标签中的 prefix 和 suffix 属性会被⽤于⽣成实际的 SQL 语句，会和标签内部的语句进⾏拼接，如 果语句前后出现了 prefixOverrides 或者 suffixOverrides 属性中指定的值，MyBatis 框架会⾃动将其删除。

```xml
<select id="findByAccount" parameterType="com.southwind.entity.Account"
resultType="com.southwind.entity.Account">
 select * from t_account
 <trim prefix="where" prefixOverrides="and">
 <if test="id!=0">
 id = #{id}
 </if>
 <if test="username!=null">
 and username = #{username}
 </if>
 <if test="password!=null">
 and password = #{password}
 </if>
 <if test="age!=0">
 and age = #{age}
 </if>
 </trim>
</select>
```



- `set标签`

set 标签⽤于 update 操作，会⾃动根据参数选择⽣成 SQL 语句。

```xml
<update id="update" parameterType="com.southwind.entity.Account">
 update t_account
 <set>
 <if test="username!=null">
 username = #{username},
</if>
 <if test="password!=null">
 password = #{password},
 </if>
 <if test="age!=0">
 age = #{age}
 </if>
 </set>
 where id = #{id}
</update>
```



- `foreach 标签 `

  foreach 标签可以迭代⽣成⼀系列值，这个标签主要⽤于 SQL 的 in 语句。

  ```xml
  <select id="findByIds" parameterType="com.southwind.entity.Account"
  resultType="com.southwind.entity.Account">
   select * from t_account
   <where>
   <foreach collection="ids" open="id in (" close=")" item="id"
  separator=",">
   #{id}
   </foreach>
   </where>
  </select>
  ```

  