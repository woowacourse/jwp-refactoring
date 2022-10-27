package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.dto.OrderTableCreateRequest;

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
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블입니다."));

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("변경할 수 있는 상태가 아닙니다.");
        }

        savedOrderTable.changeEmpty(request.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
        final OrderTableChangeNumberOfGuestRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블입니다."));

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }

}
