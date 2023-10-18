package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.table.CreateOrderTableRequest;
import kitchenpos.ui.dto.table.UpdateTableGuestRequest;
import kitchenpos.ui.dto.table.UpdateTableStatusRequest;
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
    public OrderTable create(final CreateOrderTableRequest createOrderTableRequest) {
        final OrderTable orderTable = createOrderTableRequest.toDomain();
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final UpdateTableStatusRequest updateTableStatusRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ) {
            throw new IllegalArgumentException();
        }

        orderTable.updateStatusEmpty(updateTableStatusRequest.getEmpty());

        return orderTableDao.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(
            final Long orderTableId,
            final UpdateTableGuestRequest updateTableGuestRequest
    ) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        final int numberOfGuests = updateTableGuestRequest.getGuests();
        orderTable.updateNumberOfGuests(numberOfGuests);

        return orderTableDao.save(orderTable);
    }
}
