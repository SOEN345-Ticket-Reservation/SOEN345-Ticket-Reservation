package com.example.frontend.ui.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.model.UserResponse

@Composable
fun LoginScreen(
    onLoginSuccess: (UserResponse) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    AuthScreenLayout(
        title = "Login",
        formContent = {
            AuthTextField(
                value = email,
                onValueChange = { viewModel.email.value = it },
                label = "Email or phone"
            )
            Spacer(modifier = Modifier.height(12.dp))
            AuthTextField(
                value = password,
                onValueChange = { viewModel.password.value = it },
                label = "Password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            if (isLoading) {
                Spacer(modifier = Modifier.height(12.dp))
                CircularProgressIndicator()
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                AuthButton(
                    text = "Login",
                    onClick = { viewModel.login(onLoginSuccess) },
                    enabled = email.isNotBlank() && password.isNotBlank()
                )
            }
        },
        footerContent = {
            AuthFooter(
                text = "Don't have an account? ",
                linkText = "Register",
                onClickLink = {
                    viewModel.clearError()
                    onNavigateToRegister()
                }
            )
        }
    )
}
