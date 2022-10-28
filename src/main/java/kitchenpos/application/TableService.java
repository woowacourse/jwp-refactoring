package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.OrderTableEmptyRequest;
import kitchenpos.application.request.OrderTableGuestModifyRequest;
import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.from(orderTableDao.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableDao.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableEmptyRequest orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 중이거나 식사 중인 테이블이 존재합니다.");
        }

        savedOrderTable.changeEmptyStatus(orderTable.isEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableGuestModifyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블입니다.");
        }

        savedOrderTable.changeNumberOfGuest(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }
}
