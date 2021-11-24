package kitchenpos.ordertable.service;

import java.util.List;
import kitchenpos.ordertable.service.dto.OrderTableRequest;
import kitchenpos.ordertable.service.dto.OrderTableResponse;

public interface OrderTableService {

    OrderTableResponse create(OrderTableRequest orderTableRequest);

    List<OrderTableResponse> list();

    OrderTableResponse changeEmpty(Long orderTableId, boolean empty);

    OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests);
}
