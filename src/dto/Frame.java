package dto;

/**
 * Flv帧的实体类，一个对象对应一个Tag
 * @author AricSun
 * @date 2020.08.13 2:03
 */
/*
摘自XML文件，因XML文件过大可能不会上传，故在此粘贴
文件说明

本 XML 文件是软件 fib (FlvInteractiveRebase) 的命令文件
可以使用 ./fib build 来根据本文件中的内容生成 FLV 文件
如果需要传输本文件，强烈推荐压缩成压缩包，可以显著降低文件大小

在下面 Tag 中：

Command 是对此 Tag 执行的操作，可选：
- D, Drop 丢弃，也可以直接删除整个 Tag element
- P, Pick 使用
- I, Inline 用 HEX 字符串生成 Tag  TIP: 内容是 ./fib extract 不带输出路径时的运行结果
- S, Script 用 Script Tag JSON 生成 Tag

Index 是 Tag 在原 FLV 文件中的序号，注意 *不是* 在新文件中的序号。

TimeStamp 是 Tag 在新文件中会使用的时间戳，格式是 ISO 8601 的 Durations
https://en.wikipedia.org/wiki/ISO_8601#Durations

From 会使 fib 从另一个 FLV 中获取 Tag （见下面的示例）
Raw 会使 fib 从另一个文件中读取一个独立的 Tag （见下面的示例）

只有上述五个参数会对输出的新 FLV 文件产生影响
下面的五个参数均为展示用，不影响输出的新文件

Type 是 Tag 的类型 (Script, Audio, Video)
Size 是 Tag 的数据大小，不包括 11 字节 Tag Header 的大小
Offset 是 Tag 在原文件中开始的位置

Header 为 true 时说明这是个 Header Tag
Keyframe 为 true 时说明这是个关键帧

示例：
<Tag Command="Pick" Index="0" TimeStamp="PT0S" Type="Script" Size="1094" Offset="13" />
<Tag Command="Drop" Index="0" TimeStamp="PT0S" Type="Script" Size="1094" Offset="13" />
<Tag Command="D" Index="0" TimeStamp="PT0S" Type="Script" Size="1094" Offset="13" />
<Tag Command="Pick" Index="2" TimeStamp="PT0S" Type="Video" Size="34" Offset="1160" Keyframe="true" Header="true" />
<Tag Command="P" Index="1" TimeStamp="PT0S" From="D:\视频\另一个视频文件.flv" />
<Tag Command="P" Index="0" TimeStamp="PT0S" Raw="D:\视频\一个独立的Tag.blob" />
 */
public class Frame {
    private String command;  // Command="Pick"
    private String index;  // Index="1"
    private String timeStamp;  // TimeStamp="PT0S"
    private String type;  // Audio/Video
    private String size;  // Size="4"
    private String offset; // Offset="590"
    private String keyframe; // Keyframe="true"
    private String header; // Header="true"

    /*
     * function: 针对无参情况下的默认值，
     * 以防被判空（null）而造成异常
     * -2是区别于下方的set方法，
     * 易于排错
     * 一般通过set方法赋值，
     * 故没有设定带参的构造方法
     * @Param: []
     * @Return:
     */
    public Frame() {
        this.command = "unknown";
        this.index = "-2";
        this.timeStamp = "PT-2S";
        this.type = "unknown";
        this.size = "-2";
        this.offset = "-2";
        this.keyframe = "False";
        this.header = "False";  // 注意下面的set方法里是false全小写
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = null!=command?command:"Null";
        // 降判空逻辑提升至实体类方法中，
        // 减少其它位置的冗余判断
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = null!=index?index:"-1";
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = null!=timeStamp?timeStamp:"PT-1S";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type =  null!=type?type:"Null";
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = null!=size?size:"-1";
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = null!=offset?offset:"-1";
    }

    public String getKeyframe() {
        return keyframe;
    }

    public void setKeyframe(String keyframe) {
        this.keyframe = null!=keyframe?keyframe:"false";
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = null!=header?header:"false";
    }

    @Override
    public String toString() {
        return '\n' +
                // 开头换行是格式化需要
                "Frame{" +
                "command='" + command + '\'' +
                ", index='" + index + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                ", offset='" + offset + '\'' +
                ", keyframe='" + keyframe + '\'' +
                ", header='" + header + '\'' +
                '}';
    }
}
