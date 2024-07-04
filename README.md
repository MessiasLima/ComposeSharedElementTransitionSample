# Jetpack compose Shared element transition
This is a simple application that aims to demonstrate the shared element transition animation using Jetpack compose and Compose navigation

https://github.com/MessiasLima/ComposeSharedElementTransitionSample/assets/10220064/d1b51e28-3941-4a84-8794-960466f75454

I also customized the `NavHost` animations to make the transition smoother.

The shared element transition is achieved using the `.sharedElement` modifier on both the start and target elements.

```kotlin
        .sharedElement(
            state = rememberSharedContentState(key = "key"), // Element identifier
            animatedVisibilityScope = animatedContentScope, // Animation scope
        )
```

Further info is available on the [documentation page](https://developer.android.com/develop/ui/compose/animation/shared-elements/navigation).
