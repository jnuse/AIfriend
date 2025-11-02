package me.rerere.rikkahub.ui.components.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import me.rerere.rikkahub.R
import java.util.Calendar

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium
) {
    @Composable
    fun getGreetingMessage(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> stringResource(id = R.string.greeting_morning)
            in 12..17 -> stringResource(id = R.string.greeting_afternoon)
            in 18..22 -> stringResource(id = R.string.greeting_evening)
            else -> stringResource(id = R.string.greeting_night)
        }
    }

    Text(
        text = getGreetingMessage(),
        style = style,
        modifier = modifier
    )
}
