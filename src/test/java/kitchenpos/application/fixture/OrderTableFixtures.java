package kitchenpos.application.fixture;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableSaveRequest;

public class OrderTableFixtures {

    public static final OrderTable generateOrderTable(final int numberOfGuests, final boolean empty) {
        return generateOrderTable(null, null, numberOfGuests, empty);
    }

    public static final OrderTable generateOrderTable(final Long tableGroupId,
                                                      final int numberOfGuests,
                                                      final boolean empty) {
        return generateOrderTable(null, tableGroupId, numberOfGuests, empty);
    }

    public static final OrderTable generateOrderTable(final Long id, final OrderTable orderTable) {
        return generateOrderTable(
                id,
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public static final OrderTable generateOrderTable(final Long id,
                                                      final Long tableGroupId,
                                                      final int numberOfGuests,
                                                      final boolean empty) {
        try {
            Constructor<OrderTable> constructor = OrderTable.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            OrderTable orderTable = constructor.newInstance();
            Class<? extends OrderTable> clazz = orderTable.getClass();

            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(orderTable, id);

            Field tableGroupIdField = clazz.getDeclaredField("tableGroupId");
            tableGroupIdField.setAccessible(true);
            tableGroupIdField.set(orderTable, tableGroupId);

            Field numberOfGuestsField = clazz.getDeclaredField("numberOfGuests");
            numberOfGuestsField.setAccessible(true);
            numberOfGuestsField.set(orderTable, numberOfGuests);

            Field emptyField = clazz.getDeclaredField("empty");
            emptyField.setAccessible(true);
            emptyField.set(orderTable, empty);

            return orderTable;
        } catch (final Exception e) {
            throw new RuntimeException();
        }
    }

    public static final OrderTableSaveRequest generateOrderTableSaveRequest(final int numberOfGuests,
                                                                            final boolean empty) {
        return new OrderTableSaveRequest(numberOfGuests, empty);
    }
}
