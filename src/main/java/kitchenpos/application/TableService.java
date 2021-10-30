package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    public OrderTableResponse create(final OrderTableRequest orderTable) {
        return OrderTableResponse.of(orderTableDao.save(orderTable.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll().stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        // TODO : 캡슐화
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(empty);

        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        // TODO : 캡슐화
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }
}
