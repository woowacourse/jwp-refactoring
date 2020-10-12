package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
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
    public TableResponse create(final TableRequest request) {
        OrderTable orderTable = OrderTable.builder()
            .empty(request.getEmpty())
            .numberOfGuests(request.getNumberOfGuests())
            .build();
        OrderTable savedTable = orderTableDao.save(orderTable);
        return TableResponse.from(savedTable);
    }

    public List<TableResponse> list() {
        List<OrderTable> tables = orderTableDao.findAll();
        return TableResponse.listFrom(tables);
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(request.getEmpty());

        OrderTable changedTable = orderTableDao.save(savedOrderTable);
        return TableResponse.from(changedTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        OrderTable changedTable = orderTableDao.save(savedOrderTable);
        return TableResponse.from(changedTable);
    }
}
