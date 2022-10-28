package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTable request) {
        final var orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable request) {
        final var isEmpty = request.isEmpty();

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));

        validateOrderTableNotGrouped(savedOrderTable);
        validateAllOrderTablesCompleted(orderTableId);

        savedOrderTable.changeEmptyStatusTo(isEmpty);

        return orderTableDao.save(savedOrderTable);
    }

    private void validateOrderTableNotGrouped(final OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정된 테이블입니다.");
        }
    }

    private void validateAllOrderTablesCompleted(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("계산이 완료되지 않은 테이블입니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable request) {
        final int numberOfGuests = request.getNumberOfGuests();

        validateNumberOfGuestsNotNegative(numberOfGuests);

        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
        validateOrderTableNotEmpty(orderTable);

        orderTable.updateNumberOfGuests(numberOfGuests);
        return orderTableDao.save(orderTable);
    }

    private void validateNumberOfGuestsNotNegative(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수가 될 수 없습니다.");
        }
    }

    private void validateOrderTableNotEmpty(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }
}
