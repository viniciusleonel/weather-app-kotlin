package screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.weatherapp.R
import br.com.fiap.weatherapp.model.Forecast
import com.google.gson.Gson
import br.com.fiap.weatherapp.model.WeatherResponse
import br.com.fiap.weatherapp.model.WeatherResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import service.RetrofitClient


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun consultaCidades(navController: NavController) {
    var textoBusca by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var tempAtual: Int by remember { mutableStateOf(-100) }
    var tempMinima: Int by remember { mutableStateOf(-100) }
    var tempMaxima: Int by remember { mutableStateOf(100) }
    var probChuva: Int by remember { mutableStateOf(-1) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Chaves API para testes: 8ed1e29b - ad9170ec
    val apiKey = "8ed1e29b"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        val iconPainter = chooseImage(tempAtual)
        Image(
            painter = iconPainter,
            contentDescription = "Icone do tempo",
            modifier = Modifier
                .size(150.dp)
        )

        // Texto
        Text(
            text = "Descubra a previsão do tempo de hoje!",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            lineHeight = 35.sp,
            modifier = Modifier
                .padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = textoBusca,
            onValueChange = { textoBusca = it },
            label = { Text("Digite o nome da cidade") },
            placeholder = { Text(text = "Exemplo: São Paulo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {  })
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                RetrofitClient.instance.getWeather(apiKey, textoBusca)
                    .enqueue(object : Callback<WeatherResponse> {
                        override fun onResponse(
                            call: Call<WeatherResponse>,
                            response: Response<WeatherResponse>
                        ) {
                            if (response.isSuccessful) {
                                val weatherResponse = response.body()

                                if (weatherResponse?.results?.city_name != null) {
                                    cidade = weatherResponse?.results?.city_name
                                }

                                if (weatherResponse?.results?.date != null) {
                                    data = weatherResponse?.results?.date
                                }

                                if (weatherResponse?.results?.temp != null) {
                                    tempAtual = weatherResponse?.results?.temp
                                }

                                if (weatherResponse?.results?.description != null) {
                                    descricao = "Agora: " + weatherResponse?.results?.description
                                }

                                var dataAtual = weatherResponse?.results?.forecast?.get(0)

                                if (dataAtual?.min != null) {
                                    tempMinima = dataAtual.min
                                }
                                if (dataAtual?.max != null) {
                                    tempMaxima = dataAtual.max
                                }
                                if (dataAtual?.rain_probability != null) {
                                    probChuva = dataAtual.rain_probability
                                }
                            }
                        }

                        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {

                        }
                    })

                keyboardController?.hide()
            },
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF298DDD),
                contentColor = Color.White,
            )

        ) {
            Text(text = "Consultar",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(120.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                navController.navigate("TelaInicial")
            },
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF298DDD),
                contentColor = Color.White,
            )

        ) {

            Text(text = "Voltar",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(120.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {

            Text(text = cidade,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = data,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)

            Spacer(modifier = Modifier.height(5.dp))

            Row {
                if (tempMinima > -100) {
                    Text("Min: " + tempMinima.toString() + "º ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    var minIcon = chooseImage(tempMinima)
                    Image(
                        painter = minIcon,
                        contentDescription = "Icone do tempo",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row {
                if (tempMaxima < 100) {
                    Text(text = "Max: " + tempMaxima.toString() + "º ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    var maxIcon = chooseImage(tempMaxima)
                    Image(
                        painter = maxIcon,
                        contentDescription = "Icone do tempo",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row {
                if (tempAtual > -100) {
                    Text(text = "Temperatura: " + tempAtual.toString() + "º ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    var tempIcon = chooseImage(tempAtual)
                    Image(
                        painter = tempIcon,
                        contentDescription = "Icone do tempo",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = descricao,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                if (probChuva > -1) {
                    Text("Probabilidade de chuva: " + probChuva.toString() + "% ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Image(
                        painter = painterResource(id = R.drawable.ic_chuva_forte), // Substitua com o ID do recurso do seu ícone de temperatura máxima
                        contentDescription = "probabilidade de chuva",
                        modifier = Modifier.size(25.dp) // Tamanho do ícone
                    )
                }
            }

        }
    }
}

@Composable
fun chooseImage(temp: Int): Painter {
    return when {
        temp > 27 -> painterResource(id = R.drawable.ic_sol_forte)
        temp > 20 -> painterResource(id = R.drawable.ic_boas_vindas)
        temp > 10 -> painterResource(id = R.drawable.ic_chuva)
        else -> painterResource(id = R.drawable.ic_chuva_forte)
    }
}
