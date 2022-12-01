# Springboot快速开发脚手架

> 林九 221201

## 框架介绍

**基于Quanta研发部后端脚手架，特此感谢Quanta和17th CEO 泽帆哥**

主要增加和修改习惯于自己开发的配置和类

## 开发须知

**配置数据库、存放文件路径、邮箱**

**需要修改包名的地方**

- 启动类ArchetypeApplication中的`@MapperScan`
- exception下的异常捕获类GlobalExceptionHandler中的`@ControllerAdvice`
- log包下的RequestLogAspect `@Pointcut`

## 功能介绍

**基于redis的多级权限管理**

利用权限拦截注解RequiredPermission中的参数进行对应权限拦截

具体流程：

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

![](https://imagebed-1309295650.cos.ap-guangzhou.myqcloud.com/note/202212012219865.png)

**全局异常捕获&告警**

- 利用微信企业微信机器人告警
- 当线上/测试环境出现异常时 通过企业微信机器人发送信息告警

## 最后

**由衷感谢Quanta和各位师兄师姐对我的帮助和支持😘**
