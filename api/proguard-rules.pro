-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }

# project specific.
-keep,includedescriptorclasses class app.bambushain.model.**$$serializer { *; }
-keepclassmembers class app.bambushain.model.** { *** Companion; }
-keepclasseswithmembers class app.bambushain.model.** { kotlinx.serialization.KSerializer serializer(...); }
