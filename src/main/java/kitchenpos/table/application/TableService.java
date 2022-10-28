package kitchenpos.table.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.OrderTableSaveRequest;
import kitchenpos.table.domain.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(OrderTableSaveRequest request) {
        OrderTable orderTable = orderTableDao.save(request.toEntity());
        return OrderTableResponse.toResponse(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll().stream()
            .map(OrderTableResponse::toResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, Boolean empty) {
        OrderTable orderTable = findOrderTable(orderTableId);
        validateChangeEmpty(orderTable);
        orderTable.changeEmpty(empty);

        orderTableDao.save(orderTable);
        return OrderTableResponse.toResponse(orderTable);
    }

    private void validateChangeEmpty(OrderTable orderTable) {
        if (shouldNotChangeEmpty(orderTable)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean shouldNotChangeEmpty(OrderTable orderTable) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), List.of(COOKING.name(), MEAL.name()));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);

        orderTableDao.save(orderTable);
        return OrderTableResponse.toResponse(orderTable);
    }

    private OrderTable findOrderTable(Long oderTableId) {
        return orderTableDao.findById(oderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
