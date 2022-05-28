本文基于Zipkin Server 2.21.4编写，理论支持Zipkin 2.0及更高版本。 Zipkin Server的 API兼容性（微服务通过集成reporter模块，从而Zipkin Server通信） 非常好，对于Spring
Cloud Greenwich，Zipkin Server只需安装2.x即可。

#### 方式1

```
使用Zipkin官方的Shell下载 从maven中央仓库下载，特别慢并且还容易中断

如下命令可下载最新版本:
1. curl -sSL https://zipkin.io/quickstart.sh | bash -s

2. 下载下来的文件名为 zipin.jar
```

#### 方式2：

```

1. 到Maven中央仓库下载 TIPS 如下地址令可下载最新版本。 访问如下地址即可：
https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec

下载下来的文件名为 zipkin-server-2.21.4-exec.jar

2. 启动Zipkin Server

 - 使用如下命令，即可启动Zipkin Server

 - java -jar 你的jar包

 - 访问http://localhost:9411 即可看到Zipkin Server的首页。
```

#### 方式3

```
https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/
```
