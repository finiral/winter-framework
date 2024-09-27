setlocal enabledelayedexpansion

:: Demande à l'utilisateur de saisir le chemin du dossier source
set SRC_FOLDER=lib\

:: Nom du fichier où les chemins seront enregistrés
set OUTPUT_FILE=lib_files.txt
if exist %OUTPUT_FILE% (
    del lib_files.txt
)

:: Vérifie si le dossier existe
if not exist "%SRC_FOLDER%" (
    echo Le dossier specifie n'existe pas.
    goto End
)
:: Initialiser la variable ALL_PATHS
set "ALL_PATHS="

:: Cherche tous les fichiers .jar dans le dossier donné et sous-dossiers
echo Listing des fichiers .jar dans %SRC_FOLDER% et sous-dossiers:
for /R "%SRC_FOLDER%" %%f in (*.jar) do (
    echo %%f
    if defined ALL_PATHS (
        set "ALL_PATHS=!ALL_PATHS!;%%f"
    ) else (
        set "ALL_PATHS=%%f"
    )
)
:: Écrit tous les chemins sur une seule ligne dans le fichier
echo !ALL_PATHS! > %OUTPUT_FILE%

echo Les chemins des fichiers ont été sauvegardés dans %OUTPUT_FILE% sur une seule ligne.

:End
endlocal
