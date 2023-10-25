package kitchenpos.table.application;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.ui.dto.OrderTableCreateRequest;
import kitchenpos.table.ui.dto.OrderTableResponse;
import kitchenpos.table.ui.dto.OrderTableUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Transactional(readOnly = true)
@Service
public class OrderTableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.save(request.toEntity());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = findByIdOrThrow(orderTableId);

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException("주문 테이블이 조리 중이거나 식사 중입니다.");
        }

        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    public OrderTable findByIdOrThrow(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = findByIdOrThrow(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTable> findAllByIdsOrThrow(final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블입니다.");
        }

        return orderTables;
    }
}
