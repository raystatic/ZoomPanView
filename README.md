# ZoomPanView
[![](https://jitpack.io/v/raystatic/ZoomPanView.svg)](https://jitpack.io/#raystatic/ZoomPanView)

<img src="https://github.com/raystatic/ZoomPanView/assets/31301266/a82b675d-7c4a-4c9a-89c9-bfb07ed54546" width="400"> <img src="https://github.com/raystatic/ZoomPanView/assets/31301266/2ef1b58c-5b39-48ba-afff-001e87c849d7" width="400">

ZoomPanView is a custom Android library that provides zooming and panning capabilities to images and videos. It allows users to interactively zoom in and out of images/videos and pan them within the view.

## Features

- Smooth zooming and panning of images
- Multi-touch support for zooming
- Dragging and flick gestures for panning
- Customizable zoom and pan behavior
- Support for handling touch events

## Installation

To use ZoomPanView in your Android project, follow these steps:

1. Add the following dependency in your project's build.gradle file:

```kotlin
implementation 'com.github.raystatic:ZoomPanView:${latest_version}'
```


Sync your project to fetch the library from the Maven repository.

## Usage
Add the ZoomPanView to your XML layout file:

```xml
<com.raystatic.zoom_pan_view.ZoomPanView
        android:id="@+id/zoomPanView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

For images, try the following

```kotlin
binding.zoomPanView.setImage(
    image = ContextCompat.getDrawable(this, R.drawable.sample_image),
    shouldShowGradientBackground = true
  )
```

For videos, try the following

```kotlin
 val uri = Uri.parse("android.resource://" + packageName + "/" + videos.random())
            binding.zoomPanView.setVideo(uri, shouldShowGradientBackground = true)
```

```kotlin
binding.zoomPanView.setImage(
    image = ContextCompat.getDrawable(this, R.drawable.sample_image),
    shouldShowGradientBackground = true
  )
```

## Sample Code

For a complete example of using ZoomPanView, refer to the [sample project](https://github.com/raystatic/ZoomPanView/tree/main/app/src).


## Troubleshooting

If you encounter any issues while using ZoomPanView, consider the following:

Ensure that you have added the library dependency correctly.
Verify that the image or video resource you are using is valid and accessible.
Check for conflicts with other libraries or custom view configurations in your project.
If you still face issues, please open a [new issue](https://github.com/raystatic/ZoomPanView/issues) with detailed information about the problem.

## Contributing

Contributions to ZoomPanView are welcome! To contribute, follow these steps:

Fork the repository and clone it to your local machine.
Make your changes or additions.
Create a new branch for your changes.
Commit and push your changes to your forked repository.
Submit a [pull request](https://github.com/raystatic/ZoomPanView/pulls) explaining the changes you made.



