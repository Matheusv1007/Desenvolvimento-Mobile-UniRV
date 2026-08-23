package com.unirv.jogodavelha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

sealed interface Route {
    data object Login : Route
    data object Registration : Route
    data object Confirmation : Route
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<Route>(Route.Login) }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLast()
            }
        },
        transitionSpec = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { largura -> largura }
            ) + fadeIn() togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(400),
                        targetOffsetX = { largura -> -largura }
                    ) + fadeOut()
        },
        popTransitionSpec = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { largura -> -largura }
            ) + fadeIn() togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(400),
                        targetOffsetX = { largura -> largura }
                    ) + fadeOut()
        },
        entryProvider = { route ->
            when (route) {
                Route.Login -> NavEntry(route) {
                    LoginScreen(
                        onRegistration = { backStack.add(Route.Registration) }
                    )
                }

                Route.Registration -> NavEntry(route) {
                    RegistrationScreen(
                        onBack = { backStack.removeLast() },
                        onSubmit = { backStack.add(Route.Confirmation) }
                    )
                }

                Route.Confirmation -> NavEntry(route) {
                    ConfirmationScreen(
                        onConfirm = {
                            backStack.clear()
                            backStack.add(Route.Login)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun LoginScreen(onRegistration: () -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(112.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "UniRV",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Acesse sua conta",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Login") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        TextButton(
            onClick = {},
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Esqueci minha senha")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Entrar")
        }

        Text(
            text = "ou",
            modifier = Modifier.padding(vertical = 14.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Entrar com Facebook")
        }

        Spacer(modifier = Modifier.height(36.dp))

        OutlinedButton(
            onClick = onRegistration,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Cadastro")
        }
    }
}

@Composable
fun RegistrationScreen(
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var sobrenome by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var comprador by remember { mutableStateOf(true) }
    var vendedor by remember { mutableStateOf(false) }
    var receberNoticias by remember { mutableStateOf(true) }
    var tentouEnviar by remember { mutableStateOf(false) }

    val camposObrigatoriosValidos = usuario.isNotBlank() &&
            senha.isNotBlank() &&
            email.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("‹ Voltar")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Cadastro",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(76.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Login *") },
            singleLine = true,
            isError = tentouEnviar && usuario.isBlank()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Senha *") },
            singleLine = true,
            isError = tentouEnviar && senha.isBlank(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("E-mail *") },
            singleLine = true,
            isError = tentouEnviar && email.isBlank(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nome") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = sobrenome,
            onValueChange = { sobrenome = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Sobrenome") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = dataNascimento,
            onValueChange = { dataNascimento = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Data de nascimento") },
            placeholder = { Text("dd/mm/aaaa") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = telefone,
            onValueChange = { telefone = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Telefone") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = endereco,
            onValueChange = { endereco = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Endereço") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        CheckOption(
            text = "Quero comprar",
            checked = comprador,
            onCheckedChange = { comprador = it }
        )
        CheckOption(
            text = "Quero vender",
            checked = vendedor,
            onCheckedChange = { vendedor = it }
        )
        CheckOption(
            text = "Quero receber notícias",
            checked = receberNoticias,
            onCheckedChange = { receberNoticias = it }
        )

        if (tentouEnviar && !camposObrigatoriosValidos) {
            Text(
                text = "Preencha login, senha e e-mail.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                tentouEnviar = true
                if (camposObrigatoriosValidos) {
                    onSubmit()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Enviar")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CheckOption(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text)
    }
}

@Composable
fun ConfirmationScreen(onConfirm: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Obrigado por se cadastrar!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Enviamos uma mensagem para o seu e-mail para concluir o cadastro.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HorizontalDivider(modifier = Modifier.padding(top = 20.dp))

                TextButton(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK")
                }
            }
        }
    }
}
