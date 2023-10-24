package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableEmptyChangeRequest;
import kitchenpos.dto.OrderTableNumberOfGuestsChangeRequest;
import kitchenpos.dto.OrderTableRequest;
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
    public OrderTable create(final OrderTableRequest request) {
        return orderTableDao.save(
                new OrderTable(request.getTableGroupId(), request.getNumberOfGuests(), request.getEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableEmptyChangeRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        validateOrderTableForChangeEmpty(savedOrderTable);
        savedOrderTable.changeEmpty(request.getEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    private void validateOrderTableForChangeEmpty(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("테이블 그룹 아이디가 null입니다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중 or 식사중인 주문이 포함되어 있습니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableNumberOfGuestsChangeRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(savedOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }
}
