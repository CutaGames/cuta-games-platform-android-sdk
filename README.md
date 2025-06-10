# cuta-games-platform-android-sdk
cuta games platform android sdk
# Cuta-Platform Android SDK

[![Release](https://jitpack.io/v/CutaGames/cuta-games-platform-android-sdk.svg)](https://jitpack.io/#CutaGames/cuta-games-platform-android-sdk)

-轻量级DID获取SDK，帮助Android应用快速集成Cuta平台身份标识系统
-多链钱包支付接口
-游戏数据获取接口

## 集成方式

### 1. 添加仓库依赖

在项目根目录的`settings.gradle`中添加JitPack仓库：

```gradle
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. 添加SDK依赖
在模块的build.gradle中：

```gradle
dependencies {
    implementation("com.github.CutaGames:cuta-games-platform-android-sdk:0.01")
}
```

## 快速开始
### 1. 初始化SDK
初始化：

```
CutaPlatformSdk.init(this)
```
### 2. 获取DID
在任何需要DID的地方调用：

```
val did = CutaPlatformSdk.getCurrentDID()
did?.let { 
    // 使用DID
    startGame(it)
} ?: run {
    // 处理未获取到DID的情况
    showLoginPrompt()
}
```

## API参考
### CutaDID 单例对象

| 方法     | 说明     |
|---------|--------|
| init(context: Context) | 必须首先调用 |
| getCurrentDID(): String | 获取did  |

## 示例响应数据
### 成功获取的DID格式：
```
did:cuta:6yLcQFEqbC7jAHkFxgiz8Ntty4QQxv7V34JwfLZcxNkW
```
