; �ű��� Inno Setup �ű��� ���ɣ�
; �йش��� Inno Setup �ű��ļ�����ϸ��������İ����ĵ���

#define MyAppName "FirstKeyFrameFilter"
#define MyAppVersion "1.0.1-20.10.4"
#define MyAppPublisher "zsunqian69@gmail.com"
#define MyAppURL "https://github.com/Aric-Sun/FirstKeyFrameFilter"
#define MyAppExeName "FirstKeyFrameFilter.exe"

[Setup]
; ע: AppId��ֵΪ������ʶ��Ӧ�ó���
; ��ҪΪ������װ����ʹ����ͬ��AppIdֵ��
; (��Ҫ�����µ� GUID�����ڲ˵��е�� "����|���� GUID"��)
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
; �Ƴ������У����ڹ���װģʽ�����У�Ϊ�����û���װ����
PrivilegesRequired=lowest
PrivilegesRequiredOverridesAllowed=commandline
OutputDir=D:\Downloads\FirstKeyFrameFilter\out\artifacts\FirstKeyFrameFilter_jar
OutputBaseFilename=FirstKeyFrameFilter1.0.1-20.10.4_Setup
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "chinesesimp"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "D:\Downloads\FirstKeyFrameFilter\out\artifacts\FirstKeyFrameFilter_jar\FirstKeyFrameFilter\FirstKeyFrameFilter.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "D:\Downloads\FirstKeyFrameFilter\out\artifacts\FirstKeyFrameFilter_jar\FirstKeyFrameFilter\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; ע��: ��Ҫ���κι���ϵͳ�ļ���ʹ�á�Flags: ignoreversion��

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"

