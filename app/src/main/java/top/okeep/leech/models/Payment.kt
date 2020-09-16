package top.okeep.leech.models

import androidx.room.*
import java.util.*

@Entity(
    tableName = "payments",
    foreignKeys = [ForeignKey(
        entity = Subject::class,
        parentColumns = ["id"],
        childColumns = ["subjectId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["subjectId"]), Index(value = ["paidStamp"])]
)
data class Payment(
    @PrimaryKey val id: Long,
    val subjectId: Long,
    val payAmount: Double,
    val paidStamp: Long,
    val description: String
) {
    var milliStamp = Long.MIN_VALUE

    companion object {
        fun newInstance(pId: Long, amount: Double, payStamp: Long): Payment {
            return Payment(
                id = System.currentTimeMillis(),
                subjectId = pId,
                payAmount = amount,
                paidStamp = payStamp,
                description = ""
            )
        }
    }

    val payYMDString: String
        get() {
            val now = Calendar.getInstance()
            now.timeInMillis = paidStamp
            return "${now.get(Calendar.YEAR)}-${now.get(Calendar.MONTH)}-${now.get(Calendar.DAY_OF_MONTH)}"
        }

    @Ignore
    val amountString = "%.2f".format(payAmount)
}