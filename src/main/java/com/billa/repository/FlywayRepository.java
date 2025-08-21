package com.billa.repository;


import com.billa.jooq.tables.pojos.Orders;
import com.billa.jooq.tables.records.OrdersRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.billa.jooq.tables.Orders.ORDERS;

@Repository
public class FlywayRepository {

    private final DSLContext ctx;

    public FlywayRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Orders save(Orders order) {
        OrdersRecord record = ctx.insertInto(ORDERS)
                .set(ORDERS.PERSON_ID, order.getPersonId())
                .set(ORDERS.AMOUNT, order.getAmount())
                .returning()
                .fetchOne();

        assert record != null;
        return record.into(Orders.class);
    }
}
