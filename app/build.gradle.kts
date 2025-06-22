plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
    //kapt id
    id("kotlin-kapt")
}

android {
    namespace = "lk.kdu.ac.mc.todolist"
    compileSdk = 35

    defaultConfig {
        applicationId = "lk.kdu.ac.mc.todolist"
        minSdk = 31  // advance min sdk because splashscrren themes only working with API 31+, prevoius sdk value is 21
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
    implementation("com.google.firebase:firebase-analytics")

    //(help from : https://www.youtube.com/watch?v=wJKwsI5WUI4)
    //navigation dependency for move one screen to another(https://developer.android.com/develop/ui/compose/navigation#kts)
    val nav_version = "2.9.0"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")


    //firebase authentication dependencies (tutorial : https://www.youtube.com/watch?v=OsxJHpfTK1Q )
    //(source file : https://firebase.blog/posts/2022/05/adding-firebase-auth-to-jetpack-compose-app/ )

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependencies for the Credential Manager libraries and specify their versions
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    //dependency for splash screen animation
    implementation(libs.androidx.core.splashscreen)//(sourse : https://developer.android.com/develop/ui/views/launch/splash-screen#kts )

    //animated gif implementation
    implementation("io.coil-kt:coil-gif:2.6.0")
    implementation("io.coil-kt:coil-compose:2.6.0")



    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    implementation("androidx.compose.runtime:runtime-livedata:1.8.3")


    //setup depedencies for room database
    //(source : https://developer.android.com/jetpack/androidx/releases/room)
    //Knowlage : https://www.youtube.com/watch?v=sWOmlDvz_3U&list=PLgpnJydBcnPA5aNrlDxxKWSqAma7m3OIl&index=8

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")

    //dependency for extemded icons more icons
    implementation("androidx.compose.material:material-icons-extended")




}