package kitchenpos.ui.apiservice;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.OrderTableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderTabelApiService {

    private final OrderTableService orderTableService;

    public OrderTabelApiService(OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        OrderTable orderTable = orderTableService.create(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableService.list()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest request) {

        OrderTable orderTable = orderTableService.changeEmpty(orderTableId);
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableChangeNumberOfGuestsRequest request) {
        OrderTable orderTable = orderTableService.changeNumberOfGuests(orderTableId, request.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }
}
