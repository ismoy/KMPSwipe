# KMPSwipe

KMPSwipe is a complete Kotlin Cross-Platform library designed to integrate swipe gestures into your Compose-based applications, targeting both Android and iOS platforms with a unified API. This library allows developers to add dynamic and interactive swipe functionalities to any composable component, be it a Card, Box or any custom UI element, enhancing the user experience with intuitive gestures.
![KMPSwipe Banner](https://via.placeholder.com/800x200?text=KMPSwipe)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ismoy/kmpswipe.svg)](https://search.maven.org/artifact/io.github.ismoy/kmpswipe)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-1.5.0-green.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Platform](https://img.shields.io/badge/Platform-Android%20|%20iOS-orange.svg)](https://kotlinlang.org/docs/multiplatform.html)

[![KMP](https://img.shields.io/badge/KMP-Kotlin%20Multiplatform-7F52FF.svg)](https://kotlinlang.org/docs/multiplatform.html)
[![Swipe](https://img.shields.io/badge/UI-Swipe%20Gestures-red.svg)](https://github.com/ismoy/kmpswipe)
[![UX](https://img.shields.io/badge/UX-Haptic%20Feedback-blueviolet.svg)](https://github.com/ismoy/kmpswipe)

## Índice

- [How to use](#how-use)
- [Basic concepts](#basic-concepts)
- [Main components](#main-components)
- [Examples of use](#examples-of-use)
- [Advanced customization](#advanced-customization)
- [Performance optimization](#performance-optimization)
- [Integration with lists](#integration-with-lists)
- [Full API](#full-api)
- [FAQ](#faq)

| Android | iOS |
|---------|-----|
| ![Swipe Left](https://github.com/ismoy/KMPSwipe/blob/main/images/demo_slide_android.gif) | ![Swipe Right](https://github.com/ismoy/KMPSwipe/blob/main/images/demo_slide_ios.gif) |


## How use
### Native Android
 Gradle (Kotlin DSL)
 ```kotlin
 dependencies{
   implementation("io.github.ismoy:kmpswipe-android:1.0.0") // use latest version
}
```
## KMP (Kotlin multiplatform)
Gradle (Kotlin DSL)
```kotlin
commonMain.dependencies {
   implementation("io.github.ismoy:kmpswipe:1.0.0") // use latest version
}
```

## Basic concepts
KMPSwipe is based on four fundamental concepts:
SwipeDirection: Defines the direction of the swipe (Left, Right, None)
SwipeState: Defines the current state of the swipe (Start, Swiping, End, Cancelled)
SwipeableContent: The UI component that will be swiped
SwipeBackgrounds: The UI components that are displayed underneath during the swipe
## Minimal example


```kotlin
KmpSwipe(
        onSwipeComplete = { direction ->
            when (direction) {
                SwipeDirection.Left -> { /* Left action */ }
                SwipeDirection.Right -> { /* Right action */ }
                else -> {}
            }
        }
    ) { _, _ ->
        // Your slide content here
        Box(modifier = Modifier.height(160.dp)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center) {
            Text("Slide this item left or right")
        }
    }
```
## Main components
### SwipeDirection
```kotlin
enum class SwipeDirection {
    None,
    Left,
    Right
}
```
### SwipeState
```kotlin
enum class SwipeState {
    Start,      // Beginning of the gesture
    Swiping,    // During the slide
    End,        // Slide completed
    Cancelled   // Slide cancelled

```
## KmpSwipe
The main component that wraps your content to make it slideable.
### Examples of use
### Basic sliding element
```kotlin
  KmpSwipe(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onSwipeComplete = { direction ->
            when (direction) {
                SwipeDirection.Left -> { /* Delete element */ }
                SwipeDirection.Right -> { /* Archive element */ }
                else -> {}
            }
        },
        leftBackground = { _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Delete",
                    color = Color.White,
                    modifier = Modifier.padding(end = 32.dp)
                )
            }
        },
        rightBackground = { _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Green, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Archive",
                    color = Color.Black,
                    modifier = Modifier.padding(start = 32.dp)
                )
            }
        }
    ) { _, _ ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Basic sliding element", style = MaterialTheme.typography.h6)
                Text(text = "Swipe me", style = MaterialTheme.typography.body1)
            }
        }
    }
```
## List with slideable elements
```kotlin
// In your Screen o App.kt 
val itemList = listOf(
        Item(id = "1", title = "Item 1", description = "Descripción del Item 1"),
        Item(id = "2", title = "Item 2", description = "Descripción del Item 2"),
        Item(id = "3", title = "Item 3", description = "Descripción del Item 3")
    )
    SwipeableList(items = itemList)
```
```kotlin
@Composable
fun SwipeableList(items: List<Item>) {
    LazyColumn {
        items(items, key = { it.id }) { item ->
            KmpSwipe(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                onSwipeComplete = { direction ->
                    when (direction) {
                        SwipeDirection.Left -> { /* Delete */ }
                        SwipeDirection.Right -> { /* Archive */ }
                        else -> {}
                    }
                },
                leftBackground = { _ ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier.padding(end = 32.dp)
                        )
                        Text(
                            text = "Delete",
                            color = Color.White,
                            modifier = Modifier.padding(end = 90.dp)
                        )
                    }
                },
                rightBackground = { _ ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Blue, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Archive",
                            tint = Color.White,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                        Text(
                            text = "Archive",
                            color = Color.White,
                            modifier = Modifier.padding(start = 90.dp)
                        )
                    }
                }
            ) { _, _ ->
                ItemCard(item)
            }
        }
    }
}
```
```kotlin
@Composable
fun ItemCard(item: Item) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = item.title, style = MaterialTheme.typography.h6)
                Text(text = item.description, style = MaterialTheme.typography.body2)
            }
        }
    }
}
```
```kotlin
data class Item(
    val id: String,
    val title: String,
    val description: String,
)
```
## Swipe-in ​​inbox interface
```kotlin
// In your Screen o App.kt 
val emailList = listOf(
        Email(id = "1", sender = "Alice", time = "10:30 AM", subject = "Important Meeting", preview = "Hello, don't forget today's meeting at 3 PM..."),
        Email(id = "2", sender = "Bob", time = "11:15 AM", subject = "Project Update", preview = "I'm sending you the latest changes in the project report..."),
        Email(id = "3", sender = "Charlie", time = "12:00 PM", subject = "Event Invitation", preview = "You are invited to the networking event this Friday...")
    )
    EmailInbox(emails = emailList)
```
```kotlin
@Composable
fun EmailInbox(emails: List<Email>) {
    LazyColumn {
        items(emails, key = { it.id }) { email ->
            KmpSwipe(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                onSwipeComplete = { direction ->
                    when (direction) {
                        SwipeDirection.Left -> { /* Delete email */ }
                        SwipeDirection.Right -> { /* Archive email */ }
                        else -> {}
                    }
                },
                onSwipeStateChange = { state ->
                    // Status monitoring for analysis or debugging
                },
                swipeThreshold = 120.dp, // Custom threshold
                resistance = 1.2f,  // Greater resistance
                leftBackground = { _ ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(
                            modifier = Modifier.padding(end = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Delete",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                },
                rightBackground = { offset ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Archive",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Archive",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            ) { swipeState, swipeDirection ->
                EmailItem(email, swipeState)
            }
        }
    }
}
```
```kotlin
fun EmailItem(email: Email, swipeState: SwipeState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = if (swipeState == SwipeState.Swiping) 8.dp else 2.dp,
        // Animación de elevación durante el deslizamiento
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = email.sender,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = email.time,
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email.subject,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email.preview,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2
            )
        }
    }
}
```
```kotlin
data class Email(
    val id: String,
    val sender: String,
    val time: String,
    val subject: String,
    val preview: String
)
```
 ## Advanced customization
Dynamic slip threshold
```kotlin
KmpSwipe(
    // ...
    dynamicSwipeThreshold = { threshold ->
        // Dynamically calculate the threshold based on some logic
       // For example, increase the threshold based on content
        if (isLongContent) threshold * 1.5f else threshold
    }
) { swipeState, swipeDirection ->
    // Your content
}
```
## Customizing Swipe Behavior
```kotlin
KmpSwipe(
    // ...
    swipeThreshold = 120.dp,  // Distance to complete the slide
    resistance = 1.5f,        // Greater resistance = more difficult movement
    springStiffness = 700f,   // Higher stiffness = faster animation
    swipeLimitMultiplier = 2f, // Maximum slip limit
    dampingRatio = Spring.DampingRatioMediumBouncy, // Bounce Type
    vibrationEnabled = true,  // Haptic feedback upon completion
) { swipeState, swipeDirection ->
    // Your content
}
```
## Control of allowed swipe
```kotlin
KmpSwipe(
    // ...
    swipeDirections = setOf(SwipeDirection.Left), // Only allow left swipe
) { swipeState, swipeDirection ->
    // Your content
}
```
```kotlin
KmpSwipe(
    // ...
    swipeDirections = setOf(SwipeDirection.Right), // Only allow right swipe
) { swipeState, swipeDirection ->
    // Your content
}
```
## Visual change based on state
```kotlin
KmpSwipe(
    // ...
    onSwipeStateChange = { state ->
       // Track the state to perform further actions
    }
) { swipeState, swipeDirection ->
    val backgroundColor = when (swipeState) {
        SwipeState.Start -> Color.White
        SwipeState.Swiping -> if (swipeDirection == SwipeDirection.Left) 
                              Color.Red.copy(alpha = 0.1f) 
                          else 
                              Color.Green.copy(alpha = 0.1f)
        SwipeState.End -> Color.LightGray
        SwipeState.Cancelled -> Color.White
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
        elevation = if (swipeState == SwipeState.Swiping) 8.dp else 2.dp
    ) {
        // Your Content
    }
}
```
## Advanced backgrounds with animation
```kotlin
KmpSwipe(
    // ...
    leftBackground = { offset ->
        val progress = (offset / 100.dp).coerceIn(0f, 1f)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red.copy(alpha = progress), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier.padding(end = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Delete",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (16 + progress * 4).sp // Text that grows with sliding
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size((24 + progress * 8).dp) // Growing icon
                )
            }
        }
    }
) { swipeState, swipeDirection ->
    // Your content
}
```
## Performance optimization
KMPSwipe is designed to be highly performant, but here are some techniques to optimize it further:
1. Using keys on list items
```kotlin
LazyColumn {
    items(items, key = { it.id }) { item ->
        KmpSwipe(
            // ...
        ) { swipeState, swipeDirection ->
            // Content
        }
    }
}
```
2. Avoid unnecessary recompositions
```kotlin
// Extract static components
val leftBackground: @Composable (Dp) -> Unit = { offset ->
    // Left background implementation
}

val rightBackground: @Composable (Dp) -> Unit = { offset ->
   // Right background implementation
}

// Then use them in KmpSwipe
KmpSwipe(
    // ...
    leftBackground = leftBackground,
    rightBackground = rightBackground
) { swipeState, swipeDirection ->
    // Content
}
```
3. Optimize callbacks
```kotlin
// Avoid creating new lambda functions on each recomposition
val onSwipeComplete = remember<(SwipeDirection) -> Unit> { { direction ->
    // Actions upon completion of swipe
} }

KmpSwipe(
    // ...
    onSwipeComplete = onSwipeComplete
) { swipeState, swipeDirection ->
    // Content
}
```

## Integration with lists
   Task list with swipe
```kotlin
    val taskItemList = listOf(
        Task(id = "1", title = "Buy groceries", description = "Milk, eggs, bread, and fruits", isCompleted = false, dueDate = "2025-02-26", isOverdue = false),
        Task(id = "2", title = "Finish project report", description = "Complete the final draft and send it to the manager", isCompleted = false, dueDate = "2025-02-27", isOverdue = false),
        Task(id = "3", title = "Doctor's appointment", description = "Annual check-up at 10:00 AM", isCompleted = false, dueDate = "2025-02-28", isOverdue = false)
    )

    TaskList(taskItemList)
```
```kotlin
@Composable
fun TaskList(tasks: List<Task>) {
    LazyColumn {
        items(tasks, key = { it.id }) { task ->
            KmpSwipe(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                onSwipeComplete = { direction ->
                    when (direction) {
                        SwipeDirection.Left -> { /* Delete Task */ }
                        SwipeDirection.Right -> { /* Add Task */ }
                        else -> {}
                    }
                },
                leftBackground = { offset ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red.copy(alpha = 0.8f), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Delete",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 24.dp)
                        )
                    }
                },
                rightBackground = { offset ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Add",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 24.dp)
                        )
                    }
                }
            ) { swipeState, completedDirection ->
                TaskItem(task,swipeState,completedDirection)
            }
        }
    }
}
```
```kotlin
@Composable
fun TaskItem(task: Task, swipeState: SwipeState, completedDirection: SwipeDirection) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 1.dp,
        shape = RoundedCornerShape(4.dp),
        backgroundColor = if (task.isCompleted) Color(0xFFE8F5E9) else Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val chipText = when (swipeState) {
                SwipeState.Swiping -> if (completedDirection == SwipeDirection.Right) "Adding" else "Deleting"
                SwipeState.End -> if (completedDirection == SwipeDirection.Left) "Deleted" else "Added"
                else -> ""
            }
            if (chipText.isNotEmpty()) {
                Chip(
                    onClick = {},
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(text = chipText)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.subtitle1,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )

                    if (task.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (task.dueDate != null) {
                    Text(
                        text = task.dueDate,
                        style = MaterialTheme.typography.caption,
                        color = if (task.isOverdue) Color.Red else Color.Gray
                    )
                }
            }
        }
    }
}
```
```kotlin
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val dueDate: String?,
    val isOverdue: Boolean
)
```
## Full API
KmpSwipe
| Parameter               | Type                                      | Description                                        | default value                      |
|--------------------------|------------------------------------------|----------------------------------------------------|------------------------------------------|
| `modifier`              | `Modifier`                               | Compose Modifier for the Container            | `Modifier`                               |
| `onSwipeComplete`       | `(SwipeDirection) -> Unit`              | Callback when a swipe is completed      | `{}`                                     |
| `onSwipe`              | `(SwipeDirection, Dp) -> Unit`           | Callback during sliding                 | `{ _, _ -> }`                           |
| `onSwipeStateChange`    | `(SwipeState) -> Unit`                  | Callback when state changes                  | `{}`                                     |
| `swipeThresholdDp`      | `Dp`                                    | Minimum distance to complete a slide | `100.dp`                                |
| `resistance`           | `Float`                                 | Sliding resistance factor                 | `1f`                                    |
| `springStiffness`       | `Float`                                 | Spring effect stiffness in animation     | `500f`                                  |
| `swipeLimitMultiplier`  | `Float`                                 | Multiplier for the slip limit     | `1.5f`                                  |
| `backgroundPaddingHorizontal` | `Dp`                           | Horizontal padding for backgrounds                | `6.dp`                                  |
| `vibrationEnabled`      | `Boolean`                               | Enable haptic feedback              | `true`                                  |
| `dampingRatio`         | `Float`                                 | Damping ratio for animations           | `Spring.DampingRatioMediumBouncy`       |
| `leftBackground`        | `@Composable (offset: Dp) -> Unit`      | Composable for left slide background | `{}`                             |
| `rightBackground`       | `@Composable (offset: Dp) -> Unit`      | Composable for right slide background  | `{}`                             |
| `enabled`              | `Boolean`                               | Enable/disable swipe gestures | `true`                                  |
| `swipeDirections`      | `Set<SwipeDirection>`                    | Allowed directions for swiping               | `setOf(Left, Right)`                     |
| `onSwipeVelocity`      | `(Float) -> Unit`                       | Callback with the speed of the slide      | `{}`                                     |
| `dynamicSwipeThreshold` | `((Dp) -> Dp)?`                        | Function to dynamically calculate the threshold     | `null`                                   |
| `content`              | `@Composable (SwipeState, SwipeDirection) -> Unit` | Sliding content | `-` |

## FAQ
#### How can I handle multiple actions in one swipe?
You can use different thresholds for different actions:
```kotlin
KmpSwipe(
    // ...
    onSwipe = { direction, offset ->
        if (direction == SwipeDirection.Left && offset.value > 150f) {
           // Additional action when sliding beyond a certain point
        }
    }
) { swipeState, swipeDirection ->
    // Content
}
```
#### How can I save the sliding state?
You can persist state with rememberSaveable:
```kotlin
val persistedState = rememberSaveable { mutableStateOf(SwipeState.Start) }
val persistedDirection = rememberSaveable { mutableStateOf(SwipeDirection.None) }

KmpSwipe(
    // ...
    onSwipeStateChange = { state ->
        persistedState.value = state
    },
    onSwipeComplete = { direction ->
        persistedDirection.value = direction
        // Other actions
    }
) { swipeState, swipeDirection ->
    // Use persistedState.value and persistedDirection.value if necessary
   // to maintain state across recompositions
}
```
#### Is it possible to customize the return animation?
The return animation uses the springStiffness and dampingRatio parameters:
```kotlin
KmpSwipe(
    // ...
    springStiffness = 800f,  // Faster
    dampingRatio = Spring.DampingRatioNoBouncy,  // No bounce
) { swipeState, swipeDirection ->
    // Content
}
```
#### How can I disable swiping conditionally?
Use the enabled parameter:
```kotlin
KmpSwipe(
    // ...
    enabled = !isLoading && itemIsSwipeable,
) { swipeState, swipeDirection ->
    // Content
}
```
#### How can I get the sliding speed?
Use the onSwipeVelocity callback:
```kotlin
KmpSwipe(
    // ...
    onSwipeVelocity = { velocity ->
        if (abs(velocity) > 1000f) {
            //Fast slide, you could make a special animation
        }
    }
) { swipeState, swipeDirection ->
    // Content
}
```
## Contribution
*KMPSwipe* is an open source project and contributions are welcome. To contribute:
1. Fork the repository
2. Create a branch for your feature (git checkout -b feature/amazing-feature)
3. Commit your changes (git commit -m 'Add some amazing feature')
4. Push to branch (git push origin feature/amazing-feature)
5. Open a Pull Request

## Native iOS Support

Good news! Native iOS support is on the way. We're working hard to bring the functionality of KmpSwipe to the iOS platform, allowing you to use the same swiping logic in your iOS applications. Stay tuned for future updates and announcements regarding the availability of iOS support.

## License
*KMPSwipe* is licensed under the *MIT license.* See the *LICENSE* file for more details.

