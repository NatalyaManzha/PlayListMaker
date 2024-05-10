plugins {
    id("kotlin-kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.practicum.playlistmaker"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.practicum.playlistmaker"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://itunes.apple.com\"")
        buildConfigField("String", "FAVORITES_DB", "\"favorites.db\"")
        buildConfigField("String", "PLAYLISTS_DB", "\"playlists.db\"")
        buildConfigField("String", "SHARED_PREFERENCES", "\"shared_preferences\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    //Peco - Runtime Permissions
    implementation("com.markodevcic:peko:3.0.5")
    //Room
    val roomVersion = "2.5.1" // текущая стабильная версия
    implementation("androidx.room:room-runtime:$roomVersion") // библиотека Room
    kapt("androidx.room:room-compiler:2.6.1") // Kotlin-кодогенератор
    implementation("androidx.room:room-ktx:$roomVersion") // поддержка корутин
    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    //TabLayout — часть библиотеки Material Design
    implementation("com.google.android.material:material:1.9.0")
    //Fragments
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    //ViewPager
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    //Koin
    implementation("io.insert-koin:koin-android:3.3.0")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //для MVVM
    implementation("androidx.activity:activity-ktx:1.6.1")
    //Обратная совместимость SDK (темы)
    implementation("androidx.appcompat:appcompat:1.6.1")
    //Constraintlayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}