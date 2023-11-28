package elice.team5th.domain.tmdb.dto

import elice.team5th.domain.tmdb.entity.ActorEntity

data class ActorDto(
    val id: Long?,
    val name: String?,
    val profilePath: String?
) {
    constructor(actor: ActorEntity) : this(
        id = actor.id,
        name = actor.name,
        profilePath = actor.profilePath
    )
}
