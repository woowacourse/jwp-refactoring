package kitchenpos.integration.templates;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderTableTemplate {

    public static final String TABLE_URL = "/api/tables";
    public static final String EMPTY_TABLE_URL = TABLE_URL + "/{orderTableId}/empty";
    public static final String NUMBER_OF_GUESTS_TABLE_URL =
        TABLE_URL + "/{orderTableId}/number-of-guests";

    private final IntegrationTemplate integrationTemplate;

    public OrderTableTemplate(IntegrationTemplate integrationTemplate) {
        this.integrationTemplate = integrationTemplate;
    }

    public ResponseEntity<OrderTableResponse> create(int numberOfGuests, boolean isEmpty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(null, null, numberOfGuests, isEmpty);

        return integrationTemplate.post(
            TABLE_URL,
            orderTableRequest,
            OrderTableResponse.class
        );
    }

    public ResponseEntity<OrderTable[]> list() {
        return integrationTemplate.get(
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

    public ResponseEntity<OrderTable> changeNumberOfGuests(Long orderTableId,
                                                           OrderTable orderTable) {
        return changeOrderTable(
            NUMBER_OF_GUESTS_TABLE_URL,
            orderTableId,
            orderTable
        );
    }

    public ResponseEntity<OrderTable> changeOrderTable(String url,
                                                       Long orderTableId,
                                                       OrderTable orderTable) {
        return integrationTemplate.put(
            url,
            orderTableId,
            orderTable,
            OrderTable.class
        );
    }
}
