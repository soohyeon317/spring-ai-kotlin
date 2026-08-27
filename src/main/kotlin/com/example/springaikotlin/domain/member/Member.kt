package com.example.springaikotlin.domain.member

import java.time.LocalDateTime

data class Member (
    val id: Long?,
    val email: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {

    constructor(
        email: String,
    ): this(
        id = null,
        email = email,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
}
