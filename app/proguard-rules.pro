# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/alvinzhang/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Gson
-keep class com.google.gson.stream.** {*;}
-dontwarn sun.misc.Unsafe

# Retrofit2
-keep class retrofit2.** {*;}
-dontwarn retrofit2.**

-keepclasseswithmembers class * {
  @retrofit2.http.* <methods>;
}

# Okio
-dontwarn okio.**

# OkHttp3
-keep class okhttp3.** {*;}
-keep interface okhttp3.** {*;}

# Rxjava
-dontwarn rx.internal.util.unsafe.**
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
 # for DexGuard only
 -keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# android.support.design
-keep class android.support.design.** {*;}

# 序列化实体
-keep public class * implements java.io.Serializable {*;}

# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

 #qq分享
 -keep class com.tencent.tauth.**{*;}
 -keep class com.tencent.open.**{*;}
 -keep class com.tencent.connect.**{*;}
 #微信分享
 -dontwarn com.tencent.mm.**
 -keep class com.tencent.mm.**{*;}

