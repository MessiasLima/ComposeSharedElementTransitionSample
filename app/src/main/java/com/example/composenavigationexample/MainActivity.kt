package com.example.composenavigationexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.composenavigationexample.ui.theme.ComposeNavigationExampleTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeNavigationExampleTheme {
                Scaffold { padding ->
                    NavigationRoot(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

private val nameList = listOf(
    "Torchic", "Chikorita", "Jolteon", "Magnamite", "Slugma", "Blastoise"
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    SharedTransitionLayout {
        NavHost(modifier = modifier,
            navController = navController,
            startDestination = ListScreenDestination,
            enterTransition = { slideIn(initialOffset = { IntOffset(x = it.width, y = 0) }) },
            exitTransition = {
                slideOut(targetOffset = {
                    IntOffset(
                        x = (it.width / 2) * -1, y = 0
                    )
                }) + fadeOut()
            },
            popEnterTransition = {
                slideIn(initialOffset = {
                    IntOffset(
                        x = it.width * -1, y = 0
                    )
                })
            },
            popExitTransition = {
                slideOut(targetOffset = {
                    IntOffset(
                        x = it.width / 2, y = 0
                    )
                }) + fadeOut()
            }) {
            composable<ListScreenDestination> {
                ListScreen(
                    onNameClicked = {
                        navController.navigate(DetailScreenDestination(it))
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable
                )
            }

            composable<DetailScreenDestination> { backStackEntry ->
                val args = backStackEntry.toRoute<DetailScreenDestination>()
                DetailScreen(args.name,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    onNavigateBack = {
                        navController.navigateUp()
                    })
            }
        }
    }
}

@Serializable
data object ListScreenDestination

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    onNameClicked: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "The best Pokemon in the world",
            style = MaterialTheme.typography.headlineLarge
        )

        with(sharedTransitionScope) {
            nameList.forEach { name ->
                Row(modifier = Modifier
                    .clickable {
                        onNameClicked(name)
                    }
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = "icon-$name"),
                            animatedVisibilityScope = animatedContentScope,
                        ), imageVector = Icons.Default.AccountCircle, contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = "name-$name"),
                                animatedVisibilityScope = animatedContentScope,
                            )
                            .fillMaxWidth(),
                        text = name,
                    )
                }
            }
        }
    }
}

@Serializable
data class DetailScreenDestination(val name: String)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailScreen(
    name: String,
    sharedTransitionScope: SharedTransitionScope,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    animatedContentScope: AnimatedContentScope,
) {

    Column(
        modifier = modifier.clickable { onNavigateBack() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        with(sharedTransitionScope) {
            Image(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "icon-$name"),
                        animatedVisibilityScope = animatedContentScope,
                    )
                    .size(64.dp),
                imageVector = Icons.Default.AccountCircle, contentDescription = null
            )
            Text(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "name-$name"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .padding(16.dp), text = name, style = MaterialTheme.typography.headlineLarge
            )
        }

        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = "$name is a good friend",
        )
    }

}