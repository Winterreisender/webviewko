var ffi = require('ffi-napi');

var lib = ffi.Library('win32-x86-64/webview', {
  'webview_create': [ 'pointer', [ 'int' ] ]
});
lib.webview_create(1);

// You can also access just functions in the current process by passing a null
var current = ffi.Library(null, {
  'atoi': [ 'int', [ 'string' ] ]
});


class Webview {

}