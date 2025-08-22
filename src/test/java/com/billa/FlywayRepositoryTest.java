package com.billa;

import com.billa.jooq.tables.pojos.Orders;
import com.billa.jooq.tables.records.OrdersRecord;
import com.billa.repository.FlywayRepository;
import org.jooq.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.billa.jooq.Tables.ORDERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlywayRepositoryTest {

    @Mock
    private DSLContext dsl;

    @Mock
    private Batch batch;

    @Mock
    private OrdersRecord record1;

    @Mock
    private OrdersRecord record2;

    @Mock
    private OrdersRecord record;

    // Update mocks
    @Mock
    private UpdateSetFirstStep<OrdersRecord> updateStepFirst;

    @Mock
    private UpdateSetMoreStep<OrdersRecord> updateStepMore;

    @Mock
    private UpdateConditionStep<OrdersRecord> updateConditionStep;

    // Select mocks
    @Mock
    private SelectJoinStep<OrdersRecord> selectStep;

    @InjectMocks
    private FlywayRepository repository;

    @Test
    void testSave() {
        Orders input = new Orders();
        input.setPersonId(10L);
        input.setAmount(500);

        Orders expected = new Orders();
        expected.setId(1L);
        expected.setPersonId(10L);
        expected.setAmount(500);

        when(dsl.newRecord(ORDERS, input)).thenReturn(record);
        when(record.store()).thenReturn(1);
        doNothing().when(record).refresh();
        when(record.into(Orders.class)).thenReturn(expected);

        Orders actual = repository.save(input);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPersonId(), actual.getPersonId());
        assertEquals(expected.getAmount(), actual.getAmount());

        verify(dsl).newRecord(ORDERS, input);
        verify(record).store();
        verify(record).refresh();
        verify(record).into(Orders.class);
    }

    @Test
    void testSaveAll() {
        Orders order1 = new Orders();
        order1.setPersonId(1L);
        order1.setAmount(100);

        Orders order2 = new Orders();
        order2.setPersonId(2L);
        order2.setAmount(200);

        List<Orders> input = List.of(order1, order2);

        when(dsl.newRecord(ORDERS, order1)).thenReturn(record1);
        when(dsl.newRecord(ORDERS, order2)).thenReturn(record2);

        when(dsl.batchStore(List.of(record1, record2))).thenReturn(batch);
        when(batch.execute()).thenReturn(new int[]{1, 1});

        doNothing().when(record1).refresh();
        doNothing().when(record2).refresh();

        Orders saved1 = new Orders();
        saved1.setId(101L);
        saved1.setPersonId(1L);
        saved1.setAmount(100);

        Orders saved2 = new Orders();
        saved2.setId(102L);
        saved2.setPersonId(2L);
        saved2.setAmount(200);

        when(record1.into(Orders.class)).thenReturn(saved1);
        when(record2.into(Orders.class)).thenReturn(saved2);

        List<Orders> actual = repository.saveAll(input);

        assertEquals(2, actual.size());
        assertEquals(101L, actual.get(0).getId());
        assertEquals(102L, actual.get(1).getId());

        verify(dsl).newRecord(ORDERS, order1);
        verify(dsl).newRecord(ORDERS, order2);
        verify(dsl).batchStore(List.of(record1, record2));
        verify(batch).execute();
        verify(record1).refresh();
        verify(record2).refresh();
        verify(record1).into(Orders.class);
        verify(record2).into(Orders.class);
    }

    @Test
    void testFindAll() {
        Orders order1 = new Orders();
        order1.setId(1L);
        order1.setPersonId(10L);
        order1.setAmount(500);

        Orders order2 = new Orders();
        order2.setId(2L);
        order2.setPersonId(20L);
        order2.setAmount(1000);

        List<Orders> expected = List.of(order1, order2);

        when(dsl.selectFrom(ORDERS)).thenReturn(selectStep);
        when(selectStep.fetchInto(Orders.class)).thenReturn(expected);

        List<Orders> actual = repository.findAll();

        assertEquals(2, actual.size());
        assertEquals(expected, actual);

        verify(dsl).selectFrom(ORDERS);
        verify(selectStep).fetchInto(Orders.class);
    }

    @Test
    void testFindById() {
        Long orderId = 1L;

        Orders expected = new Orders();
        expected.setId(orderId);
        expected.setPersonId(10L);
        expected.setAmount(500);

        SelectWhereStep<OrdersRecord> selectFromStep = mock(SelectWhereStep.class);
        SelectConditionStep<OrdersRecord> whereStep = mock(SelectConditionStep.class);

        when(dsl.selectFrom(ORDERS)).thenReturn(selectFromStep);
        when(selectFromStep.where(ORDERS.ID.eq(orderId))).thenReturn(whereStep);
        when(whereStep.fetchOneInto(Orders.class)).thenReturn(expected);

        Orders actual = repository.findById(orderId);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPersonId(), actual.getPersonId());
        assertEquals(expected.getAmount(), actual.getAmount());
    }

    @Test
    void testUpdate() {
        Orders input = new Orders();
        input.setId(1L);
        input.setPersonId(20L);
        input.setAmount(1000);

        when(dsl.update(ORDERS)).thenReturn(updateStepFirst);
        when(updateStepFirst.set(ORDERS.PERSON_ID, input.getPersonId())).thenReturn(updateStepMore);
        when(updateStepMore.set(ORDERS.AMOUNT, input.getAmount())).thenReturn(updateStepMore);
        when(updateStepMore.where(ORDERS.ID.eq(input.getId()))).thenReturn(updateConditionStep);
        when(updateConditionStep.execute()).thenReturn(1);

        Orders updatedOrder = new Orders();
        updatedOrder.setId(1L);
        updatedOrder.setPersonId(20L);
        updatedOrder.setAmount(1000);

        SelectWhereStep<OrdersRecord> selectFromStep = mock(SelectWhereStep.class);
        SelectConditionStep<OrdersRecord> whereStep = mock(SelectConditionStep.class);

        when(dsl.selectFrom(ORDERS)).thenReturn(selectFromStep);
        when(selectFromStep.where(ORDERS.ID.eq(input.getId()))).thenReturn(whereStep);
        when(whereStep.fetchOneInto(Orders.class)).thenReturn(updatedOrder);

        Orders result = repository.update(input);

        assertEquals(updatedOrder.getId(), result.getId());
        assertEquals(updatedOrder.getPersonId(), result.getPersonId());
        assertEquals(updatedOrder.getAmount(), result.getAmount());

        verify(dsl).update(ORDERS);
        verify(updateStepFirst).set(ORDERS.PERSON_ID, input.getPersonId());
        verify(updateStepMore).set(ORDERS.AMOUNT, input.getAmount());
        verify(updateStepMore).where(ORDERS.ID.eq(input.getId()));
        verify(updateConditionStep).execute();
    }

    @Test
    void testDeleteById() {
        Long orderId = 1L;

        // Mock DeleteWhereStep and DeleteConditionStep
        DeleteUsingStep<OrdersRecord> deleteUsingStep = mock(DeleteUsingStep.class);
        DeleteConditionStep<OrdersRecord> deleteConditionStep = mock(DeleteConditionStep.class);

        // Mock chain: dsl.deleteFrom -> where -> execute
        when(dsl.deleteFrom(ORDERS)).thenReturn(deleteUsingStep);
        when(deleteUsingStep.where(ORDERS.ID.eq(orderId))).thenReturn(deleteConditionStep);
        when(deleteConditionStep.execute()).thenReturn(1);

        // Call repository method
        int rowsDeleted = repository.deleteById(orderId);

        // Assertions
        assertEquals(1, rowsDeleted);

        // Verify interactions
        verify(dsl).deleteFrom(ORDERS);
        verify(deleteUsingStep).where(ORDERS.ID.eq(orderId));
        verify(deleteConditionStep).execute();
    }

}
