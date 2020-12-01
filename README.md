# FirstKeyFrameFilter

[简体中文 | Simplified Chinese](README_CN.md)

------

[TOC]

------



## Feature

Solve the problem of recording file [missing frame](#Problem description), eliminates the process of [manual operation](#Solution optimization)

## Background

### Problem Description

When I do live video recording, I often find that the beginning of the recorded video file is **missing frames**. The specific performance is: 

- On PotPlayer, the picture at the beginning of the video is still, but the sound continues.
- On MPV, the time frame automatically jumps to a few seconds later.
- When contributing to bilibili, if there is no secondary suppression which should be fully suppressed twice now, bilibili will remove the part without the picture and advance the subsequent pictures, resulting in a **serious out of sync of the sound and picture**. (The main impact. it does not affect the viewing experience much, because the player can automatically check the time-line.)

#### Triggering Conditions

Since I did not go to a large number of live broadcasts of other anchors to verify the universality of the problem, this project is not universal. According to my personal experience, this kind of problem is relatively small. It is very likely that only this one anchor often has this kind of problem, at least it hasn't happened in another anchor I recorded. My personal guess is that there is a problem with the host’s start-up operation, which has something to do with her not turning on the microphone every time she starts the broadcast, <u>maybe</u>. Anyway, it doesn’t look like the server’s or [BililiveRecorder](  https://rec.danmuji.org)'s problem.

- Anchor: [一只萌豌豆](https://live.bilibili.com/1123)
- Recording tools: [BililiveRecorder](https://rec.danmuji.org)，Version: 1.1.18

### Initial Solution

To solve this problem, the more brainless solution is to recode and suppress (Format Factory/Maruko's Toolbox), but this solution takes a long time and consumes a lot of resources, which is not suitable for my thin and light notebook. Non-destructive cutting is the solution that suits me. Using the `-ss` and `-c copy` parameters of FFmpeg, the missing frame can be **quickly** cut off (just cut the missing frame at the beginning, if there is a missing frame at the end, it will not cause the video, audio and picture of station B to be out of sync, and generally there is no), then the value of the `-ss` parameter is the time when the screen just started moving.

### Principle Explanation

The above sentence is easy to understand. Start wherever there is no problem, but this will involve another problem, that is, why the video will start to move at this value position, which needs to explain the principle of key frames. The video frame is offset according to the data of the previous frame, and the source is traced forward one by one, and the previous key frame will be found. If the key frame is not found, the player will not know how to deal with the frame data before the next key frame, which causes frame loss. The key frame here refers to the key frame of the original video, not the key frame (file header) automatically marked after the video is cut (the player will not recognize the key frame without the key frame at the beginning of the video). Then a cut on the non-key frames will inevitably cause the missing frames of the video, which refers to [cv4373238](https://www.bilibili.com/read/cv4373238).

### Advanced Solutions

In the beginning, I saw it with the naked eye, adding and subtracting in units of `1s` (a few tenths of a second is difficult to estimate with the naked eye). Sometimes it will be corrected by FFmpeg (The `-ss` parameter is to let FFmpeg find (seeking) keyframes **around here**, which refers to [FFmpeg official documentation](http://trac.ffmpeg.org/wiki/Seeking)), most of the time it will cause new missing frames, that is, FFmpeg did not find the key frame in a limited range. If it is earlier than the actual key frame, the duration of the missing frame will be a little shorter; if it is cut after the key frame, then the generated video will miss the frame to the next key frame. If you fine-tune it at this time, it will undoubtedly be a waste of time. So, how to get the accurate key frame position? With Genteure's fib (see [Project Dependence](#Project Dependency)), you can find the location of the **second** key frame in the parsed XML file, and then use FFmpeg to cut this location (Assign the second key frame time to the `-ss` parameter).

### Solution Optimization

If things go on like this, I gradually feel that it is a bit cumbersome to perform such operations every day. Let me count the steps in detail:

1. Use fib to analyze FLV and get XML. command-line e.g.：`fib parse test.flv out.xml`
2. Open the XML, search for the second keyframe attribute, find the time value of the same row
3. Video cutting of the resulting time using FFmpeg. command-line e.g.：`ffmpeg -i test.flv -ss 8.3 -c copy test-cut.flv`

It contains two questions:

1. Enter similar commands repeatedly every day, and if the tool does not add environment variables, you need to drag and drop the path into the command line, and the recorded file name is usually very long, and you need to drag and drop.
2. The size of the XML file depends on the size of the FLV file. For reasons of convenient management and fault tolerance, I set the automatic slicing duration of [BililiveRecorder](https://rec.danmuji.org/) to one hour. A live video recording with a bit rate of 4Mbps is about 1.78GB in an hour. In contrast, XML is at least about 28.5MB. Therefore, when using Sublime to open XML, there will be a longer loading time (Large IDEs can be loaded in read-only mode in seconds, but my use case is not suitable for opening an IDE, just look at an XML),  and the screen is also very stuck when scrolling.

So, how to combine these two tools through programming, avoid the tedious operation of repeatedly typing commands, hide the intermediate steps, and improve the reading speed of large-volume XML?  This led to the birth of this project.

## Requirements

Most (almost all) operating environments of this project have been packaged into compressed packages and Setup, just pay attention to the operating system.

-   Windows (Currently) 
-   JDK (Optional)  `1.8`(`jar`) / `1.6+`(`exe`) ，See [Assets description](#Assets description) here for details

## installation
### Assets

#### File List

```
FirstKeyFrameFilter.exe
FirstKeyFrameFilter.jar
FirstKeyFrameFilter{version-code}-Full.zip
FirstKeyFrameFilter{version-code}-Full_Setup.exe
FirstKeyFrameFilter{version-code}-Without_JRE.zip
```

#### Assets Description

-   Because the project relies on two exe executable files and the JDK environment, the program is too large, and the actual core code only occupies a relatively small part.
But basically, neither the dependent `exe` nor the JDK environment will change.  So please **directly download** `FirstKeyFrameFilter.exe` in subsequent updates to overwrite the old version.

-   The single `FirstKeyFrameFilter.exe` cannot work normally, and at least the `lib-apps` folder and two `exe`s under it must exist in the same directory. The error message is redirected to the `error.log` in the current directory. If the program does not output or the black box flashes by, or other abnormalities, please refer to the `log` file first.

-   The `jar` file is uploaded for possible cross-platform programs in the future. How to use (command line):`java -jar FirstKeyFrameFilter.jar {flvFile}`。
    Note that you must run `jar` with the same JDK version as the compiled version (`1.8`), otherwise it will appear error: `java.lang.UnsupportedClassVersionError: Main : Unsupported major.minor version 52.0`. The `exe` program does not have this problem.
    
-   The files with the word `Full` contain the complete dependency `exe` and JDK environment.

-   If there is already a JDK environment (>=`1.6`) on the computer, that is, the environment variables are accessible, you can delete the `jre1.8.0_161` folder in the program directory, or download `~Without_JRE.zip`.

### installation Steps

1.  Download the `zip` compressed package or `~Setup.exe`, if the JDK version is greater than `1.6`, you can download `~Without_JRE.zip`
2.  Unzip directly or follow the installation wizard to install
-   Directly download `FirstKeyFrameFilter.exe` to cover when updating
-   If you patiently read the above [Assets Description](#Assets Description), you will find that this is just a summary

### Directory Structure

The program must be placed in the following structure to ensure the relative position, otherwise, it cannot run.

```
FirstKeyFrameFilter
│ FirstKeyFrameFilter.exe
│
├─jre1.8.0_161 (Optional) 
│
└─lib-apps
        ffmpeg.exe
        FlvInteractiveRebase.exe
```

## Usage

1.  Drag File(s)

    Drag one or **multiple** FLV files into `FirstKeyFrameFilter.exe` to run, which is the <u>easiest and direct method</u>

2.  Command Line

    `FirstKeyFrameFilter.exe test.flv`。Among them, `test.flv` is the pending [missing frame](#Problem description) FLV video file

## Project Dependency

+ FlvInteractiveRebase (fib，FLV File editing tool) 

  + [BililiveRecorder QQ Group](https://jq.qq.com/?_wv=1027&k=pJMpD57V) internal beta 1
  + By genteure ( fib-beta@danmuji.org )
  + Software core functions:
      - Tag level editing of FLV can be used to solve all kinds of strange problems
      - Use a simple text file as the command format for manual editing...
      - ...At the same time, it is convenient to write scripts and realize automatic processing
      - The command file is relatively small and easy to transfer, which is convenient for people who do not understand the details of FLV to repair the file
  + Main software usage (Assuming the software file name is fib) ：
      1. **Read FLV files and generate command files** (This project depends on)   
         `./fib parse problem.flv command.xml`
      2. Read other FLV files for reference or copy data  
         `./fib parse other.flv Other-commands.xml`
      3. Manually edit command text files
      4. Use the modified command file to generate a new FLV file  
         `./fib build command.xml after-repair.flv`
      - See help please run  
         `./fib help`
  
+ [FFmpeg](https://ffmpeg.org/)
  
  + Mainly used for FLV lossless cutting

## Support

-   Go to [Issues](https://github.com/Aric-Sun/FirstKeyFrameFilter/issues) to post a new question.
-   Email me: zsunqian69@gmail.com.

## Development

Before development, you need:

-   IntelliJ IDEA 2019.3.3 or other Java IDE
-   JDK 1.6+，Preferably 1.8

Three modules are called by `src/Main.java` to complete the whole work, they are

1.  Call `fib`: read FLV and generate XML
2.  StAX parsing XML: time to read the second key frame
3.  Call `FFmpeg`: Cut the FLV with the received time as the start time, generate a new file in the same folder, and the source file will not be deleted

If you want to study the source code of this project or modify the functions, you can start from the entry of the above three modules:

1.  `src/XMLGenerator/FlvInteractiveRebase.java`
2.  `src/XMLParsing/StAX/XMLReader.java`
3.  `src/FLVCutter/FastForwardMpeg.java`

### How to Pack

1.  Packaged into `jar`: Each IDE has corresponding functions, and the specific methods are self-checked.
2.  Package into `exe`: use exe4j Wizard, see the configuration file `/exe4jConfigFile.exe4j`
3.  Packaged into Setup: Use Inno Setup, see the configuration file`/InnoSetupConfig.iss`

## Changelog

### [v1.1.0 (2020.10.8) ](https://github.com/Aric-Sun/FirstKeyFrameFilter/releases/tag/v1.1.0)

- repair: Because of `indexOf`, the file name with multiple English periods cannot be handled correctly.  See [issue#2](https://github.com/Aric-Sun/FirstKeyFrameFilter/issues/2)
- optimization: Extract the method of obtaining the file name without extension into `Utils` for easy calling
- repair: Calling FFmpeg to cut the video is recognized by PotPlayer as an audio problem, which is **relatively** compatible, so the version number is increased to 1.1.0.  See [issue#3](https://github.com/Aric-Sun/FirstKeyFrameFilter/issues/3)
- optimization: The method of converting timestamp to seconds is transferred to `Utils`, which is convenient to call and conforms to the specification
- optimization. Remove useless `import`

### [v1.0.1 (2020.10.4) ](https://github.com/Aric-Sun/FirstKeyFrameFilter/releases/tag/v1.0.1)

- repair: It cannot handle the case where the missing frame duration is within 1s, such as 0.15s.

### [v1.0.0 (2020.9.5) ](https://github.com/Aric-Sun/FirstKeyFrameFilter/releases/tag/v1.0.0)

The beginning of everything

#### Problem

- If the environment variables of the program in *lib-apps* exist on the computer, this folder still cannot be deleted, see [issue#1](https://github.com/Aric-Sun/FirstKeyFrameFilter/issues/1) for details.

## Typical Sample Link

-   [Baidu Net Disk](https://pan.baidu.com/s/15g8sDAWVIvRAYuHxmzCAgw), Extraction code: 7w1w
-   [OneDrive](https://mg001uolacnz-my.sharepoint.com/:f:/g/personal/2159_zaihua_istore_app/EnkzwmrNympBt5KdVJh2a7oBacx3D6o-Zy8L8cjhOFIJdg)

### File Description

1.  `TypicalFLV` is the recorded video file in question. It is unprocessed and will be exported directly from [BililiveRecorder](https://rec.danmuji.org/) and will be **continuously updated**.  A small number of files that do not have missing frames but have other problems are also included and have been specially marked.
2.  `TypicalXML` are files generated by fib (see [Project Dependency](#Project Dependency)) analysis, which is a sample left over from the development of XML parsing and is no longer updated. The files with the word `TypicalCase` in them come from the missing frame video (i.e. `TypicalFLV`).
3.  `DanmakuXML` are newly added danmaku files in [BililiveRecorder](https://rec.danmuji.org/) 1.1.20. The danmaku files that have not been suppressed and submitted are archived here, which has nothing to do with this project...

## Open Source Agreement

[GNU General Public License v3.0](https://github.com/Aric-Sun/FirstKeyFrameFilter/blob/master/LICENSE)