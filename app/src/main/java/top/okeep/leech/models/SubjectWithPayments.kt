package top.okeep.leech.models

import androidx.room.Embedded
import androidx.room.Relation

data class SubjectWithPayments(
    @Embedded val subject: Subject,
    @Relation(
        parentColumn = "id",
        entityColumn = "subjectId"
    )
    val payments: List<Payment>
)