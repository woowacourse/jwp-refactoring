package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dto.OrderStatus;
import kitchenpos.dto.OrderTableDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

        return orderTableDao.save(orderTableDto);
    }

    public List<OrderTableDto> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTableDto savedOrderTableDto = orderTableDao.findById(orderTableId)
                                                              .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTableDto.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTableDto.setEmpty(orderTableDto.isEmpty());

        return orderTableDao.save(savedOrderTableDto);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTableDto) {
        final int numberOfGuests = orderTableDto.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTableDto savedOrderTableDto = orderTableDao.findById(orderTableId)
                                                              .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTableDto.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTableDto.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTableDto);
    }
}
