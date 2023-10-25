package kitchenpos.ordertable;

import kitchenpos.ordertable.OrderTableDao;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final ApplicationEventPublisher eventPublisher;
    private final OrderTableDao orderTableDao;

    public TableService(ApplicationEventPublisher eventPublisher, OrderTableDao orderTableDao) {
        this.eventPublisher = eventPublisher;
        this.orderTableDao = orderTableDao;
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
        eventPublisher.publishEvent(orderTable);
        return OrderTableDto.from(orderTableDao.save(orderTable));
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
