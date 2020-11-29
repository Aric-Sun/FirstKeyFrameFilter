# FirstKeyFrameFilter

## 主要功能

解决录播文件[缺帧](#问题描述)的问题，免去[手动操作](#方案优化)的过程

## 项目背景

### 问题描述

在我做直播录像的时候，经常会发现录下来的视频文件开头部分**缺帧**，具体表现为：

- 在 PotPlayer 上，视频开头部分画面是不动的，但是有声音。
- 在 mpv 上，时间帧自动跳到几秒后。
- 投稿到B站上，如果没有二压的话（现在应该全面二压了），会把没有画面的部分剔除掉，后面的画面提前，从而造成**音画严重不同步**。（主要影响。因为播放器是可以自动校对时间轴的，不怎么影响观看体验）

#### 触发条件

由于我并没有去大量拉流其他主播的直播去验证问题的普遍性，因此本项目并不具有普适性。仅根据个人见闻来说，这种问题还是比较小众的，很可能只有这一个主播经常会有这种问题，至少在我另外一个录的主播没出现过这种问题。个人猜测是主播开播操作上有问题，这和她每次开播不先开麦克风<u>可能</u>有点关系，反正看起来不像是服务器的锅，也不像[录播姬](https://rec.danmuji.org)的锅。

- 主播：[一只萌豌豆](https://live.bilibili.com/1123)
- 录播工具：[录播姬](https://rec.danmuji.org)，版本：1.1.18

### 初步解决方案

针对此问题，比较无脑的方案是重新转码压制（格式工厂/小丸工具箱），但这个方案占用时间长，资源消耗多，相当不适合我这种轻薄本。无损切割才是适合我的方案，利用 ffmpeg 的`-ss`和`-c copy`参数可以将缺帧的部分**快速**切除掉（只需切除开头的缺帧，如果末尾有缺帧是不会造成B站视频音画不同步的，而且一般也没有），那么这个`-ss`参数的值就是画面刚开始动的时间。

### 原理解释

上面这句话很好理解，哪里没问题了就从哪里开始，但这会牵扯到另外的问题，就是在这个数值的位置上为什么视频会开始动，这就要解释一下关键帧的原理了。视频帧是根据上一帧的数据进行画面偏移（offset）的，一个一个往前溯源，最终会找到前面的一个关键帧。如果找不到关键帧，播放器会不知道该怎么处理到下一个关键帧之前的帧数据，这就造成了缺帧。这里的关键帧指的是原始视频的关键帧，而不是视频切割后自动打上的关键帧（文件头）（视频开头没有关键帧播放器是认不出来的）。那么在非关键帧上切一刀，必然会造成视频的缺帧。参考 [cv4373238](https://www.bilibili.com/read/cv4373238)。

### 进阶解决方案

一开始的时候，我是肉眼看的，以1s为单位不断加减（零点几秒肉眼难以估算），有的时候会被 ffmpeg 纠正（`-ss`参数是让 ffmpeg 在此处**周围**寻找（seeking）关键帧，参考 [ffmpeg 官方文档](http://trac.ffmpeg.org/wiki/Seeking) ），大部分时候会造成新的缺帧，就是 ffmpeg 在有限的范围内并没有找到关键帧，如果比实际的关键帧提前了，那么缺帧的时长会短那么一点点；如果切在关键帧后，那么生成的视频会缺帧到下一个关键帧。此时再微调的话，无疑是很浪费时间的，那么，如何获得准确的关键帧位置呢？借由 genteure 的 fib（详见[项目依赖](#项目依赖)），可以在其解析出的 XML文件中找到**第二个**关键帧的位置，然后用 ffmpeg 对此位置进行切割（将第二个关键帧时间赋值给`-ss`参数）。

### 方案优化

长此以往，我逐渐觉得，每天进行这样的操作有些繁琐，细数一下操作的步骤：

1. 使用 fib 分析 FLV ，得到 xml，命令行 e.g.：`fib parse test.flv out.xml`
2. 打开 XML搜索第二个 keyframe 属性，找到同一行的时间值
3. 使用 ffmpeg 对得到的时间进行视频切割，命令行 e.g.：`ffmpeg -i test.flv -ss 8.3 -c copy test-cut.flv`

其中包含两个问题：

1. 每天重复输入相似的命令，而且工具没有加入环境变量的话，需要通过拖拽把路径拉进cmd，而且录播的文件名通常很长，也需要拖拽
2. XML 文件大小随 FLV 文件大小而定，基于方便管理和容错的原因，设置了[录播姬](https://rec.danmuji.org/)的自动切片时长为一个小时，一个 4Mbps 码率的直播录像，一个小时大概有 1.78GB 左右大小，相对的，XML也起码有 28.5MB 左右。所以，使用 Sublime 打开 XML时，会有一个较长的加载时间（大型 IDE 可以以只读模式秒加载，但是我这种使用场景不适合开一个 IDE 只用来看一个 XML ），而且滚动屏幕的时候也十分的卡顿

那么，如何通过编程将这两个工具联合起来，免去重复打命令的繁琐操作，隐去中间步骤，提高大体积 XML 读取速度呢？这就导致了本项目的诞生。

## 必要条件

本项目绝大部分（几乎全部）运行环境已打包进压缩包和Setup，只需注意操作系统。

-   Windows（目前）
-   JDK（可选） `1.8`(`jar`) / `1.6+`(`exe`) ，此处详见[Assets说明](#Assets说明)

## 安装
### Assets

#### 文件列表

```
FirstKeyFrameFilter.exe
FirstKeyFrameFilter.jar
FirstKeyFrameFilter{version-code}-Full.zip
FirstKeyFrameFilter{version-code}-Full_Setup.exe
FirstKeyFrameFilter{version-code}-Without_JRE.zip
```

#### Assets说明

-   由于项目依赖了两个 `exe` 可执行文件和 JDK 环境，导致程序体积过大，实际的核心代码只占了相当小的一部分。
但基本上，依赖的`exe`和 JDK 环境都不会发生变化。所以请在后续更新时**直接下载** `FirstKeyFrameFilter.exe` 以覆盖旧版本。

-   单独的 `FirstKeyFrameFilter.exe` 无法正常工作，需要同目录下至少存在 `lib-apps` 文件夹和其下的两个 `exe`。错误信息被重定向到当前目录的`error.log`，如果程序没有输出或者黑框一闪而过或者其他异常，请首先查阅此 `log` 文件。

-   `jar` 文件是为了未来可能实现的跨平台程序而上传的，使用方法（命令行）：`java -jar FirstKeyFrameFilter.jar {flvFile}`。
    注意，必须使用与编译版本（`1.8`）相同的 JDK 版本运行 `jar`，否则会出现`java.lang.UnsupportedClassVersionError: Main : Unsupported major.minor version 52.0`错误。`exe` 程序无此问题。
    
-   带有 Full 字样的文件都包含了完整的依赖 `exe` 和 JDK 环境。

-   如果电脑上已有 JDK 环境（>=`1.6`），即环境变量可访问，则可以删除程序目录下的 `jre1.8.0_161` 文件夹，或下载 `~Without_JRE.zip`。

### 安装步骤

1.  下载`zip`压缩包或者`~Setup.exe`，如果 JDK 版本大于`1.6`可以下载`~Without_JRE.zip`
2.  直接解压或跟随安装向导安装
-   更新时直接下载`FirstKeyFrameFilter.exe` 覆盖
-   如果你耐心看完上面的[Assets说明](#Assets说明)，就会发现这只是个总结

### 目录结构描述

必须按以下结构放置程序，保证相对位置，否则无法运行。

```
FirstKeyFrameFilter
│ FirstKeyFrameFilter.exe
│
├─jre1.8.0_161（可选）
│
└─lib-apps
        ffmpeg.exe
        FlvInteractiveRebase.exe
```

## 使用方法

1.  拖拽

    将一个或**多个** FLV 文件拖入`FirstKeyFrameFilter.exe`运行，这是<u>最简单直接</u>的方法

2.  命令行

    `FirstKeyFrameFilter.exe test.flv`。其中，`test.flv`是待处理的[缺帧](#问题描述) FLV 视频文件

## 项目依赖

+ FlvInteractiveRebase（fib，FLV 文件编辑工具）

  + [录播姬QQ群](https://jq.qq.com/?_wv=1027&k=pJMpD57V)内部测试版 beta 1
  + By genteure ( fib-beta@danmuji.org )
  + 软件核心功能：
      - 对 FLV 进行 Tag 级别编辑处理，可用于解决各类奇葩问题
      - 使用简单的文本文件作为命令格式，方便手工编辑...
      - ...同时也方便编写脚本，实现自动化处理
      - 命令文件相对更小易于传输，方便协助不太懂 FLV 细节的人修复文件
  + 软件主要用法（假设软件文件名为 fib）：
      1. **读取 FLV 文件，生成命令文件**（本项目所依赖）  
         `./fib parse 有问题的.flv 命令.xml`
      2. 读取其他供参考或复制数据的 FLV 文件  
         `./fib parse 其他.flv 其他命令.xml`
      3. 手动编辑命令文本文件
      4. 使用修改过的命令文件生成新的 FLV 文件  
         `./fib build 命令.xml 修复之后.flv`
      - 查看帮助请运行  
         `./fib help`
  
+ [ffmpeg](https://ffmpeg.org/)
  
  + 主要用来进行 FLV 无损切割

## 获取帮助

-   前往 [Issues](https://github.com/Aric-Sun/FirstKeyFrameFilter/issues) 发布新问题
-   给我发邮件：zsunqian69@gmail.com

## 加入开发

开发之前，你需要：

-   IntelliJ IDEA 2019.3.3 或其他 Java IDE
-   JDK 1.6+，最好是1.8

由`src/Main.java`调用三个模块来完成整个工作，分别是

1.  调用`fib`：读取 FLV，生成 XML
2.  StAX 解析 XML：读取第二个关键帧的时间
3.  调用`ffmpeg`：将接收到的时间作为开始时间切割 FLV，在同一文件夹下生成新的文件，源文件不删除

如果你想研究这个项目的源代码，或者修改功能的话，你可以从以上三个模块的入口开始看起：

1.  `src/XMLGenerator/FlvInteractiveRebase.java`
2.  `src/XMLParsing/StAX/XMLReader.java`
3.  `src/FLVCutter/FastForwardMpeg.java`

### 如何打包

1.  打包成`jar`：各个 IDE 都有相应的功能，具体方法自查。
2.  封装成`exe`：使用 exe4j Wizard，配置文件见 `/exe4jConfigFile.exe4j`
3.  打包成Setup：使用 Inno Setup，配置文件见 `/InnoSetupConfig.iss`

## 更新日志

### [v1.1.0（2020.10.8）](https://github.com/Aric-Sun/FirstKeyFrameFilter/releases/tag/v1.1.0)

- 修复 因为 indexOf 造成无法正确处理多英文句号文件名的问题。参见 [issue#2](https://github.com/Aric-Sun/FirstKeyFrameFilter/issues/2)
- 优化 提取获取无扩展名的文件名的方法进 Utils，方便调用
- 修复 调用 ffmpeg 切割视频被 PotPlayer 识别为音频的问题，**相对**兼容，遂版本号升至 1.1.0。参见 [issue#3](https://github.com/Aric-Sun/FirstKeyFrameFilter/issues/3)
- 优化 将时间戳转换为秒的方法转移到 Utils 下，方便调用，符合规范
- 优化 去除无用的import

### [v1.0.1（2020.10.4）](https://github.com/Aric-Sun/FirstKeyFrameFilter/releases/tag/v1.0.1)

- 修复 不能处理缺帧时长在 1s 内的情况，比如 0.15s。

### [v1.0.0（2020.9.5）](https://github.com/Aric-Sun/FirstKeyFrameFilter/releases/tag/v1.0.0)

一切的开始

#### 存在的问题

- 如果电脑上存在 *lib-apps* 中程序的环境变量，仍然不能删除此文件夹，详见 [issue#1](https://github.com/Aric-Sun/FirstKeyFrameFilter/issues/1)。

## 典型样例链接

-   [百度云](https://pan.baidu.com/s/15g8sDAWVIvRAYuHxmzCAgw)，提取码：7w1w
-   [OneDrive](https://mg001uolacnz-my.sharepoint.com/:f:/g/personal/2159_zaihua_istore_app/EnkzwmrNympBt5KdVJh2a7oBacx3D6o-Zy8L8cjhOFIJdg)

### 文件说明

1.  `TypicalFLV`是有问题的录播视频文件，未经处理，由[录播姬](https://rec.danmuji.org/)直出，会**持续更新**。一小部分未存在缺帧问题但有其他问题的文件也在其中，已特别标注。
2.  `TypicalXML`是由 fib（详见[项目依赖](#项目依赖)）分析生成的文件，是当时研发XML解析时留下的样例，现已不再更新。其中带有`TypicalCase`字样的文件来源于缺帧视频（即`TypicalFLV`）。
3.  `DanmakuXML`是[录播姬](https://rec.danmuji.org/)`1.1.20`新增的弹幕文件，将未压制投稿的弹幕文件存档于此，与本项目没什么关系……

## 开源协议

[GNU General Public License v3.0](https://github.com/Aric-Sun/FirstKeyFrameFilter/blob/master/LICENSE)