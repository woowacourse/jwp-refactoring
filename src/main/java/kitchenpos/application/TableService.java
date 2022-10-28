package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = new OrderTable(request.getTableGroupId(), request.getNumberOfGuests(),
                request.isEmpty());

        orderTable.setId(null);
        orderTable.setTableGroupId(null);

        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return new OrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(foundOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("테이블 상태 변경을 위해선 테이블이 그룹으로 묶여있으면 안됩니다.");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중이거나 식사중인 상태이면 테이블을 비울 수 없습니다.");
        }

        foundOrderTable.setEmpty(request.isEmpty());

        final OrderTable savedOrderTable = orderTableRepository.save(foundOrderTable);
        return new OrderTableResponse(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("음수로 주문 테이블의 손님 수를 변경할 수 없습니다.");
        }

        final OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (foundOrderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있는 상태일 수 없습니다.");
        }

        foundOrderTable.setNumberOfGuests(numberOfGuests);

        final OrderTable savedOrderTable = orderTableRepository.save(foundOrderTable);
        return new OrderTableResponse(savedOrderTable);
    }
}
