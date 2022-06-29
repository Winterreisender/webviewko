#include "webview.h"

#include <stdlib.h>
#include <stdint.h>

struct binding_context {
    webview_t w;
    uintptr_t index;
};

void _webviewDispatchKtCallback(void *);
void _webviewBindingKtCallback(webview_t, char *, char *, uintptr_t);

static void _webview_dispatch_cb(webview_t w, void *arg) {
    _webviewDispatchKtCallback(arg);
}

static void _webview_binding_cb(const char *id, const char *req, void *arg) {
    struct binding_context *ctx = (struct binding_context *) arg;
    _webviewBindingKtCallback(ctx->w, (char *)id, (char *)req, ctx->index);
}

void CKtWebViewDispatch(webview_t w, uintptr_t arg) {
    webview_dispatch(w, _webview_dispatch_cb, (void *)arg);
}

void CKtWebViewBind(webview_t w, const char *name, uintptr_t index) {
    struct binding_context *ctx = calloc(1, sizeof(struct binding_context));
    ctx->w = w;
    ctx->index = index;
    webview_bind(w, name, _webview_binding_cb, (void *)ctx);
}