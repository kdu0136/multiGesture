[![](https://jitpack.io/v/kdu0136/multiGesture.svg)](https://jitpack.io/#kdu0136/multiGesture)

View Multi Gesture
=============
A multi gesture(move, rotate, scale) compatible with [View](https://developer.android.com/reference/android/view/View).

# Setup

__Step 1.__ Add the JitPack repository to your build file
```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```
__Step 2.__ Add the dependency

```groovy
dependencies {
  implementation 'com.github.kdu0136:multiGesture:<latest-version>'
}
```

# Usage

Add the `ViewMultiGesture` to your code:

```
    val targetView = findViewById<View>(R.id.targetView)

    ViewMultiGesture
        .Builder(target = targetView)
        .gestureConfig(gestureConfig = MultiGestureConfig())
        .touchListener(touchListener = object: TouchListener {
            override fun onDoubleTouch(view: View) {}
            override fun onLongTouch(view: View) {}
            override fun onSingleTouch(view: View) {}
            override fun onStartTouch(view: View) {}
            override fun onEndTouch(view: View) {}
        })
        .transformListener(transformListener = object: TransformListener {
            override fun onMove(view: View) {}
            override fun onScale(view: View) {}
            override fun onRotate(view: View) {}
        })
        .register()
```

# Customization
- MultiGestureConfig option

| Attribute                  | Note                                      | Default     |
|----------------------------|-------------------------------------------|-------------|
| minScaleFactor             | Minimum view scale factor                 | 1.0         |
| maxScaleFactor             | Maximum view scale factor                 | 2.0         |
| isRotateEnabled            | Use rotate gesture                        | true        |
| isScaleEnabled             | Use scale gesture                         | true        |
| isSimpleGestureEnabled     | Use move gesture                          | true        |
