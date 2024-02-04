# ant-task
一个轻量级的定时任务管理器

## 简介
在**Spring Boot**中有一个自带的**@Scheduled**注解，可以用于启动定时任务，使用很方便，
但也存在着不足，例如定时任务直接写死、方法无参数、不能随时启停等等。
**ant-task**就是为了解决这些问题而存在的，在解决问题的同时也一如既往的方便接入，**ant-task**特性如下：
- 图形化界面管理
- 方便快捷接入
- 支持随时启停任务
- 支持动态参数传递
- 支持动态修改任务时间
- 支持Cron表达式
- 支持日志ID跟踪

**注意**：因为没有使用第三方存储，所以不适合分布式项目，下个版本更新

## 安装
**maven**
```
<dependency>
  <groupId>com.github.hwywl</groupId>
  <artifactId>ant-task</artifactId>
  <version>1.0.4-RELEASE</version>
</dependency>
```

**Gradle**
```
implementation 'com.github.hwywl:ant-task:1.0.4-RELEASE'
```

## 使用
使用很简单，只要两步即可接入（本项目依赖**Spring Boot**中的**spring-boot-starter-web**组件）。

1. 在pom.xml中引入上面的依赖。
2. 在Spring Boot配置文件中设置如下配置,根据不同的配置文件二选一即可。

```
# application.yml

ant-task:
  conf:
    pool-size: 10
    file-path: /home/ec2-user/server/sms/task.properties
```

```
# application.properties

# 定时任务线程池，默认为 6
ant-task.conf.pool-size=10
# 任务数据配置存储路径(必填)
ant-task.conf.file-path=/home/ec2-user/server/sms/task.properties
```

此时就已经成功接入了，是不是很简单，我们启动项目访问：**http://localhost:{port}/task.html** 进入页面。

![](https://hwy-figure-bed.oss-cn-hangzhou.aliyuncs.com/blog/image/1631842589969-1.png)

看到页面说明我们接入成功了，接下来我们配置一个数据试一下。

新建一个测试类：
```java
/**
 * 测试定时任务
 * @author HWY
 * 2022年12月13日15:07:44
 */
@Component("schedulingTask")
public class TaskDemo {
    public void taskParams(String params) {
        System.out.println("定时执行有参任务：" + params);
    }

    public void taskJsonParams(TaskBean params) {
        System.out.println("定时执行有参任务：" + params);
    }

    public void taskNoParams() {
        System.out.println("定时执行无参任务！");
    }

    class TaskBean{
        /**
         * id
         */
        private Integer id;

        /**
         * 账户id
         */
        private String customerId;
    }
}
```

写好测试类之后我们**重启**项目，进入上面URL那个配置界面，进行如下配置：

![](https://hwy-figure-bed.oss-cn-hangzhou.aliyuncs.com/blog/image/1631843002464-2.png)

或者试用JSON参数配置
![](https://hwy-figure-bed.oss-cn-hangzhou.aliyuncs.com/blog/image/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20221213151033.png)

配置非常简单，点击确定定时任务就开始执行了，我配置了两台测试，一个有参一个无参，来看看效果。

![](https://hwy-figure-bed.oss-cn-hangzhou.aliyuncs.com/blog/image/1631843346023-4.png)

运行结果如下：

![](https://hwy-figure-bed.oss-cn-hangzhou.aliyuncs.com/blog/image/1631843374250-3.png)

## 技术栈
- SpringBoot
- ThreadPoolTaskScheduler 线程池
- Vue

## 版本迭代
### 1.0.4-RELEASE
- 小版本更新各个包版本

### 1.0.3-RELEASE
- 增加参数对JSON数据的支持，可以让入参的Bean对象直接接收
- 修复因更新导致的任务重复执行问题

### 1.0.2-RELEASE
- 增加页面的方便配置
- 增加任务状态开关
- 发布到中央仓库

### 1.0.1-RELEASE
- 实现基础定时功能
- 实现手动文件可配置化

## 感谢
- 感谢[江南一点雨](https://github.com/lenve)提供了思路
- 工具还有不足之处，请大家Issues ヾ(๑╹◡╹)ﾉ"
- 我那么可爱你不点个star吗 φ(>ω<*) 


## 问题建议

- 联系我的邮箱：ilovey_hwy@163.com
- 我的博客：https://www.hwy.ac.cn
- GitHub：https://github.com/HWYWL