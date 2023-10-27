package kitchenpos.ordertable.application;

import java.util.Collections;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.dto.request.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.request.OrderTableEmptyChangeRequest;
import kitchenpos.ordertable.dto.request.OrderTableGuestChangeRequest;
import kitchenpos.ordertable.dto.response.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(
            new OrderTable(null, request.getNumberOfGuests(), request.isEmpty(), Collections.emptyList())));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.of(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyChangeRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("orderTable 을 찾을 수 없습니다."));

        return OrderTableResponse.of(orderTableValidator.changeEmpty(orderTable, request.isEmpty()));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableGuestChangeRequest request) {
        final int numberOfGuests = request.getNumbersOfGuest();

        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        orderTable.changeNumbersOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTable);
    }
}
