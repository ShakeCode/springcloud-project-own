# 引用配置中心的配置时需注意

这里使用bootstrap.yml文件，该文件会先于application.yml被加载，spring-cloud相关的属性必须配置在bootstrap.properties中，因为config的相关配置会先于application.properties，而bootstrap.properties的加载也是先于application.properties。例如上面的defaultZone如果不配置，则找不到service-id，会导致启动失败

#### 启动服务后,注册到服务中心成功，测试获取配置中心配置成功:

![img.png](img.png)

# 終止监听端口

- 获取监听端口的进程
- 终止对应的进程号
- cmd 演示

```
C:\Users\90393>netstat -ano | findstr 8090
  TCP    0.0.0.0:8090           0.0.0.0:0              LISTENING       21428
  TCP    [::]:8090              [::]:0                 LISTENING       21428
  TCP    [::1]:51756            [::1]:8090             TIME_WAIT       0

C:\Users\90393>TASKKILL /F /PID 21428
成功: 已终止 PID 为 21428 的进程。


输出线程堆栈信息: 
 jstack -l pid > 路径文件
 
 jstack -l 21428 > D:/eureke-thread-dump-stack.txt
```

# 熔断机制

```
熔断很好理解, 当Hystrix Command请求后端服务失败数量超过一定比例(默认50%), 断路器会切换到开路状态(Open). 这时所有请求会直接失败而不会发送到后端服务. 断路器保持在开路状态一段时间后(默认5秒), 自动切换到半开路状态(HALF-OPEN). 这时会判断下一次请求的返回情况, 如果请求成功, 断路器切回闭路状态(CLOSED), 否则重新切换到开路状态(OPEN). Hystrix的断路器就像我们家庭电路中的保险丝, 一旦后端服务不可用, 断路器会直接切断请求链, 避免发送大量无效请求影响系统吞吐量, 并且断路器有自我检测并恢复的能力。
```