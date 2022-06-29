# Works on window

```shell
&"~\.konan\dependencies\llvm-11.1.0-windows-x64-essentials/bin/clang++" -std=c++17  -target x86_64-pc-windows-gnu -c .\webview.cc -I../WebView2Loader_dl
l -L..\WebView2Loader_dll -l"WebView2Loader.dll" -lole32 -lshell32 -lshlwapi -luser32 -o webview.o
&"~\.konan\dependencies\llvm-11.1.0-windows-x64-essentials/bin/llvm-ar" -rv libwebview.a webview.o

gradle runReleaseExecutableNative

# Then you'll get lld-link: error: undefined symbol: std::__throw_bad_array_new_length()
# In `~\.konan\dependencies`, replace `msys2-mingw-w64-x86_64-1` and `llvm-11.1.0-windows-x64-essentials` with the latest (14.0.5) version of Clang
# junction ./msys2-mingw-w64-x86_64-1 ~\scoop\apps\msys2\current\mingw64

gradle runDebugExecutableNative

# Then you'll get a Task :runDebugExecutableNative FAILED

cp src/nativeMain/nativeInterop/cinterop/WebView2Loader_dll/WebView2Loader.dll build/bin/native/debugExecutable 
gradle runDebugExecutableNative
```