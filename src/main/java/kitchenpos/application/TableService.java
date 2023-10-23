package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private static final List<String> INCLUDE_ORDER_STATUS_NAMES
            = List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = orderTableDao.save(
                new OrderTable(null, request.getNumberOfGuests(), request.isEmpty()));
        return OrderTableResponse.toResponse(orderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        validateTableGroupId(savedOrderTable);
        validateOrderStatusComplete(orderTableId);
        savedOrderTable.setEmpty(request.isEmpty());
        return OrderTableResponse.toResponse(orderTableDao.save(savedOrderTable));
    }

    private void validateOrderStatusComplete(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, INCLUDE_ORDER_STATUS_NAMES)) {
            throw new IllegalArgumentException("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }

    private static void validateTableGroupId(final OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("[ERROR] 해당 주문 테이블은 다른 그룹에 속하는 테이블입니다.");
        }
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 주문 테이블입니다."));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        validateNumberOfGuests(numberOfGuests);
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        validateOrderTableIsEmpty(savedOrderTable);
        savedOrderTable.setNumberOfGuests(numberOfGuests);
        return OrderTableResponse.toResponse(orderTableDao.save(savedOrderTable));
    }

    private static void validateOrderTableIsEmpty(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }

    private static void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("[ERROR] 손님의 수가 음수입니다.");
        }
    }
}
