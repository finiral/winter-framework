set destination=C:\Users\USER\College\S4\Web_Dynamique\Frameworks\winter\Fw\compiled
set nomJar=winter-1
set nomSrcTxt=java_files_list.txt
set nomLibTxt=lib_files.txt
set lib=lib

:: Si le répertoire de destination existe, supprimer tous les fichiers à l'intérieur
if exist %destination% (
    del %destination%\* /F /Q
)

call .\findJavaSrc

:: Charger tous les fichiers sources Java dans la variable src
set src=
for /f "delims=" %%i in (%nomSrcTxt%) do set src=%src% %%i

:: Compilation des fichiers Java
javac -parameters -cp "%lib%\*" -d %destination% %src%

:: Création du fichier JAR dans le répertoire de destination
cd /d %destination% && jar -cvf %nomJar%.jar *

:: Suppression du fichier liste de sources si il existe
if exist %nomSrcTxt% (
    del %nomSrcTxt%
)

:: Garder la fenêtre de commande ouverte
cmd /k
