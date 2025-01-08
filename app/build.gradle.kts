plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.appdam"
    compileSdk = 34

    viewBinding{
        enable = true
    }


    defaultConfig {
        applicationId = "com.example.appdam"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        enable = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.annotation:annotation:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

//circle image view
    implementation("de.hdodenhof:circleimageview:3.1.0")

//scalable unit text size
    implementation("com.intuit.ssp:ssp-android:1.0.6")

//scalable unit size
    implementation("com.intuit.sdp:sdp-android:1.0.6")

//room database
    implementation("androidx.room:room-runtime:2.2.5")
    kapt ("androidx.room:room-compiler:2.2.5")
    implementation("androidx.room:room-ktx:2.2.1")
    implementation("com.makeramen:roundedimageview:2.3.0")


//easy permission
    implementation("pub.devrel:easypermissions:3.0.0")

//coroutines core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

}