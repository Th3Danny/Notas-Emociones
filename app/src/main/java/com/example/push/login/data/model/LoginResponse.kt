data class LoginResponse(
    val data: LoginDataResponse,
    val message: String,
    val success: Boolean,
    val http_status: String
)


data class LoginDataResponse(
    val id_user: Int,
    val access_token: String,
    val refresh_token: String,
    val fcm: String,
    val roles: List<Role>,
    val name: String,
    val username: String
)

data class Role(
    val name: String
)
