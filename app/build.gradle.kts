 plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "hisaab.store.analyser"  // ✅ Add this line to specify the namespace
    compileSdk = 35

    defaultConfig {
        applicationId = "hisaab.store.analyser"
        minSdk = 24
        targetSdk = 35
        versionCode = 131
        versionName = "2.2.8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
}

dependencies {
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.1")
    implementation("com.itextpdf:itext7-core:7.2.3")
    implementation("com.github.skydoves:colorpickerview:2.2.4")
    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")
    implementation("com.google.android.material:material:1.13.0")

    testImplementation("junit:junit:4.+")
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.android.gms:play-services-ads:23.3.0")
    implementation("com.airbnb.android:lottie:4.1.0")
    implementation("com.google.android.play:app-update:2.1.0")
    implementation ("com.squareup.picasso:picasso:2.8")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
