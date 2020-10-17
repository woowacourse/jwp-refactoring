package kitchenpos.factory;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderTable;

@Component
public class OrderTableFactory {
    public OrderTable create(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public OrderTable create(Long id) {
        return create(id, null, 0, false);
    }

    public OrderTable create(boolean empty) {
        return create(null, null, 0, empty);
    }

    public OrderTable create(int numberOfGuests, boolean empty) {
        return create(null, null, numberOfGuests, empty);
    }

    public OrderTable create(int numberOfGuests) {
        return create(null, null, numberOfGuests, false);
    }

    public OrderTable create(Long id, boolean empty) {
        return create(id, null, 0, empty);
    }

    public OrderTable create(Long id, Long tableGroupId, boolean empty) {
        return create(id, tableGroupId, 0, empty);
    }
}

