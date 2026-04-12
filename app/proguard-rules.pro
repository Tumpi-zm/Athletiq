# Athletiq ProGuard Rules

# Room
-keep class * extends androidx.room.RoomDatabase
-keepclassmembers class * { @androidx.room.* <methods>; }

# kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.athletiq.app.**$$serializer { *; }
-keepclassmembers class com.athletiq.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.athletiq.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}
