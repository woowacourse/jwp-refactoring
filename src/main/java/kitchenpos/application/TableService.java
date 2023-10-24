package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableDto;
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
    public OrderTableDto create(final OrderTableDto orderTableDto) {
        orderTableDto.setId(null);
        orderTableDto.setTableGroupId(null);

        OrderTable savedOrderTable = orderTableDao.save(toEntity(orderTableDto));
        return OrderTableDto.from(savedOrderTable);
    }

    private OrderTable toEntity(OrderTableDto orderTableDto) {
        return new OrderTable(
            orderTableDto.getId(),
            null,
            orderTableDto.getNumberOfGuests(),
            orderTableDto.isEmpty()
        );
    }

    public List<OrderTableDto> list() {
        return orderTableDao.findAll()
                            .stream()
                            .map(OrderTableDto::from)
                            .collect(toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable foundOrderTable = orderTableDao.findById(orderTableId)
                                                        .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(foundOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        foundOrderTable.setEmpty(orderTableDto.isEmpty());

        return OrderTableDto.from(foundOrderTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId,
        final OrderTableDto orderTableDto) {
        final int numberOfGuests = orderTableDto.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable foundOrderTable = orderTableDao.findById(orderTableId)
                                                              .orElseThrow(IllegalArgumentException::new);

        if (foundOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        foundOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableDto.from(foundOrderTable);
    }
}
