@echo off
echo 正在下载图书封面图片...
echo.

set ISBN_LIST=978-7-111-68451-2 978-7-115-58168-4 978-7-302-58169-1 978-7-02-000220-7 978-7-5321-5432-7 978-7-5086-8721-6 978-7-111-53569-0 978-7-100-17010-5 978-7-121-38901-0 978-7-5086-9823-6
set NAME_LIST=java-core spring-boot algorithm hongloumeng huozhe sapiens csapp 100-years mysql principles
set COVERS_DIR=src\main\resources\static\images\covers

setlocal enabledelayedexpansion

for %%i in (0 1 2 3 4 5 6 7 8 9) do (
  for /f "tokens=%%i delims= " %%a in ("%ISBN_LIST%") do set isbn_%%i=%%a
  for /f "tokens=%%i delims= " %%b in ("%NAME_LIST%") do set name_%%b=%%b
)

echo 请选择下载方式:
echo 1. 从京东(JD.com)获取封面
echo 2. 使用美观的占位图片(picsum.photos)
echo.
set /p choice="请输入 1 或 2: "

if "%choice%"=="1" (
  echo 正在从京东搜索封面...
  echo 提示: 需先安装 curl, 然后手动查找每本书的封面URL
  echo 建议直接访问 jd.com 搜索每本书的ISBN
)
if "%choice%"=="2" (
  echo 下载占位封面...
  REM Download using curl
  curl -L -o %COVERS_DIR%\java-core.jpg https://picsum.photos/seed/java-core/400/500
  curl -L -o %COVERS_DIR%\spring-boot.jpg https://picsum.photos/seed/spring-boot/400/500
  curl -L -o %COVERS_DIR%\algorithm.jpg https://picsum.photos/seed/algorithm/400/500
  curl -L -o %COVERS_DIR%\hongloumeng.jpg https://picsum.photos/seed/hongloumeng/400/500
  curl -L -o %COVERS_DIR%\huozhe.jpg https://picsum.photos/seed/huozhe/400/500
  curl -L -o %COVERS_DIR%\sapiens.jpg https://picsum.photos/seed/sapiens/400/500
  curl -L -o %COVERS_DIR%\csapp.jpg https://picsum.photos/seed/csapp/400/500
  curl -L -o %COVERS_DIR%\100-years.jpg https://picsum.photos/seed/100-years/400/500
  curl -L -o %COVERS_DIR%\mysql.jpg https://picsum.photos/seed/mysql/400/500
  curl -L -o %COVERS_DIR%\principles.jpg https://picsum.photos/seed/principles/400/500
  echo 下载完成！
)
endlocal
pause
