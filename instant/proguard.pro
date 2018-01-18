# ProGuard configuration was created by following this guide:
# https://medium.com/google-developers/enabling-proguard-in-an-android-instant-app-fbd4fc014518

#
# rules from quiz module, generated using:
# comm -23 <(apkanalyzer dex packages quiz-debug.apk | grep "^C r" | cut -f4 | sort) \
# <(jar tf ~/Android/Sdk/platforms/android-27/android.jar | sed s/.class$// | sed -e s-/-.-g | sort)
#

-keep, includedescriptorclasses class android.support.design.widget.FloatingActionButton { public protected *; }
-keep, includedescriptorclasses class android.support.v4.app.ActivityCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.app.Fragment { public protected *; }
-keep, includedescriptorclasses class android.support.v4.app.FragmentManager { public protected *; }
-keep, includedescriptorclasses class android.support.v4.app.FragmentTransaction { public protected *; }
-keep, includedescriptorclasses class android.support.v4.app.SharedElementCallback { public protected *; }
-keep, includedescriptorclasses class android.support.v4.content.ContextCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.graphics.drawable.DrawableCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.view.animation.FastOutLinearInInterpolator { public protected *; }
-keep, includedescriptorclasses class android.support.v4.view.animation.FastOutSlowInInterpolator { public protected *; }
-keep, includedescriptorclasses class android.support.v4.view.animation.LinearOutSlowInInterpolator { public protected *; }
-keep, includedescriptorclasses class android.support.v4.view.MarginLayoutParamsCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.view.ViewCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.view.ViewPropertyAnimatorCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.view.ViewPropertyAnimatorListenerAdapter { public protected *; }
-keep, includedescriptorclasses class android.support.v7.app.AppCompatActivity { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ActivityLaunchHelper { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ActivityLaunchHelper$Companion { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.AnswerHelper { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ApiLevelHelper { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ContextExtensionsKt { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.FragmentExtensionsKt { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ViewExtensionsKt { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ViewUtils { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.Avatar { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.Category { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.Player { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.AlphaPickerQuiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.FillBlankQuiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.FillTwoBlanksQuiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.FourQuarterQuiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.MultiSelectQuiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.PickerQuiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.Quiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.QuizType { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.SelectItemQuiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.ToggleTranslateQuiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.quiz.TrueFalseQuiz { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.Theme { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.widget.AvatarView { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.widget.TextWatcherAdapter { public protected *; }
-keep, includedescriptorclasses class kotlin.collections.ArraysKt { public protected *; }
-keep, includedescriptorclasses class kotlin.collections.CollectionsKt { public protected *; }
-keep, includedescriptorclasses class kotlin.collections.IntIterator { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.functions.Function0 { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.internal.FunctionReference { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.internal.Intrinsics { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.internal.Lambda { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.internal.PropertyReference1Impl { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.internal.Reflection { public protected *; }
-keep, includedescriptorclasses class kotlin.Lazy { public protected *; }
-keep, includedescriptorclasses class kotlin.LazyKt { public protected *; }
-keep, includedescriptorclasses class kotlin.LazyThreadSafetyMode { public protected *; }
-keep, includedescriptorclasses class kotlin.NoWhenBranchMatchedException { public protected *; }
-keep, includedescriptorclasses class kotlin.ranges.RangesKt { public protected *; }
-keep, includedescriptorclasses class kotlin.text.StringsKt { public protected *; }
-keep, includedescriptorclasses class kotlin.TypeCastException { public protected *; }
-keep, includedescriptorclasses class kotlin.Unit { public protected *; }

#
# rules from categories module, generated using:
# comm -23 <(apkanalyzer dex packages categories-debug.apk | grep "^C r" | cut -f4 | sort) \
# <(jar tf ~/Android/Sdk/platforms/android-27/android.jar | sed s/.class$// | sed -e s-/-.-g | sort)
#


-keep, includedescriptorclasses class android.support.v4.app.ActivityCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.app.ActivityOptionsCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.app.Fragment { public protected *; }
-keep, includedescriptorclasses class android.support.v4.app.FragmentActivity { public protected *; }
-keep, includedescriptorclasses class android.support.v4.content.ContextCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.graphics.drawable.DrawableCompat { public protected *; }
-keep, includedescriptorclasses class android.support.v4.util.Pair { public protected *; }
-keep, includedescriptorclasses class android.support.v7.app.ActionBar { public protected *; }
-keep, includedescriptorclasses class android.support.v7.app.AppCompatActivity { public protected *; }
-keep, includedescriptorclasses class android.support.v7.widget.RecyclerView { public protected *; }
-keep, includedescriptorclasses class android.support.v7.widget.RecyclerView$Adapter { public protected *; }
-keep, includedescriptorclasses class android.support.v7.widget.RecyclerView$ViewHolder { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ActivityLaunchHelper { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ActivityLaunchHelper$Companion { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ApiLevelHelper { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.ContextExtensionsKt { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.FragmentExtensionsKt { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.helper.TransitionHelper { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.Category { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.Player { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.model.Theme { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.persistence.TopekaDatabaseHelper { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.widget.AvatarView { public protected *; }
-keep, includedescriptorclasses class com.google.samples.apps.topeka.widget.OffsetDecoration { public protected *; }
-keep, includedescriptorclasses class kotlin.collections.CollectionsKt { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.internal.Intrinsics { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.internal.Lambda { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.internal.PropertyReference1Impl { public protected *; }
-keep, includedescriptorclasses class kotlin.jvm.internal.Reflection { public protected *; }
-keep, includedescriptorclasses class kotlin.Lazy { public protected *; }
-keep, includedescriptorclasses class kotlin.LazyKt { public protected *; }
-keep, includedescriptorclasses class kotlin.LazyThreadSafetyMode { public protected *; }
-keep, includedescriptorclasses class kotlin.TypeCastException { public protected *; }


#
# rules copied from: quiz/build/intermediates/proguard-rules/feature/release/aapt_rules.txt
#

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/quiz/src/main/res/layout/activity_quiz.xml:88
-keep class android.support.design.widget.FloatingActionButton { <init>(...); }

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/quiz/src/main/res/layout/fragment_quiz.xml:37
-keep class android.support.v7.widget.Toolbar { <init>(...); }

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/quiz/build/intermediates/manifests/full/feature/release/AndroidManifest.xml:29
-keep class com.google.samples.apps.topeka.activity.QuizActivity { <init>(...); }

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/quiz/src/main/res/layout/fragment_quiz.xml:66
-keep class com.google.samples.apps.topeka.widget.AvatarView { <init>(...); }

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/quiz/src/main/res/transition-v21/quiz_shared_enter.xml:24
-keep class com.google.samples.apps.topeka.widget.TextResizeTransition { <init>(...); }

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/quiz/src/main/res/layout/answer_submit.xml:18
-keep class com.google.samples.apps.topeka.widget.fab.CheckableFab { <init>(...); }

#
# rules copied from: categories/build/intermediates/proguard-rules/feature/release/aapt_rules.txt
#

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/categories/src/main/res/layout/fragment_categories.xml:17
-keep class android.support.v7.widget.RecyclerView { <init>(...); }

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/categories/src/main/res/layout/activity_category_selection.xml:25
-keep class android.support.v7.widget.Toolbar { <init>(...); }

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/categories/build/intermediates/manifests/full/feature/release/AndroidManifest.xml:29
-keep class com.google.samples.apps.topeka.activity.CategorySelectionActivity { <init>(...); }

# Referenced at /usr/local/google/home/wkal/StudioProjects/topeka-devrel/categories/src/main/res/layout/activity_category_selection.xml:34
-keep class com.google.samples.apps.topeka.widget.AvatarView { <init>(...); }

