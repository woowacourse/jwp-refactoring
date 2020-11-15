package kitchenpos.application.fixture;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static List<OrderTable> createSavedOrderTableWithCount(int count, Long tableGroupId, int guest) {
        return LongStream.range(1, count + 1)
            .mapToObj((i) -> {
                OrderTable orderTable = new OrderTable();
                orderTable.setId(i);
                orderTable.setEmpty(false);
                orderTable.setTableGroupId(tableGroupId);
                orderTable.setNumberOfGuests(guest);
                return orderTable;
            })
            .collect(Collectors.toList());
    }

    public static List<OrderTable> createOrderTableCountBy(int count) {
        return LongStream.range(1, count + 1)
            .mapToObj((i) -> {
                OrderTable orderTable = new OrderTable();
                orderTable.setId(i);
                orderTable.setTableGroupId(i);
                orderTable.setEmpty(true);
                return orderTable;
            })
            .collect(Collectors.toList());
    }

    public static List<OrderTable> createOrderTableWithIdAndNotEmptyCountBy(int count) {
        return LongStream.range(1, count + 1)
            .mapToObj((i) -> {
                OrderTable orderTable = new OrderTable();
                orderTable.setId(i);
                orderTable.setEmpty(false);
                return orderTable;
            })
            .collect(Collectors.toList());
    }

    public static OrderTable createSavedWithTableGroupId(long id, long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(tableGroupId);

        return orderTable;
    }

    public static OrderTable createBeforeSave() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable createGroupedOrderTable(Long groupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(groupId);
        orderTable.setEmpty(true);
        return orderTable;
    }
}
