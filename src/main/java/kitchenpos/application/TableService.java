package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.request.OrderTableUpdateGuestRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.OrderTablesResponse;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    public TableService(final OrderTableDao orderTableDao,
                        final OrderDao orderDao) {
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = request.toEntity();
        orderTableDao.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    public OrderTablesResponse list() {
        List<OrderTable> orderTables = orderTableDao.findAll();
        return OrderTablesResponse.from(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableUpdateEmptyRequest request) {
        validatePossibleChangeToEmpty(orderTableId);
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeToEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    private void validatePossibleChangeToEmpty(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException("조리중이거나 식사 중인 테이블 입니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableUpdateGuestRequest request) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
    }
}
