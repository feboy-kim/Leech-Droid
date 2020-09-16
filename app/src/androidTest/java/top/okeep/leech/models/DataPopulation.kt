package top.okeep.leech.models

import kotlinx.coroutines.runBlocking

object DataPopulation {
    val vp1 = Subject(
        id = 21L,
        title = "Subject A1",
        enabled = true,
        remark = "Subject A description"
    )
    val vp2 = Subject(
        id = 22L,
        title = "Subject B2",
        enabled = false,
        remark = "Subject B description"
    )
    val p1_m1 = Payment(
        id = 101L,
        subjectId = vp1.id,
        payAmount = 101.1,
        paidStamp = 10101L,
        description = "payment 1 description"
    )
    val p1_m2 = Payment(
        id = 102L,
        subjectId = vp1.id,
        payAmount = -102.1,
        paidStamp = 10102L,
        description = "payment 2 description"
    )
    val p2_m1 = Payment(
        id = 103L,
        subjectId = vp2.id,
        payAmount = 101.1,
        paidStamp = 10201L,
        description = "payment 1 description"
    )
    val p2_m2 = Payment(
        id = 104L,
        subjectId = vp2.id,
        payAmount = -102.1,
        paidStamp = 10202L,
        description = "payment 2 description"
    )
    const val NONEXISTENT = 12345L

    fun populate(db: AppDatabase) {
        runBlocking {
            db.subjectAccessor().insertOne(vp1)
            db.subjectAccessor().insertOne(vp2)
            db.paymentAccessor().insertOne(p1_m1)
            db.paymentAccessor().insertOne(p1_m2)
            db.paymentAccessor().insertOne(p2_m1)
            db.paymentAccessor().insertOne(p2_m2)
        }
    }
}