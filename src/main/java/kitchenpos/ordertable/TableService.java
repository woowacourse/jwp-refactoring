package kitchenpos.ordertable;

import kitchenpos.order.OrderDao;
import kitchenpos.order.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    public TableService(OrderTableDao orderTableDao, OrderDao orderDao) {
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public OrderTableDto create(final OrderTableDto orderTableDto) {
        OrderTable orderTable = new OrderTable(null, null, orderTableDto.getNumberOfGuests(), orderTableDto.isEmpty());
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return OrderTableDto.from(savedOrderTable);
    }

    public List<OrderTableDto> list() {
        return orderTableDao.findAll().stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = new OrderTable(savedOrderTable.getId(), savedOrderTable.getTableGroupId(), savedOrderTable.getNumberOfGuests(), orderTableDto.isEmpty());
        changeEmpty(orderTable);
        return OrderTableDto.from(orderTableDao.save(orderTable));
    }

    public void changeEmpty(OrderTable orderTable) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeNumberOfGuest(orderTableDto.getNumberOfGuests());

        OrderTable saved = orderTableDao.save(savedOrderTable);
        return OrderTableDto.from(saved);
    }
}
