@echo off
REM Script de teste automatizado para Windows
REM Testa se as modificações não quebraram nada

echo =========================================
echo   TESTE AUTOMATIZADO - ANIMALYS
echo =========================================
echo.

REM Variáveis de cor (se suportado)
set GREEN=[92m
set RED=[91m
set YELLOW=[93m
set NC=[0m

REM Contador de testes
set PASSED=0
set FAILED=0

echo 1. TESTANDO COMPILACAO...
echo -----------------------------------------
echo Executando: mvn clean compile...
mvn clean compile
if %ERRORLEVEL% EQU 0 (
    echo.
    echo %GREEN%[PASS]%NC% Compilacao bem-sucedida
    set /a PASSED+=1
) else (
    echo.
    echo %RED%[FAIL]%NC% Erro na compilacao (veja mensagens acima)
    set /a FAILED+=1
    echo.
    echo Pressione qualquer tecla para sair...
    pause >nul
    goto :END
)
echo.

echo 2. TESTANDO BUILD DO PROJETO...
echo -----------------------------------------
echo Executando: mvn package (sem recompilar)...
mvn package -DskipTests -Dmaven.compiler.skip=true -q
if %ERRORLEVEL% EQU 0 (
    echo.
    echo %GREEN%[PASS]%NC% Build bem-sucedido
    set /a PASSED+=1
) else (
    echo.
    echo %RED%[FAIL]%NC% Erro no build (veja mensagens acima)
    set /a FAILED+=1
    echo.
    echo Pressione qualquer tecla para sair...
    pause >nul
    goto :END
)
echo.

echo 3. VERIFICANDO ARQUIVO WAR...
echo -----------------------------------------
if exist "target\animalys-1.0.0-SNAPSHOT.war" (
    echo %GREEN%[PASS]%NC% Arquivo WAR gerado
    set /a PASSED+=1
    for %%A in (target\animalys-1.0.0-SNAPSHOT.war) do echo        Tamanho: %%~zA bytes
) else (
    echo %RED%[FAIL]%NC% Arquivo WAR nao encontrado
    set /a FAILED+=1
)
echo.

echo 4. PROCURANDO CAMINHOS HARDCODED DO WINDOWS...
echo -----------------------------------------
set FOUND_HARDCODED=0

REM Procurar por C:\ em arquivos Java
findstr /s /i /m "C:\\\\" src\main\java\*.java > nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo %RED%[FAIL]%NC% Encontrados caminhos hardcoded C:\\ em:
    findstr /s /i /n "C:\\\\" src\main\java\*.java
    set /a FAILED+=1
    set FOUND_HARDCODED=1
)

REM Procurar por C:/ em arquivos Java (exceto comentários)
findstr /s /i /m "C:/" src\main\java\*.java > nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo %YELLOW%[WARN]%NC% Encontrados caminhos C:/ (verificar se sao comentarios):
    findstr /s /i /n "C:/" src\main\java\*.java
)

if %FOUND_HARDCODED% EQU 0 (
    echo %GREEN%[PASS]%NC% Nenhum caminho hardcoded encontrado
    set /a PASSED+=1
)
echo.

echo 5. VERIFICANDO File.separator NO CODIGO...
echo -----------------------------------------
findstr /s /i /m "File.separator" src\main\java\*.java > nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo %GREEN%[PASS]%NC% File.separator encontrado (multiplataforma OK)
    set /a PASSED+=1
    findstr /s /i /c:"File.separator" src\main\java\*.java | find /c "File.separator"
) else (
    echo %YELLOW%[WARN]%NC% File.separator nao encontrado
)
echo.

echo 6. VERIFICANDO System.getProperty("user.home")...
echo -----------------------------------------
findstr /s /i /m "user.home" src\main\java\*.java > nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo %GREEN%[PASS]%NC% user.home encontrado (multiplataforma OK)
    set /a PASSED+=1
) else (
    echo %RED%[FAIL]%NC% user.home nao encontrado
    set /a FAILED+=1
)
echo.

echo 7. VERIFICANDO DIRETORIOS DE UPLOAD...
echo -----------------------------------------
if not exist "%USERPROFILE%\animalys" (
    echo %YELLOW%[INFO]%NC% Criando diretorios de teste...
    mkdir "%USERPROFILE%\animalys\documentos" 2>nul
    mkdir "%USERPROFILE%\animalys\fotos" 2>nul
    mkdir "%USERPROFILE%\animalys\receitas" 2>nul
)

if exist "%USERPROFILE%\animalys\documentos" (
    echo %GREEN%[PASS]%NC% Diretorio documentos OK: %USERPROFILE%\animalys\documentos
    set /a PASSED+=1
) else (
    echo %RED%[FAIL]%NC% Diretorio documentos nao existe
    set /a FAILED+=1
)

if exist "%USERPROFILE%\animalys\fotos" (
    echo %GREEN%[PASS]%NC% Diretorio fotos OK: %USERPROFILE%\animalys\fotos
) else (
    echo %RED%[FAIL]%NC% Diretorio fotos nao existe
)

if exist "%USERPROFILE%\animalys\receitas" (
    echo %GREEN%[PASS]%NC% Diretorio receitas OK: %USERPROFILE%\animalys\receitas
) else (
    echo %RED%[FAIL]%NC% Diretorio receitas nao existe
)
echo.

echo 8. VERIFICANDO PERMISSOES DE ESCRITA...
echo -----------------------------------------
echo teste > "%USERPROFILE%\animalys\documentos\teste.txt" 2>nul
if exist "%USERPROFILE%\animalys\documentos\teste.txt" (
    echo %GREEN%[PASS]%NC% Permissao de escrita OK
    set /a PASSED+=1
    del "%USERPROFILE%\animalys\documentos\teste.txt" 2>nul
) else (
    echo %RED%[FAIL]%NC% Sem permissao de escrita
    set /a FAILED+=1
)
echo.

echo 9. VERIFICANDO WEB.XML...
echo -----------------------------------------
if exist "src\main\webapp\WEB-INF\web.xml" (
    findstr /i /c:"user.home" "src\main\webapp\WEB-INF\web.xml" > nul 2>&1
    if %ERRORLEVEL% EQU 0 (
        echo %GREEN%[PASS]%NC% web.xml atualizado com ${user.home}
        set /a PASSED+=1
    ) else (
        echo %RED%[FAIL]%NC% web.xml nao usa ${user.home}
        set /a FAILED+=1
    )
) else (
    echo %RED%[FAIL]%NC% web.xml nao encontrado
    set /a FAILED+=1
)
echo.

echo 10. VERIFICANDO CLASSES PRINCIPAIS...
echo -----------------------------------------
set CLASSES_OK=0

if exist "target\classes\br\gov\pr\guaira\animalys\util\FileUploadUtil.class" (
    echo %GREEN%[OK]%NC% FileUploadUtil.class compilada
    set /a CLASSES_OK+=1
)

if exist "target\classes\br\gov\pr\guaira\animalys\servlet\FotoServlet.class" (
    echo %GREEN%[OK]%NC% FotoServlet.class compilada
    set /a CLASSES_OK+=1
)

if exist "target\classes\br\gov\pr\guaira\animalys\service\FotoService.class" (
    echo %GREEN%[OK]%NC% FotoService.class compilada
    set /a CLASSES_OK+=1
)

if %CLASSES_OK% EQU 3 (
    echo %GREEN%[PASS]%NC% Todas as classes principais compiladas
    set /a PASSED+=1
) else (
    echo %RED%[FAIL]%NC% Algumas classes nao foram compiladas
    set /a FAILED+=1
)
echo.

:END
echo =========================================
echo   RESULTADO DOS TESTES
echo =========================================
echo.
echo Total de testes executados: 10
echo %GREEN%Testes aprovados:%NC% %PASSED%
if %FAILED% GTR 0 (
    echo %RED%Testes falhados:%NC% %FAILED%
) else (
    echo Testes falhados: %FAILED%
)
echo.

if %FAILED% EQU 0 (
    echo %GREEN%=========================================%NC%
    echo %GREEN%  TODOS OS TESTES PASSARAM!%NC%
    echo %GREEN%  Projeto pronto para deploy no Linux!%NC%
    echo %GREEN%=========================================%NC%
    exit /b 0
) else (
    echo %RED%=========================================%NC%
    echo %RED%  ALGUNS TESTES FALHARAM!%NC%
    echo %RED%  Corrija os problemas antes do deploy!%NC%
    echo %RED%=========================================%NC%
    exit /b 1
)
