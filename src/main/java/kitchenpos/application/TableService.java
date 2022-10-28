package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableCreationDto;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
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

    @Deprecated
    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        orderTable.setId(null);
        orderTable.setTableGroupId(null);

        return orderTableDao.save(orderTable);
    }

    @Transactional
    public OrderTableDto createOrderTable(final OrderTableCreationDto orderTableCreationDto) {
        return OrderTableDto.from(orderTableDao.save(OrderTableCreationDto.toEntity(orderTableCreationDto)));
    }

    @Deprecated
    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<OrderTableDto> getOrderTables() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
    }

    @Deprecated
    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final Boolean emptyStatus) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        canChangeEmptyStatus(savedOrderTable);

        final OrderTable orderTable = new OrderTable(savedOrderTable.getId(), savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(), emptyStatus);

        return OrderTableDto.from(orderTableDao.save(orderTable));
    }

    private void canChangeEmptyStatus(final OrderTable orderTable) {
        final Optional<Order> order = orderDao.findByTableId(orderTable.getId());
        if (orderTable.isPartOfTableGroup() || (order.isPresent() && !order.get().isInCompletionStatus())) {
            throw new IllegalArgumentException();
        }
    }

    @Deprecated
    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        return OrderTableDto.from(orderTableDao.save(savedOrderTable.changeNumberOfGuest(numberOfGuests)));
    }
}
