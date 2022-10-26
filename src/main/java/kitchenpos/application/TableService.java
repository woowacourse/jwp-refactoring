package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;

public interface TableService {
    OrderTable create(OrderTableCreateRequest request);

    List<OrderTable> list();

    OrderTable changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest request);

    OrderTable changeNumberOfGuests(Long orderTableId, OrderTableChangeNumberOfGuestsRequest request);
}
