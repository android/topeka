# Topeka for Android

A fun to play quiz that showcases material design on Android

### Introduction

Material design is a new system for visual, interaction and motion design.
The Android version of Topeka demonstrates that the same branding and material
design principles can be used to create a consistent experience across
platforms.

We originally launched the [Topeka web app](https://github.com/Polymer/topeka)
as an Open Source example of material design on the web.

The current release of Topeka is available to users down to API level 14 aka
[Ice Cream Sandwich](http://developer.android.com/about/versions/android-4.0.html).
This is being accomplished by utilizing several [support
libraries](https://developer.android.com/tools/support-library/index.html).
Especially
[AppCompat](https://developer.android.com/tools/support-library/features.html#v7-appcompat)
and the [design support
library](https://developer.android.com/tools/support-library/features.html#design)
play important roles.

Topeka also features a set of [Espresso
tests](http://google.github.io/android-testing-support-library) which can be
executed with the `connectedAndroidTest` gradle task.

You can read more about the project on the [Android Developers
blog](http://android-developers.blogspot.co.uk/2015/06/more-material-design-with-topeka-for_16.html).

### Screenshots

<img src="screenshots/categories.png" width="30%" />
<img src="screenshots/category_history.png" width="30%" />
<img src="screenshots/quiz_shakespeare.png" width="30%" />

### Getting Started

Clone this repository, enter the top level directory and run <code>./gradlew tasks</code>
to get an overview of all the tasks available for this project.

Some important tasks are:

```
assembleDebug - Assembles all Debug builds.
installDebug - Installs the Debug build.
connectedAndroidTest - Installs and runs the tests for Debug build on connected
devices.
test - Run all unit tests.
```

### Support

- Google+ Community: https://plus.google.com/communities/105153134372062985968

- Stack Overflow: http://stackoverflow.com/questions/tagged/android

If you've found an error in this sample, please file an issue:

https://github.com/googlesamples/android-topeka/issues

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub.

### License


```
Copyright 2015 Google, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements. See the NOTICE file distributed with this work for
additional information regarding copyright ownership. The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```
