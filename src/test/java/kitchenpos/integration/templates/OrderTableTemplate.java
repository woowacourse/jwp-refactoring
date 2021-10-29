package kitchenpos.integration.templates;

import kitchenpos.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderTableTemplate extends IntegrationTemplate {

    public static final String TABLE_URL = "/api/tables";
    public static final String EMPTY_TABLE_URL = TABLE_URL + "/{orderTableId}/empty";
    public static final String NUMBER_OF_GUESTS_TABLE_URL = TABLE_URL + "/{orderTableId}/number-of-guests";

    public ResponseEntity<OrderTable> create(int numberOfGuests, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);

        return post(
                TABLE_URL,
                orderTable,
                OrderTable.class
        );
    }

    public ResponseEntity<OrderTable[]> list() {
        return get(
                TABLE_URL,
                OrderTable[].class
        );
    }

    public ResponseEntity<OrderTable> changeEmpty(Long orderTableId, OrderTable orderTable) {
        return changeOrderTable(
                EMPTY_TABLE_URL,
                orderTableId,
                orderTable
        );
    }

    public ResponseEntity<OrderTable> changeNumberOfGuests(Long orderTableId, OrderTable orderTable) {
        return changeOrderTable(
                NUMBER_OF_GUESTS_TABLE_URL,
                orderTableId,
                orderTable
        );
    }

    public ResponseEntity<OrderTable> changeOrderTable(String url, Long orderTableId, OrderTable orderTable) {
        return put(
                url,
                orderTableId,
                orderTable,
                OrderTable.class
        );
    }
}
