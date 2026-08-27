package com.example.springaikotlin.infrastructure.persistence.jpa.member

import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataMemberRepository : JpaRepository<MemberEntity, Long> {

    fun findTopByIdAndDeletedAtIsNullOrderByIdDesc(
        id: Long
    ): MemberEntity?
}
