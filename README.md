# KMPSwipe

KMPSwipe es una biblioteca Kotlin Multiplatform completa diseñada para integrar gestos de deslizamiento en tus aplicaciones basadas en Compose, dirigida tanto a plataformas Android como iOS con una API unificada. Esta biblioteca permite a los desarrolladores añadir funcionalidades de deslizamiento dinámicas e interactivas a cualquier componente composable, ya sea una Card, Box o cualquier elemento UI personalizado, mejorando la experiencia del usuario con gestos intuitivos.

![KMPSwipe Banner](https://via.placeholder.com/800x200?text=KMPSwipe)

## Índice

- [Instalación](#instalación)
- [Conceptos básicos](#conceptos-básicos)
- [Componentes principales](#componentes-principales)
- [Ejemplos de uso](#ejemplos-de-uso)
- [Personalización avanzada](#personalización-avanzada)
- [Control programático](#control-programático)
- [Optimización de rendimiento](#optimización-de-rendimiento)
- [Integración con listas](#integración-con-listas)
- [API completa](#api-completa)
- [Preguntas frecuentes](#preguntas-frecuentes)

## Instalación

### Gradle (Kotlin DSL)

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

// build.gradle.kts (módulo)
dependencies {
    implementation("io.github.ismoy:kmpswipe:1.0.0")
}
```
### Gradle (Groovy DSL)
``` kotlin
// settings.gradle
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

// build.gradle (módulo)
dependencies {
    implementation 'io.github.ismoy:kmpswipe:1.0.0'
}
```
## Conceptos básicos
KMPSwipe se basa en cuatro conceptos fundamentales:

SwipeDirection: Define la dirección del deslizamiento (Left, Right, None)
SwipeState: Define el estado actual del deslizamiento (Start, Swiping, End, Cancelled)
Contenido deslizable: El componente UI que será deslizable
Fondos de deslizamiento: Los componentes UI que se muestran debajo durante el deslizamiento
## Ejemplo mínimo
```kotlin
KmpSwipe(
    onSwipeComplete = { direction ->
        when (direction) {
            SwipeDirection.Left -> { /* Acción izquierda */ }
            SwipeDirection.Right -> { /* Acción derecha */ }
            else -> {}
        }
    }
) { swipeState, swipeDirection ->
    // Tu contenido deslizable aquí
    Card {
        Text("Desliza este elemento hacia la izquierda o derecha")
    }
}
```
## Componentes principales
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
    Start,      // Inicio del gesto
    Swiping,    // Durante el deslizamiento
    End,        // Deslizamiento completado
    Cancelled   // Deslizamiento cancelado
}
```
## KmpSwipe
El componente principal que envuelve tu contenido para hacerlo deslizable.
### Ejemplos de uso
### Elemento deslizable básico
```kotlin
KmpSwipe(
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    onSwipeComplete = { direction ->
        when (direction) {
            SwipeDirection.Left -> { /* Eliminar elemento */ }
            SwipeDirection.Right -> { /* Archivar elemento */ }
            else -> {}
        }
    },
    leftBackground = { offset ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Eliminar",
                color = Color.White,
                modifier = Modifier.padding(end = 32.dp)
            )
        }
    },
    rightBackground = { offset ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Archivar",
                color = Color.White,
                modifier = Modifier.padding(start = 32.dp)
            )
        }
    }
) { swipeState, swipeDirection ->
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Título del elemento", style = MaterialTheme.typography.h6)
            Text(text = "Descripción del elemento", style = MaterialTheme.typography.body1)
        }
    }
}
```
## Lista con elementos deslizables
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
                        SwipeDirection.Left -> { /* Eliminar */ }
                        SwipeDirection.Right -> { /* Archivar */ }
                        else -> {}
                    }
                },
                leftBackground = { offset ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.White,
                            modifier = Modifier.padding(end = 32.dp)
                        )
                        Text(
                            text = "Eliminar",
                            color = Color.White,
                            modifier = Modifier.padding(end = 90.dp)
                        )
                    }
                },
                rightBackground = { offset ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Blue, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = Icons.Default.Archive,
                            contentDescription = "Archivar",
                            tint = Color.White,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                        Text(
                            text = "Archivar",
                            color = Color.White,
                            modifier = Modifier.padding(start = 90.dp)
                        )
                    }
                }
            ) { swipeState, swipeDirection ->
                ItemCard(item)
            }
        }
    }
}

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
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = item.title, style = MaterialTheme.typography.h6)
                Text(text = item.description, style = MaterialTheme.typography.body2)
            }
        }
    }
}
```
## Interfaz de bandeja de entrada con deslizamiento
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
                        SwipeDirection.Left -> { /* Eliminar email */ }
                        SwipeDirection.Right -> { /* Archivar email */ }
                        else -> {}
                    }
                },
                onSwipeStateChange = { state ->
                    // Seguimiento del estado para análisis o depuración
                },
                swipeThreshold = 120.dp, // Umbral personalizado
                resistance = 1.2f,  // Mayor resistencia
                leftBackground = { offset ->
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
                                text = "Eliminar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
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
                                imageVector = Icons.Default.Archive,
                                contentDescription = "Archivar",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Archivar",
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

@Composable
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
## Personalización avanzada
### Umbral de deslizamiento dinámico
```kotlin
KmpSwipe(
    // ...
    dynamicSwipeThreshold = { threshold ->
        // Calcula dinámicamente el umbral basado en alguna lógica
        // Por ejemplo, aumentar el umbral basado en el contenido
        if (isLongContent) threshold * 1.5f else threshold
    }
) { swipeState, swipeDirection ->
    // Tu contenido
}
```
## Personalización del comportamiento de deslizamiento
```kotlin
KmpSwipe(
    // ...
    swipeThreshold = 120.dp,  // Distancia para completar el deslizamiento
    resistance = 1.5f,        // Mayor resistencia = movimiento más difícil
    springStiffness = 700f,   // Mayor rigidez = animación más rápida
    swipeLimitMultiplier = 2f, // Límite máximo del deslizamiento
    dampingRatio = Spring.DampingRatioMediumBouncy, // Tipo de rebote
    vibrationEnabled = true,  // Retroalimentación háptica al completar
) { swipeState, swipeDirection ->
    // Tu contenido
}
```
## Control de direcciones permitidas
```kotlin
KmpSwipe(
    // ...
    swipeDirections = setOf(SwipeDirection.Left), // Solo permitir deslizamiento a la izquierda
) { swipeState, swipeDirection ->
    // Tu contenido
}
```
## Cambio visual basado en el estado
```kotlin
KmpSwipe(
    // ...
    onSwipeStateChange = { state ->
        // Seguir el estado para realizar acciones adicionales
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
        // Contenido
    }
}
```
## Fondos avanzados con animación
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
                    text = "Eliminar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (16 + progress * 4).sp // Texto que crece con el deslizamiento
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White,
                    modifier = Modifier.size((24 + progress * 8).dp) // Icono que crece
                )
            }
        }
    }
) { swipeState, swipeDirection ->
    // Tu contenido
}
```
## Optimización de rendimiento
KMPSwipe está diseñado para tener un alto rendimiento, pero aquí hay algunas técnicas para optimizarlo aún más:
1. Usar keys en elementos de lista
```kotlin
LazyColumn {
    items(items, key = { it.id }) { item ->
        KmpSwipe(
            // ...
        ) { swipeState, swipeDirection ->
            // Contenido
        }
    }
}
```
2. Evitar recomposiciones innecesarias
```kotlin
// Extrae componentes estáticos
val leftBackground: @Composable (Dp) -> Unit = { offset ->
    // Implementación del fondo izquierdo
}

val rightBackground: @Composable (Dp) -> Unit = { offset ->
    // Implementación del fondo derecho
}

// Luego úsalos en KmpSwipe
KmpSwipe(
    // ...
    leftBackground = leftBackground,
    rightBackground = rightBackground
) { swipeState, swipeDirection ->
    // Contenido
}
```
3. Optimizar callbacks
```kotlin
// Evitar crear nuevas funciones lambda en cada recomposición
val onSwipeComplete = remember<(SwipeDirection) -> Unit> { { direction ->
    // Acciones al completar el deslizamiento
} }

KmpSwipe(
    // ...
    onSwipeComplete = onSwipeComplete
) { swipeState, swipeDirection ->
    // Contenido
}
```

## Integración con listas
Lista de contactos con deslizamiento
```kotlin
@Composable
fun ContactList(contacts: List<Contact>) {
    LazyColumn {
        items(contacts, key = { it.id }) { contact ->
            KmpSwipe(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onSwipeComplete = { direction ->
                    when (direction) {
                        SwipeDirection.Left -> { /* Eliminar contacto */ }
                        SwipeDirection.Right -> { /* Llamar al contacto */ }
                        else -> {}
                    }
                },
                leftBackground = { offset ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(end = 32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = Color.White
                            )
                            Text("Eliminar", color = Color.White)
                        }
                    }
                },
                rightBackground = { offset ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Green, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(start = 32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Llamar",
                                tint = Color.White
                            )
                            Text("Llamar", color = Color.White)
                        }
                    }
                }
            ) { swipeState, swipeDirection ->
                ContactCard(contact)
            }
        }
    }
}

@Composable
fun ContactCard(contact: Contact) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.first().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}
```
## Lista de tareas con deslizamiento
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
                        SwipeDirection.Left -> { /* Eliminar tarea */ }
                        SwipeDirection.Right -> { /* Completar tarea */ }
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
                            text = "Eliminar",
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
                            text = "Completar",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 24.dp)
                        )
                    }
                }
            ) { swipeState, swipeDirection ->
                TaskItem(task)
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 1.dp,
        shape = RoundedCornerShape(4.dp),
        backgroundColor = if (task.isCompleted) Color(0xFFE8F5E9) else Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { /* Actualizar estado */ }
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
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
```
## API completa
KmpSwipe
| Parámetro                | Tipo                                      | Descripción                                        | Valor por defecto                        |
|--------------------------|------------------------------------------|----------------------------------------------------|------------------------------------------|
| `modifier`              | `Modifier`                               | Modificador Compose para el contenedor            | `Modifier`                               |
| `onSwipeComplete`       | `(SwipeDirection) -> Unit`              | Callback cuando se completa un deslizamiento      | `{}`                                     |
| `onSwipe`              | `(SwipeDirection, Dp) -> Unit`           | Callback durante el deslizamiento                 | `{ _, _ -> }`                           |
| `onSwipeStateChange`    | `(SwipeState) -> Unit`                  | Callback cuando cambia el estado                  | `{}`                                     |
| `swipeThresholdDp`      | `Dp`                                    | Distancia mínima para completar un deslizamiento | `100.dp`                                |
| `resistance`           | `Float`                                 | Factor de resistencia al deslizar                 | `1f`                                    |
| `springStiffness`       | `Float`                                 | Rigidez del efecto de resorte en la animación     | `500f`                                  |
| `swipeLimitMultiplier`  | `Float`                                 | Multiplicador para el límite de deslizamiento     | `1.5f`                                  |
| `backgroundPaddingHorizontal` | `Dp`                           | Padding horizontal para los fondos                | `6.dp`                                  |
| `vibrationEnabled`      | `Boolean`                               | Habilitar retroalimentación háptica               | `true`                                  |
| `dampingRatio`         | `Float`                                 | Ratio de amortiguación para animaciones           | `Spring.DampingRatioMediumBouncy`       |
| `leftBackground`        | `@Composable (offset: Dp) -> Unit`      | Composable para el fondo de deslizamiento a la izquierda | `{}`                             |
| `rightBackground`       | `@Composable (offset: Dp) -> Unit`      | Composable para el fondo de deslizamiento a la derecha  | `{}`                             |
| `enabled`              | `Boolean`                               | Habilitar/deshabilitar los gestos de deslizamiento | `true`                                  |
| `swipeDirections`      | `Set<SwipeDirection>`                    | Direcciones permitidas para deslizar               | `setOf(Left, Right)`                     |
| `onSwipeVelocity`      | `(Float) -> Unit`                       | Callback con la velocidad del deslizamiento       | `{}`                                     |
| `initialDirection`      | `SwipeDirection`                        | Dirección inicial (control programático)          | `SwipeDirection.None`                    |
| `dynamicSwipeThreshold` | `((Dp) -> Dp)?`                        | Función para calcular dinámicamente el umbral     | `null`                                   |
| `content`              | `@Composable (SwipeState, SwipeDirection) -> Unit` | Contenido deslizable | `-` |
## Ejemplos reales
### Lista de correos electrónicos con deslizamiento vertical y horizontal
```kotlin
@Composable
fun AdvancedEmailList(emails: List<Email>) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    
    // Configuración de opciones de deslizamiento común
    val leftBgAction: @Composable (Dp) -> Unit = { offset ->
        EmailActionBackground(
            text = "Eliminar",
            icon = Icons.Default.Delete,
            color = Color.Red,
            offset = offset,
            alignment = Alignment.CenterEnd
        )
    }
    
    val rightBgAction: @Composable (Dp) -> Unit = { offset ->
        EmailActionBackground(
            text = "Archivar",
            icon = Icons.Default.Archive,
            color = Color(0xFF607D8B),
            offset = offset,
            alignment = Alignment.CenterStart
        )
    }
    
    LazyColumn(state = listState) {
        items(emails, key = { it.id }) { email ->
            KmpSwipe(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                onSwipeComplete = { direction ->
                    when (direction) {
                        SwipeDirection.Left -> { 
                            // Acción eliminar
                        }
                        SwipeDirection.Right -> {
                            // Acción archivar
                        }
                        else -> {}
                    }
                },
                leftBackground = leftBgAction,
                rightBackground = rightBgAction,
                swipeThreshold = 110.dp,
                resistance = 1.1f,
                vibrationEnabled = true,
                onSwipeStateChange = { state ->
                    // Puedes integrar con análisis o realizar acciones adicionales
                    when (state) {
                        SwipeState.End -> {
                            // El deslizamiento se completó
                        }
                        else -> {}
                    }
                },
                dynamicSwipeThreshold = { threshold ->
                    // Umbral personalizado según características del correo
                    when {
                        email.isImportant -> threshold * 1.3f // Más difícil de eliminar
                        email.isRead -> threshold * 0.9f      // Más fácil de deslizar
                        else -> threshold
                    }
                }
            ) { swipeState, swipeDirection ->
                EmailItemAdvanced(
                    email = email,
                    swipeState = swipeState,
                    swipeDirection = swipeDirection,
                    onMarkAsRead = { /* Marcar como leído */ },
                    onStar = { /* Marcar como favorito */ }
                )
            }
        }
    }
    
    // Botones para demostrar el control programático
    if (emails.isNotEmpty()) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    // Ejemplo: deslizar el primer elemento programáticamente
                    // Esta es una demostración conceptual
                    // KmpSwipe(SwipeDirection.Left, firstItemController)
                }
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Demo Swipe")
        }
    }
}

@Composable
fun EmailActionBackground(
    text: String,
    icon: ImageVector,
    color: Color,
    offset: Dp,
    alignment: Alignment
) {
    val progress = (offset / 100.dp).coerceIn(0f, 1f)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color.copy(alpha = 0.8f + (progress * 0.2f)), RoundedCornerShape(8.dp)),
        contentAlignment = alignment
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (alignment == Alignment.CenterStart) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = Color.White,
                    modifier = Modifier.size((24 + progress * 6).dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (16 + progress * 2).sp
            )
            
            if (alignment == Alignment.CenterEnd) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = Color.White,
                    modifier = Modifier.size((24 + progress * 6).dp)
                )
            }
        }
    }
}

@Composable
fun EmailItemAdvanced(
    email: Email,
    swipeState: SwipeState,
    swipeDirection: SwipeDirection,
    onMarkAsRead: () -> Unit,
    onStar: () -> Unit
) {
    // Efectos visuales basados en el estado de deslizamiento
    val cardElevation = when (swipeState) {
        SwipeState.Swiping -> 6.dp
        SwipeState.End -> 0.dp  // Desaparece visualmente
        else -> 2.dp
    }
    
    val backgroundColor = when {
        swipeState == SwipeState.Swiping && swipeDirection == SwipeDirection.Left ->
            Color.Red.copy(alpha = 0.05f)
        swipeState == SwipeState.Swiping && swipeDirection == SwipeDirection.Right ->
            Color.Gray.copy(alpha = 0.05f)
        email.isUnread -> Color.White
        else -> Color(0xFFF5F5F5)
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(), // Animación suave si el contenido cambia
        elevation = cardElevation,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (email.isUnread) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(MaterialTheme.colors.primary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    
                    Text(
                        text = email.sender,
                        fontWeight = if (email.isUnread) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                
                Row {
                    if (email.hasAttachment) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Adjunto",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    
                    Text(
                        text = email.time,
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = email.subject,
                fontWeight = if (email.isUnread) FontWeight.SemiBold else FontWeight.Normal,
                style = MaterialTheme.typography.body1
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = email.preview,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2,
                color = Color.DarkGray
            )
            
            if (email.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    email.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = tag.color.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tag.name,
                                color = tag.color,
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
            }
        }
    }
}
```
## Preguntas frecuentes
#### ¿Cómo puedo manejar acciones múltiples en un deslizamiento?
Puedes usar diferentes umbrales para distintas acciones:
```kotlin
KmpSwipe(
    // ...
    onSwipe = { direction, offset ->
        if (direction == SwipeDirection.Left && offset.value > 150f) {
            // Acción adicional al deslizar más allá de cierto punto
        }
    }
) { swipeState, swipeDirection ->
    // Contenido
}
```
#### ¿Cómo puedo guardar el estado de deslizamiento?
Puedes persistir el estado con rememberSaveable:
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
        // Otras acciones
    }
) { swipeState, swipeDirection ->
    // Usar persistedState.value y persistedDirection.value si es necesario
    // para mantener el estado a través de recomposiciones
}
```
#### ¿Es posible personalizar la animación de retorno?
La animación de retorno usa los parámetros springStiffness y dampingRatio:
```kotlin
KmpSwipe(
    // ...
    springStiffness = 800f,  // Más rápido
    dampingRatio = Spring.DampingRatioNoBouncy,  // Sin rebote
) { swipeState, swipeDirection ->
    // Contenido
}
```
#### ¿Cómo puedo deshabilitar el deslizamiento condicionalmente?
Usa el parámetro enabled:
```kotlin
KmpSwipe(
    // ...
    enabled = !isLoading && itemIsSwipeable,
) { swipeState, swipeDirection ->
    // Contenido
}
```
#### ¿Cómo puedo obtener la velocidad del deslizamiento?
Usa el callback onSwipeVelocity:
```kotlin
KmpSwipe(
    // ...
    onSwipeVelocity = { velocity ->
        if (abs(velocity) > 1000f) {
            // Deslizamiento rápido, podrías hacer una animación especial
        }
    }
) { swipeState, swipeDirection ->
    // Contenido
}
```
## Contribución
*KMPSwipe* es un proyecto de código abierto y las contribuciones son bienvenidas. Para contribuir:
1. Haz un fork del repositorio
2. Crea una rama para tu característica (git checkout -b feature/amazing-feature)
3. Haz commit de tus cambios (git commit -m 'Add some amazing feature')
4. Push a la rama (git push origin feature/amazing-feature)
5. Abre un Pull Request

## Licencia
*KMPSwipe* está licenciado bajo la licencia *MIT.* Consulta el archivo *LICENSE* para más detalles.


