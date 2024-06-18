set destination=C:\Users\USER\College\S4\Web_Dynamique\Frameworks\winter\Fw\compiled
set nomJar=winter-1
set nomSrcTxt=C:\Users\USER\College\S4\Web_Dynamique\Frameworks\winter\Fw\winter-framework\java_files_list.txt
set lib=C:\apache-tomcat-10.1.23\apache-tomcat-10.1.23\lib\servlet-api.jar

if exist %destination% (
    del %destination%"\*" /F /Q
)
call .\findJavaSrc
set src=
for /f "delims=" %%i in (%nomSrcTxt%) do set src=%src% %%i

javac -parameters -cp "%lib%" -d %destination% %src% 

cd /d %destination%  && jar -cvf %nomJar%.jar *
if exist %nomSrcTxt% (
    del %nomSrcTxt%
)
cmd /k