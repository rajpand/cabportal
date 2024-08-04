package com.varcassoftware.ridercabapp.response

data class ResponseCode(val code: Int, val message: String) {
    companion object {
        private const val SUCCESS = 200
        private const val CREATED = 201
        private const val ACCEPTED = 202
        private const val NO_CONTENT = 204
        private const val BAD_REQUEST = 400
        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
        private const val NOT_FOUND = 404
        private const val METHOD_NOT_ALLOWED = 405
        private const val CONFLICT = 409
        private const val INTERNAL_SERVER_ERROR = 500
        private const val SERVICE_UNAVAILABLE = 503

        fun getResponseErrorMessageAccordingToCode(code: Int): String {
            return when (code) {
                SUCCESS -> "Success"
                CREATED -> "Created"
                ACCEPTED -> "Accepted"
                NO_CONTENT -> "No Content"
                BAD_REQUEST -> "Bad Request"
                UNAUTHORIZED -> "Unauthorized"
                FORBIDDEN -> "Forbidden"
                NOT_FOUND -> "Not Found"
                METHOD_NOT_ALLOWED -> "Method Not Allowed"
                CONFLICT -> "Conflict"
                INTERNAL_SERVER_ERROR -> "Internal Server Error"
                SERVICE_UNAVAILABLE -> "Service Unavailable"
                else -> "Unknown Error"
            }
        }
    }
}
