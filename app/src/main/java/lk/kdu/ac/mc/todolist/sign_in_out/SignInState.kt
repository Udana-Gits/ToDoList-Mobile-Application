package lk.kdu.ac.mc.todolist.sign_in_out

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
