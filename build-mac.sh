#!/bin/bash
c++ -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin -dynamiclib  src_c/webview.cc -o src/main/resources/libwebview.dylib -DWEBVIEW_COCOA=1 -framework WebKit -DOBJC_OLD_DISPATCH_PROTOTYPES=1 -std=c++11
