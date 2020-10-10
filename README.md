# SplitCompatDynamicLanguageBug

This app illustrates a problem with SplitCompats dynamic language loading on API level 23 and below. The faulty behavior can be showcased by using bundletool:

```
./gradlew bundleDebug
bundletool build-apks --local-testing --bundle app/build/outputs/bundle/debug/app-debug.aab --output ~/tmp/bug.apks
bundletool install-apks --apks=~/tmp/bug.apks
```

Expected result:
Clicking on the DE button should switch the language of the TextView to German "Hallo Welt!".

Actual result:
This works starting on API 24. On API 23 and below the TextView's text stays in English
