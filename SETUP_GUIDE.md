# RikkaHub 项目配置与运行指南

> 本指南帮助你从零开始配置并运行RikkaHub项目

## 📋 前置要求

### 必需软件
- ✅ **Android Studio**: Latest版本 (推荐 Hedgehog 或更新)
- ✅ **JDK 17**: Android Studio自带或单独安装
- ✅ **Git**: 用于版本控制

### 系统要求
- Windows 10/11, macOS, 或 Linux
- 至少 8GB RAM (推荐 16GB)
- 至少 10GB 可用磁盘空间

---

## 🚀 快速开始 (5步)

### Step 1: 克隆项目

```bash
# 如果你还没克隆
git clone https://github.com/re-ovo/rikkahub.git
cd rikkahub
```

### Step 2: 配置 Firebase (必需)

#### 2.1 创建 Firebase 项目

1. 访问 [Firebase Console](https://console.firebase.google.com/)
2. 点击 **"添加项目"**
3. 输入项目名称 (例如: `rikkahub-dev`)
4. 按照向导完成创建

#### 2.2 添加 Android 应用

1. 在 Firebase 项目中点击 **"添加应用"** → 选择 Android 图标
2. 填写应用信息:
   - **Android 包名**: `me.rerere.rikkahub.debug` (Debug版本)
   - **应用昵称**: RikkaHub Debug (可选)
   - **SHA-1**: (可选,后续可添加)
3. 点击 **"注册应用"**

#### 2.3 下载配置文件

1. 下载 `google-services.json` 文件
2. 将文件放置到项目的 [`app/`](app/) 目录下

```
rikkahub/
└── app/
    ├── build.gradle.kts
    ├── google-services.json  ← 放这里
    └── src/
```

#### 2.4 启用 Firebase 服务 (可选但推荐)

在 Firebase Console 中启用以下服务:
- **Firebase Analytics**: 用于应用分析
- **Firebase Crashlytics**: 用于崩溃报告
- **Firebase Remote Config**: 用于远程配置

### Step 3: 配置签名 (可选,仅Release版本需要)

如果你只是运行Debug版本,**可以跳过此步骤**。

#### 3.1 生成签名密钥 (如果没有)

```bash
# Windows
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias

# macOS/Linux
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

按提示输入密码和信息。

#### 3.2 创建 `local.properties`

在项目根目录创建 `local.properties` 文件:

```properties
# local.properties (不会提交到Git)
sdk.dir=C\:\\Users\\你的用户名\\AppData\\Local\\Android\\Sdk

# 签名配置 (仅Release需要)
storeFile=my-release-key.jks
storePassword=你的密钥库密码
keyAlias=my-key-alias
keyPassword=你的密钥密码
```

**注意**: 
- Windows路径使用 `\\` 转义
- 不要提交 `local.properties` 到Git (已在 `.gitignore` 中)

### Step 4: 打开项目

1. 启动 **Android Studio**
2. 选择 **"Open"** (打开现有项目)
3. 导航到 `rikkahub` 目录并点击 **OK**
4. 等待 Gradle 同步完成 (首次可能需要10-20分钟)

#### 4.1 处理可能的错误

**错误: "google-services.json not found"**
```
解决: 确保 google-services.json 在 app/ 目录下
```

**错误: "SDK location not found"**
```
解决: 在 local.properties 中添加 SDK 路径
sdk.dir=/path/to/android/sdk
```

**错误: Gradle 同步失败**
```
解决: 
1. 检查网络连接 (Gradle需要下载依赖)
2. 尝试: File → Invalidate Caches → Invalidate and Restart
3. 清理项目: Build → Clean Project
```

### Step 5: 运行应用

#### 5.1 选择运行配置

在 Android Studio 工具栏:
- 确保选择了 **"app"** 配置
- 选择构建变体: **Debug** (默认)

#### 5.2 选择设备

**选项 A: 使用模拟器**
1. 点击设备下拉框
2. 选择 **"Device Manager"**
3. 创建虚拟设备 (推荐: Pixel 6, API 33+)
4. 启动模拟器

**选项 B: 使用真机**
1. 在手机上启用 **开发者选项**
2. 启用 **USB调试**
3. 用USB连接手机到电脑
4. 在手机上允许USB调试授权
5. 设备会出现在Android Studio设备列表中

#### 5.3 点击运行

点击工具栏的 **绿色三角形 ▶️** 按钮或按 `Shift+F10`

---

## 🛠️ 高级配置

### 构建变体说明

| 变体 | 用途 | 包名 |
|------|------|------|
| **debug** | 日常开发调试 | `me.rerere.rikkahub.debug` |
| **release** | 正式发布版本 | `me.rerere.rikkahub` |
| **baseline** | 性能基准测试 | `me.rerere.rikkahub.debug` |

切换方式: `Build → Select Build Variant`

### Gradle 构建命令

```bash
# 构建Debug APK
./gradlew assembleDebug

# 构建Release APK (需要签名配置)
./gradlew assembleRelease

# 构建Release Bundle
./gradlew bundleRelease

# 同时构建APK和AAB
./gradlew buildAll

# 清理构建
./gradlew clean

# 运行测试
./gradlew test
```

### 环境变量配置

如果需要自定义配置,可以在 [`local.properties`](local.properties) 中添加:

```properties
# 自定义配置
CUSTOM_API_URL=https://your-api.com
DEBUG_MODE=true
```

然后在代码中读取:

```kotlin
val properties = Properties()
properties.load(FileInputStream(rootProject.file("local.properties")))
val apiUrl = properties.getProperty("CUSTOM_API_URL")
```

---

## 🔧 常见问题排查

### Q1: Gradle 同步一直卡住

**解决方案**:
```bash
# 1. 停止 Gradle 守护进程
./gradlew --stop

# 2. 清理 Gradle 缓存
rm -rf ~/.gradle/caches/

# 3. 重新同步
```

在 Android Studio:
- `File → Invalidate Caches → Invalidate and Restart`

### Q2: 编译报错 "Execution failed for task ':app:processDebugGoogleServices'"

**原因**: 缺少 `google-services.json` 文件

**解决**: 
1. 确认文件在 [`app/google-services.json`](app/google-services.json)
2. 检查文件内容是否完整(JSON格式)
3. 重新从 Firebase Console 下载

### Q3: 运行时崩溃 "Unable to find explicit activity class"

**原因**: Manifest 配置问题

**解决**:
1. 检查 [`app/src/main/AndroidManifest.xml`](app/src/main/AndroidManifest.xml)
2. 确认 Activity 正确注册
3. Clean 并 Rebuild 项目

### Q4: 模块依赖错误

**错误示例**:
```
Could not find project :ai
```

**解决**:
1. 检查 [`settings.gradle.kts`](settings.gradle.kts:1) 中是否包含该模块
2. 确认模块目录存在
3. 重新同步 Gradle

### Q5: OutOfMemoryError

**解决**: 在 [`gradle.properties`](gradle.properties:1) 中增加内存:

```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m
```

### Q6: 网络相关错误 (无法下载依赖)

**解决**:
```groovy
// 在 settings.gradle.kts 添加国内镜像 (如果在中国)
repositories {
    maven { url 'https://maven.aliyun.com/repository/public' }
    maven { url 'https://maven.aliyun.com/repository/google' }
    google()
    mavenCentral()
}
```

---

## 📱 运行到设备

### 通过 Android Studio

1. 连接设备或启动模拟器
2. 点击 **Run ▶️** 按钮
3. 应用会自动安装并启动

### 通过 ADB 手动安装

```bash
# 构建APK
./gradlew assembleDebug

# 查找APK路径
# app/build/outputs/apk/debug/rikkahub_1.6.11_debug.apk

# 安装到设备
adb install -r app/build/outputs/apk/debug/rikkahub_1.6.11_debug.apk

# 启动应用
adb shell am start -n me.rerere.rikkahub.debug/.RouteActivity
```

---

## 🧪 测试与调试

### 运行单元测试

```bash
# 所有模块的单元测试
./gradlew test

# 特定模块
./gradlew :app:test
./gradlew :ai:test
```

### 运行仪器测试 (需要设备/模拟器)

```bash
# 所有仪器测试
./gradlew connectedAndroidTest

# 特定模块
./gradlew :app:connectedAndroidTest
```

### 调试技巧

**设置断点**:
1. 在代码行号旁点击设置断点
2. 点击 **Debug 🐞** 按钮启动调试
3. 应用会在断点处暂停

**查看日志**:
- 在 Android Studio 底部打开 **Logcat**
- 过滤标签: `tag:RikkaHub`
- 过滤包名: `package:me.rerere.rikkahub`

---

## 📦 导出APK

### Debug APK (无需签名)

```bash
./gradlew assembleDebug
```

输出: `app/build/outputs/apk/debug/rikkahub_1.6.11_debug.apk`

### Release APK (需要签名配置)

```bash
./gradlew assembleRelease
```

输出: `app/build/outputs/apk/release/rikkahub_1.6.11_release.apk`

### Release Bundle (Google Play)

```bash
./gradlew bundleRelease
```

输出: `app/build/outputs/bundle/release/rikkahub_1.6.11_release.aab`

---

## 🔍 项目结构速览

```
rikkahub/
├── app/                          # 主应用模块
│   ├── google-services.json     # ⚠️ 必需: Firebase配置
│   └── src/main/
├── ai/                           # AI SDK模块
├── document/                     # 文档解析模块
├── search/                       # 搜索服务模块
├── tts/                          # TTS模块
├── highlight/                    # 代码高亮模块
├── common/                       # 通用工具模块
├── build.gradle.kts              # 根构建脚本
├── settings.gradle.kts           # 模块配置
├── gradle.properties             # Gradle属性
├── local.properties              # ⚠️ 本地配置(不提交Git)
└── gradlew                       # Gradle包装器
```

---

## 📚 相关资源

### 官方文档
- [Android Studio 文档](https://developer.android.com/studio)
- [Gradle 构建指南](https://developer.android.com/build)
- [Firebase Android 设置](https://firebase.google.com/docs/android/setup)

### 项目资源
- [项目 README](README.md)
- [项目分析报告](PROJECT_ANALYSIS.md)
- [Discord 社区](https://discord.gg/9weBqxe5c4)

### 遇到问题?
1. 查看本文档的 [常见问题排查](#🔧-常见问题排查) 部分
2. 在项目中搜索相关错误信息
3. 加入 [Discord](https://discord.gg/9weBqxe5c4) 或 [QQ群](https://qm.qq.com/q/I8MSU0FkOu) 寻求帮助

---

## ✅ 配置检查清单

运行前确认以下项目:

- [ ] Android Studio 已安装并更新到最新版
- [ ] JDK 17 已配置
- [ ] 项目已克隆到本地
- [ ] `google-services.json` 已放置在 `app/` 目录
- [ ] (可选) `local.properties` 已配置签名信息
- [ ] Gradle 同步成功完成
- [ ] 设备/模拟器已准备就绪
- [ ] 网络连接正常 (首次需要下载依赖)

全部完成后,点击 **Run ▶️** 即可启动应用!

---

**祝开发顺利! 🎉**