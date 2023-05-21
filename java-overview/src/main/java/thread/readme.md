## Thread
**线程状态**
```
          ┌─────────────┐
          │     New     │
          └─────────────┘
                 │
                 ▼
┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
| ┌─────────────┐ ┌─────────────┐ │
| │  Runnable   │ │   Blocked   │ │
| └─────────────┘ └─────────────┘ │
│ ┌─────────────┐ ┌─────────────┐ │
│ |   Waiting   │ │Timed Waiting| │
│ └─────────────┘ └─────────────┘ │
└ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
                 │
                 ▼
          ┌─────────────┐
          │ Terminated  │
          └─────────────┘
```

## References
[多线程交替打印](https://zhuanlan.zhihu.com/p/370130458)

[Java并发编程笔记之StampedLock锁源码探究](https://www.cnblogs.com/huangjuncong/p/9191760.html)
[StampedLock-JDK1.8](https://zhuanlan.zhihu.com/p/324815228)
[多线程-StampLock](https://www.jianshu.com/p/466cd7d8324e)
[源码分析：升级版的读写锁 StampedLock](https://www.cnblogs.com/admol/p/14007975.html)

[CAS 原理详解](https://www.cnblogs.com/huansky/p/15746624.html)