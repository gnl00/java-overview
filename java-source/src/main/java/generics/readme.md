# 泛型

泛型的本质是**参数化类型**，所操作的类型被指定为一个参数。

泛型的优缺点
* 最大的优点是提供了程序的类型安全，同时可以向后兼容。
* 在 jdk 1.7 之前需要在声明语句的两侧都写明泛型的类型，在 1.7 之后新增了泛型的类型推断，只需要在后侧写明即可。
```java
// jdk 1.7 之前
Map<String, String> myMap = new HashMap<String, String>();

// jdk 1.7 之后
// 注意后面的 "<>"，只有加上 “<>” 才表示是自动类型推断，否则就是非泛型类型的 HashMap
Map<String, String> myMap = new HashMap<>();
```