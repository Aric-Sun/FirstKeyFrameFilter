; 脚本由 Inno Setup 脚本向导 生成！
; 有关创建 Inno Setup 脚本文件的详细资料请查阅帮助文档！

#define MyAppName "FirstKeyFrameFilter"
#define MyAppVersion "1.1.0-20.10.8"
#define MyAppPublisher "zsunqian69@gmail.com"
#define MyAppURL "https://github.com/Aric-Sun/FirstKeyFrameFilter"
#define MyAppExeName "FirstKeyFrameFilter.exe"

[Setup]
; 注: AppId的值为单独标识该应用程序。
; 不要为其他安装程序使用相同的AppId值。
; (若要生成新的 GUID，可在菜单中点击 "工具|生成 GUID"。)
AppId={{A9292E10-0A84-4E75-B14B-E0E2229177EA}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
; 移除以下行，以在管理安装模式下运行（为所有用户安装）。
PrivilegesRequired=lowest
PrivilegesRequiredOverridesAllowed=commandline
OutputDir=E:\Aric Sun\自学技术\FirstKeyFrameFilter\out\artifacts\FirstKeyFrameFilter_jar
OutputBaseFilename=FirstKeyFrameFilter1.1.0-20.10.8_Setup
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "chinesesimp"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "E:\Aric Sun\自学技术\FirstKeyFrameFilter\out\artifacts\FirstKeyFrameFilter_jar\FirstKeyFrameFilter\FirstKeyFrameFilter.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "E:\Aric Sun\自学技术\FirstKeyFrameFilter\out\artifacts\FirstKeyFrameFilter_jar\FirstKeyFrameFilter\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; 注意: 不要在任何共享系统文件上使用“Flags: ignoreversion”

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"

