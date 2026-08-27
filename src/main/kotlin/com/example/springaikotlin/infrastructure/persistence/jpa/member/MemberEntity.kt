package com.example.springaikotlin.infrastructure.persistence.jpa.member

import com.example.springaikotlin.domain.member.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "member")
class MemberEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
    val email: String,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
) {

    constructor(member: Member, willDelete: Boolean = false) :
            this(
                id = member.id,
                email = member.email,
                createdAt = if (member.id == null) {
                    LocalDateTime.now()
                } else {
                    member.createdAt ?: LocalDateTime.now()
                },
                updatedAt = if (member.id != null) {
                    if (willDelete) {
                        member.updatedAt
                    } else {
                        LocalDateTime.now()
                    }
                } else {
                    null
                },
                deletedAt = if (willDelete) {
                    LocalDateTime.now()
                } else {
                    null
                }
            )

    fun toDomain() = Member(
        id = this.id,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}
