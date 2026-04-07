package com.example.frontend.ui.auth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.model.UserResponse

@Composable
fun RegisterScreen(
    onRegisterSuccess: (UserResponse) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val password by viewModel.password.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    AuthScreenLayout(
        title = "Register",
        formContent = {
            AuthTextField(
                value = name,
                onValueChange = { viewModel.name.value = it },
                label = "Name"
            )
            Spacer(modifier = Modifier.height(12.dp))
            AuthTextField(
                value = email,
                onValueChange = { viewModel.email.value = it },
                label = "Email"
            )
            Spacer(modifier = Modifier.height(12.dp))
            AuthTextField(
                value = phone,
                onValueChange = { viewModel.phone.value = it },
                label = "Phone (optional)"
            )
            Spacer(modifier = Modifier.height(12.dp))
            AuthTextField(
                value = password,
                onValueChange = { viewModel.password.value = it },
                label = "Password",
                isPassword = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isAdmin,
                    onCheckedChange = { viewModel.isAdmin.value = it }
                )
                Text("Register as Admin")
            }
            Spacer(modifier = Modifier.height(4.dp))
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            if (isLoading) {
                Spacer(modifier = Modifier.height(12.dp))
                CircularProgressIndicator()
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                AuthButton(
                    text = "Register",
                    onClick = { viewModel.register(onRegisterSuccess) },
                    enabled = name.isNotBlank() && email.isNotBlank() && password.length >= 6
                )
            }
        },
        footerContent = {
            AuthFooter(
                text = "Already have an account? ",
                linkText = "Login",
                onClickLink = {
                    viewModel.clearError()
                    onNavigateToLogin()
                }
            )
        }
    )
}
