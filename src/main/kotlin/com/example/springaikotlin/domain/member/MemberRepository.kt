package com.example.springaikotlin.domain.member

fun interface MemberRepository {

    fun findTopByIdAndDeletedAtIsNullOrderByIdDesc(
        id: Long
    ): Member?
}
