# css-inliner

A JVM utility to inline CSS into HTML

## Getting Started

I haven't released this on Maven Central yet but you can grab it as a dependency using [JitPack](https://jitpack.io/).

### Gradle Plugin

With HTML templates in `src/main/resources/templates/` and a CSS file at `src/main/resources/style.css`:

```groovy
// build.gradle

buildscript {
  repositories {
    maven { url 'https://jitpack.io' }
  }
  dependencies {
    // change the commit hash to the latest hash or "master-SNAPSHOT" if you live your life without fear:
    classpath 'com.github.codebandits.css-inliner:com.github.codebandits.css-inliner.gradle.plugin:5301f8a3a1'
  }
}

apply plugin: 'java'
apply plugin: 'com.github.codebandits.css-inliner'

'css-inliner' {
  cssFile = project.file('src/main/resources/style.css')
  filesMatching = ['templates/**/*.html']
}
```

### Just the tool, no plugin

```groovy
// build.gradle

repositories {
  maven { url 'https://jitpack.io' }
}
dependencies {
  // change the commit hash to the latest hash or "master-SNAPSHOT" if you live your life without fear:
  compile 'com.github.codebandits.css-inliner:css-inliner:5301f8a3a1'
}
```

```java
// your app

import com.github.codebandits.cssinliner.CssInliner;

// ... ?
```
