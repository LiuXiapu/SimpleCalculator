#ָ�������ѹ������
-optimizationpasses 5
#��������ϴ�Сд
-dontusemixedcaseclassnames
#��ȥ���Էǹ����Ŀ���
-dontskipnonpubliclibraryclasses
 #�Ż�  ���Ż���������ļ�
-dontoptimize
 #ԤУ��
-dontpreverify
 #����ʱ�Ƿ��¼��־
-verbose
 # ����ʱ�����õ��㷨
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-ignorewarnings

-keepattributes Signature,*Annotation*


# keep okhttp3��okio
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-keep interface okhttp3.** { *; }
-dontwarn okio.**

# keep rx
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

# �������Ҫ����6.0ϵͳ���벻Ҫ����org.apache.http.legacy.jar
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}

