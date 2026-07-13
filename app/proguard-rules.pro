# Room
-keep class * extends androidx.room.RoomDatabase
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *

# Hilt
-keep class **_HiltModules { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }

# Retrofit
-keepattributes Signature
-keepclasseswithmembers class * {
    @retrofit2.* <methods>;
}

# Gson
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Timber
-dontwarn timber.**
-keep class timber.** { *; }

# Keep our application classes
-keep class com.actuarypro.examtracker.** { *; }
