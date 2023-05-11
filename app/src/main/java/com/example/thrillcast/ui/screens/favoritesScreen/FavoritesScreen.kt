
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thrillcast.ui.viewmodels.favorites.FavoriteViewModel
import com.example.thrillcast.ui.viewmodels.weather.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favoriteViewModel: FavoriteViewModel,
    weatherViewModel: WeatherViewModel,
    navigateBack: () -> Unit,
    context: Context
) {
    val favoriteUiState = favoriteViewModel.favoriteUiState.collectAsState()
    weatherViewModel.retrieveFavoritesWeather(favorites = favoriteUiState.value.favoriteList)

    val multiCurrentWeatherUiState = weatherViewModel.multiCurrentWeatherUiState.collectAsState()

    val favoriteAndCurrentWeatherMap = favoriteUiState.value.favoriteList.zip(multiCurrentWeatherUiState.value.currentWeatherList)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = navigateBack
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Icon",
                        //tint = Silver
                    )
                }

                Text(
                    text = "Favorites",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        },

        content = { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favoriteAndCurrentWeatherMap){

                    Text(
                        text = it.first?.name ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    NowWeatherCard(
                        windDirection = it.second.wind?.direction ?: 0,
                        speed = it.second.wind?.speed ?: 0.0,
                        unit = it.second.wind?.unit ?: "m/s",
                        gust = it.second.wind?.gust ?: 0.0,
                        symbolCode = it.second.nowCastObject?.data?.next_1_hours?.summary?.symbol_code ?: "sleetshowersandthunder_polartwilight",
                        temperature = it.second.nowCastObject?.data?.instant?.details?.air_temperature
                            ?: 0.0,
                        greenStart = it.first?.greenStart ?: 0,
                        greenStop = it.first?.greenStop ?: 0,
                        context = context
                    )
                }
            }
        }
    )
}