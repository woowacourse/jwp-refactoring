package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.request.OrderChangeNumberOfGuestsRequest;
import kitchenpos.application.request.OrderTableCreateRequest;
import kitchenpos.application.request.OrderTableEmptyChangeRequest;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.dao.table.OrderTableDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
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
    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableDao.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableEmptyChangeRequest request) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateCookingOrMeal(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(savedOrderTable);
    }

    private void validateCookingOrMeal(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 혹은 식사중인 테이블 상태를 변화할 수 없습니다.");
        }
    }
}
