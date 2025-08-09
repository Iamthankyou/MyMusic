# 21) Appendix — Suggested Gradle Snippets (illustrative)
```kotlin
// build.gradle.kts (app) — key libs (versions via BOMs / libs.versions.toml)
dependencies {
  implementation(platform("androidx.compose:compose-bom:<version>"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.navigation:navigation-compose:<version>")
  implementation("androidx.paging:paging-runtime:<version>")
  implementation("androidx.paging:paging-compose:<version>")

  implementation("com.google.dagger:hilt-android:<version>")
  kapt("com.google.dagger:hilt-compiler:<version>")
  implementation("com.squareup.retrofit2:retrofit:<version>")
  implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:<version>")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:<version>")
  implementation("com.squareup.okhttp3:okhttp:<version>")
  implementation("com.squareup.okhttp3:logging-interceptor:<version>")
  implementation("io.coil-kt:coil-compose:<version>")

  implementation("androidx.media3:media3-exoplayer:<version>")
  implementation("androidx.media3:media3-session:<version>")
  implementation("androidx.media3:media3-ui:<version>")
}
```
