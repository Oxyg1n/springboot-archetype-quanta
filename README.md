# Quanta后端Springboot快速开发脚手架

> 林九 230804

## 框架介绍

**基于Quanta17th研发部脚手架改进优化，感谢后端泽帆师兄和仕鹏师兄的指点和关照**

## 开发须知

> 修改配置文件

```yaml
project:
  isDebug: true # debug模式为true，当出现异常时会打印异常在控制台并且返回给前端，false则不会
  filePath: E:/test # 用户文件保存路径

user:
  tokenLifeDays: 7 # 用户token有效期/天

# mysql配置 redis配置 mail邮箱配置
# （可选，bu'x）cos配置 oss配置 minio配置
```

> 修改包名

- 启动类ArchetypeApplication中的`@MapperScan`
- exception下的异常捕获类GlobalExceptionHandler中的`@ControllerAdvice`
- log包下的RequestLogAspect `@Pointcut`切面

> 根据需要可删除的部分

- WxMaConfig & WxMaProperties 提供微信小程序的功能 无需对接小程序可删除
- QcloudConfig & QcloudProperties 无需使用腾讯云储存桶功能时可删除
- OSSConfig & OSSProperties & OSSUtils 无需使用阿里云对象存储功能时可删除
- MinioConfig & MinioUtils 无需使用minio对象存储功能时可删除

> 可选使用代码生成器

- CodeGenerator 根据注释填入对应信息后直接运行即可一键生成controller service mapper entity
- 生成的entity需要手动调整一些属性的类型
    - 比如数据库中的tinyint会被设置为boolean
    - 时间类型

## 功能介绍

> 基于redis的多级权限管理

利用权限拦截注解RequiredPermission中的参数进行对应权限拦截

**具体流程：**

请求进入拦截器

- 拦截器判断是否放行
  - 是=>直接放行
  - 否=>进入拦截器 => 判断是否携带Token
    - 否=>拒绝访问
    - 是 =>通过Token从redis中获取uid和role
      - 获取不到 (用户未登录)=>拒绝访问
      - 将uid和role塞入请求头 后续可通过BaseController中的getUid()或getRole()
        - 进行权限验证 判断类和方法上的注解
          - 都不允许=>拒绝访问
          - 允许=>放行

![](https://images-new-1309295650.cos.ap-guangzhou.myqcloud.com/note/20230804010111.png)

**全局异常捕获&告警**

- 利用微信企业微信机器人告警
- 当线上/测试环境出现异常时 通过企业微信机器人发送信息告警



