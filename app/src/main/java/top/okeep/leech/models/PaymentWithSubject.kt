package top.okeep.leech.models

import androidx.room.DatabaseView
import androidx.room.Ignore
import java.util.*

@DatabaseView(
    value = "SELECT s.title, s.enabled, p.subjectId, " +
            " p.id AS paymentId, p.payAmount, p.paidStamp" +
            " FROM subjects AS s INNER JOIN payments AS p ON s.id = p.subjectId",
    viewName = "payment_with_subject"
)
data class PaymentWithSubject(
    val title: String,
    val enabled: Boolean,
    val subjectId: Long,
    val paymentId: Long,
    val payAmount: Double,
    val paidStamp: Long
) {
    val payYMDString: String
        get() {
            val now = Calendar.getInstance()
            now.timeInMillis = paidStamp
            return "${now.get(Calendar.YEAR)}-${now.get(Calendar.MONTH)}-${now.get(Calendar.DAY_OF_MONTH)}"
        }

    @Ignore
    val amountString = "%.2f".format(payAmount)
}