# FirstKeyFrameFilter

## 项目背景

在我做直播录像的时候，经常会发现录下来的视频文件开头部分**缺帧**，具体表现为：

- 在PotPlayer上，视频开头部分画面是不动的，但是有声音。
- 在mpv上，时间帧自动跳到几秒后。
- 投稿到B站上，如果没有二压的话（现在应该全面二压了），会把没有画面的部分剔除掉，后面的画面提前，从而造成**音画严重不同步**。（主要影响，因为播放器是可以自动校对时间轴的，不怎么影响观看体验）

针对此问题，比较无脑的方案是重新转码压制（格式工厂/小丸工具箱），但这个方案占用时间长，资源消耗多，相当不适合我这种轻薄本。无损切割才是适合我的方案，利用ffmpeg的`-ss`和`-c copy`参数可以将缺帧的部分**快速**切除掉（只需切除开头的缺帧，如果末尾有缺帧是不会造成B站视频音画不同步的，而且一般也没有），那么这个`-ss`参数的值就是画面刚开始动的时间。

上面这句话很好理解，哪里没问题了就从哪里开始，但这会牵扯到另外的问题，就是在这个数值的位置上为什么视频会开始动，这就要解释一下关键帧的原理了。视频帧是根据上一帧的数据进行画面偏移（offset）的，一个一个往前溯源，最终会找到前面的一个关键帧。如果找不到关键帧，播放器会不知道该怎么处理到下一个关键帧之前的帧数据，这就造成了缺帧。这里的关键帧指的是原始视频的关键帧，而不是视频切割后自动打上的关键帧（文件头）（视频开头没有关键帧播放器是认不出来的）。那么在非关键帧上切一刀，必然会造成视频的缺帧。参考[cv4373238](https://www.bilibili.com/read/cv4373238)。

一开始的时候，我是肉眼看的，以1s为单位不断加减（零点几秒肉眼难以估算），有的时候会被ffmpeg纠正（`-ss`参数是让ffmpeg在此处**周围**寻找（seeking）关键帧，参考[ffmpeg官方文档](http://trac.ffmpeg.org/wiki/Seeking)），大部分时候会造成新的缺帧，就是ffmpeg在有限的范围内并没有找到关键帧，如果比实际的关键帧提前了，那么缺帧的时长会短那么一点点；如果切在关键帧后，那么生成的视频会缺帧到下一个关键帧。此时再微调的话，无疑是很浪费时间的，那么，如何获得准确的关键帧位置呢？借由genteure的fib（详见下面环境依赖），可以在其解析出的xml文件中找到**第二个**关键帧的位置，然后用ffmpeg对此位置进行切割（将第二个关键帧时间赋值给`-ss`参数）。长此以往，我逐渐觉得，每天进行这样的操作有些繁琐，细数一下操作的步骤：

1. 使用fib分析flv，得到xml，命令行e.g.：`fib parse test.flv out.xml`
2. 打开xml搜索第二个keyframe属性，找到同一行的时间值
3. 使用ffmpeg对得到的时间进行视频切割，命令行e.g.：`ffmpeg -i test.flv -ss 8.3 -c copy test-cut.flv`

其中包含两个问题：

1. 每天重复输入相似的命令，而且工具没有加入环境变量的话，需要通过拖拽把路径拉进cmd，而且录播的文件名通常很长，也需要拖拽
2. xml文件大小随flv文件大小而定，基于方便管理和容错的原因，设置了[录播姬](https://rec.danmuji.org/)的自动切片时长为一个小时，一个4Mbps码率的直播录像，一个小时大概有1.78GB左右大小，相对的，xml也起码有28.5MB左右。所以，使用sublime打开xml时，会有一个较长的加载时间（大型ide可以以只读模式秒加载，但是我这种使用场景不适合开一个ide只用来看一个xml），而且滚动屏幕的时候也十分的卡顿

那么，如何通过编程将这两个工具联合起来，免去重复打命令的繁琐操作，隐去中间步骤，提高大体积xml读取速度呢？这就导致了本项目的诞生。

## 环境依赖

+ FlvInteractiveRebase（fib，FLV 文件编辑工具）

  + [录播姬QQ群](https://jq.qq.com/?_wv=1027&k=pJMpD57V)内部测试版 beta 1

  + By genteure ( fib-beta@danmuji.org )

  + 软件核心功能：

      - 对 FLV 进行 Tag 级别编辑处理，可用于解决各类奇葩问题
      - 使用简单的文本文件作为命令格式，方便手工编辑...
      - ...同时也方便编写脚本，实现自动化处理
      - 命令文件相对更小易于传输，方便协助不太懂 FLV 细节的人修复文件

    + 软件主要用法：

      - 注：假设软件文件名为 fib	

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

## 目录结构描述

需按以下结构放置程序，保证相对位置，否则无法运行。

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

