package dto;

/**
 * Flv֡��ʵ���࣬һ�������Ӧһ��Tag
 * @author AricSun
 * @date 2020.08.13 2:03
 */
/*
ժ��XML�ļ�����XML�ļ�������ܲ����ϴ������ڴ�ճ��
�ļ�˵��

�� XML �ļ������ fib (FlvInteractiveRebase) �������ļ�
����ʹ�� ./fib build �����ݱ��ļ��е��������� FLV �ļ�
�����Ҫ���䱾�ļ���ǿ���Ƽ�ѹ����ѹ�������������������ļ���С

������ Tag �У�

Command �ǶԴ� Tag ִ�еĲ�������ѡ��
- D, Drop ������Ҳ����ֱ��ɾ������ Tag element
- P, Pick ʹ��
- I, Inline �� HEX �ַ������� Tag  TIP: ������ ./fib extract �������·��ʱ�����н��
- S, Script �� Script Tag JSON ���� Tag

Index �� Tag ��ԭ FLV �ļ��е���ţ�ע�� *����* �����ļ��е���š�

TimeStamp �� Tag �����ļ��л�ʹ�õ�ʱ�������ʽ�� ISO 8601 �� Durations
https://en.wikipedia.org/wiki/ISO_8601#Durations

From ��ʹ fib ����һ�� FLV �л�ȡ Tag ���������ʾ����
Raw ��ʹ fib ����һ���ļ��ж�ȡһ�������� Tag ���������ʾ����

ֻ����������������������� FLV �ļ�����Ӱ��
��������������Ϊչʾ�ã���Ӱ����������ļ�

Type �� Tag ������ (Script, Audio, Video)
Size �� Tag �����ݴ�С�������� 11 �ֽ� Tag Header �Ĵ�С
Offset �� Tag ��ԭ�ļ��п�ʼ��λ��

Header Ϊ true ʱ˵�����Ǹ� Header Tag
Keyframe Ϊ true ʱ˵�����Ǹ��ؼ�֡

ʾ����
<Tag Command="Pick" Index="0" TimeStamp="PT0S" Type="Script" Size="1094" Offset="13" />
<Tag Command="Drop" Index="0" TimeStamp="PT0S" Type="Script" Size="1094" Offset="13" />
<Tag Command="D" Index="0" TimeStamp="PT0S" Type="Script" Size="1094" Offset="13" />
<Tag Command="Pick" Index="2" TimeStamp="PT0S" Type="Video" Size="34" Offset="1160" Keyframe="true" Header="true" />
<Tag Command="P" Index="1" TimeStamp="PT0S" From="D:\��Ƶ\��һ����Ƶ�ļ�.flv" />
<Tag Command="P" Index="0" TimeStamp="PT0S" Raw="D:\��Ƶ\һ��������Tag.blob" />
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
     * function: ����޲�����µ�Ĭ��ֵ��
     * �Է����пգ�null��������쳣
     * -2���������·���set������
     * �����Ŵ�
     * һ��ͨ��set������ֵ��
     * ��û���趨���εĹ��췽��
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
        this.header = "False";  // ע�������set��������falseȫСд
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = null!=command?command:"Null";
        // ���п��߼�������ʵ���෽���У�
        // ��������λ�õ������ж�
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
                // ��ͷ�����Ǹ�ʽ����Ҫ
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
