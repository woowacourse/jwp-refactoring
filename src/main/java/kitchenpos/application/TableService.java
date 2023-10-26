package kitchenpos.application;

import kitchenpos.application.exception.InvalidChangeOrderTableNumberOfGuests;
import kitchenpos.application.exception.NotFoundOrderTableException;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.menugroup.ChangeOrderTableEmptyRequest;
import kitchenpos.ui.dto.table.ChangeOrderTableNumberOfGuestsRequest;
import kitchenpos.ui.dto.table.OrderTableRequest;
import kitchenpos.ui.dto.table.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.save(orderTableRequest.toEntity());

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                          .map(OrderTableResponse::from)
                          .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(
            final Long orderTableId,
            final ChangeOrderTableEmptyRequest changeEmtpyRequest
    ) {
        final OrderTable orderTable =
                orderTableRepository.findById(orderTableId)
                                    .orElseThrow(() -> new NotFoundOrderTableException("해당 주문 테이블이 존재하지 않습니다."));

        validateOrder(orderTableId, orderTable);
        orderTable.updateEmpty(changeEmtpyRequest.isEmpty());

        return OrderTableResponse.from(orderTable);
    }

    private void validateOrder(final Long orderTableId, final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final ChangeOrderTableNumberOfGuestsRequest changeNumberOfGuestsRequest
    ) {
        final int numberOfGuests = changeNumberOfGuestsRequest.getNumberOfGuests();
        final OrderTable savedOrderTable =
                orderTableRepository.findById(orderTableId)
                                    .orElseThrow(() -> new NotFoundOrderTableException("해당 주문 테이블이 존재하지 않습니다."));

        validateOrderTableIsEmpty(savedOrderTable);

        savedOrderTable.updateNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(savedOrderTable);
    }

    private void validateOrderTableIsEmpty(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new InvalidChangeOrderTableNumberOfGuests("주문 테이블이 빈 상태라면 사용자 수를 변경할 수 없습니다.");
        }
    }
}
