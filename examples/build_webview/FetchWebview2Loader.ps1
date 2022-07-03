# We use Webview2 from https://www.nuget.org/api/v2/package/Microsoft.Web.WebView2
# See https://www.nuget.org/packages/Microsoft.Web.WebView2/1.0.1245.22/License and license folder for the license for Webview2

# Can also use https://www.nuget.org/api/v2/package/Microsoft.Web.WebView2/{version}
mkdir libs
curl -o libs/mswebview2.nuget -ssL https://www.nuget.org/api/v2/package/Microsoft.Web.WebView2 # Get latest version.
#unzip -o libs/mswebview2.nuget "build/native/**/*" -d libs
Expand-Archive -Force -Path libs/mswebview2.nuget -DestinationPath libs

mv -Force libs/build/native/include libs
mv -Force libs/build/native/x64/* libs