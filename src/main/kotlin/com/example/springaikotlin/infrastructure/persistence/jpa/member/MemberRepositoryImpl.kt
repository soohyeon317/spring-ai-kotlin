package com.example.springaikotlin.infrastructure.persistence.jpa.member

import com.example.springaikotlin.domain.member.Member
import com.example.springaikotlin.domain.member.MemberRepository
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val springDataMemberRepository: SpringDataMemberRepository
) : MemberRepository {

    override fun findTopByIdAndDeletedAtIsNullOrderByIdDesc(id: Long): Member? {
        return springDataMemberRepository.findTopByIdAndDeletedAtIsNullOrderByIdDesc(
            id = id
        )?.toDomain()
    }
}
