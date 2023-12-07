package com.elmirov.vkcompose.presentation.main

sealed interface AuthState {

    object Initial : AuthState

    object Authorized : AuthState

    object NoAuthorized : AuthState
}