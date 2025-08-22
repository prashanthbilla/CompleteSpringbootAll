package com.billa.repository;


import com.billa.jooq.Tables;
import com.billa.jooq.tables.pojos.Orders;
import com.billa.jooq.tables.records.OrdersRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.billa.jooq.tables.Orders.ORDERS;

@Repository
public class FlywayRepository {

    private final DSLContext ctx;

    public FlywayRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Orders save(Orders order) {
        OrdersRecord record = ctx.newRecord(ORDERS, order);
        record.store();   // performs the insert
        record.refresh();
        return record.into(Orders.class);
    }

    public List<Orders> saveAll(List<Orders> orders) {
        List<OrdersRecord> records = orders.stream()
                .map(order -> ctx.newRecord(ORDERS, order))
                .collect(Collectors.toList());

        // Batch store all records
        ctx.batchStore(records).execute();

        // Refresh records to get generated IDs
        records.forEach(OrdersRecord::refresh);

        // Convert back to Orders POJO
        return records.stream()
                .map(record -> record.into(Orders.class))
                .collect(Collectors.toList());
    }

    public List<Orders> findAll() {
        return ctx.selectFrom(ORDERS)
                .fetchInto(Orders.class);
    }

    public Orders findById(Long id) {
        return ctx
                .selectFrom(Tables.ORDERS)
                .where(Tables.ORDERS.ID.eq(id))
                .fetchOneInto(Orders.class);
    }

    public Orders update(Orders order) {
        // Update the record where ID matches
        int updatedRows = ctx.update(ORDERS)
                .set(ORDERS.PERSON_ID, order.getPersonId())
                .set(ORDERS.AMOUNT, order.getAmount())
                .where(ORDERS.ID.eq(order.getId()))
                .execute();

        if (updatedRows == 0) {
            throw new RuntimeException("No order found with ID " + order.getId());
        }

        // Return the updated object (optional: fetch fresh from DB)
        return findById(order.getId());
    }

    public int deleteById(Long id) {
        return ctx.deleteFrom(ORDERS)
                .where(ORDERS.ID.eq(id))
                .execute();
    }

}
