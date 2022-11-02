package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableChangeEmptyResponse;
import kitchenpos.table.dto.TableChangeNumberOfGuestsResponse;
import kitchenpos.table.dto.TableCreateResponse;
import kitchenpos.table.dto.TableFindResponse;
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
    public TableCreateResponse create(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = orderTableDao.save(new OrderTable(numberOfGuests, empty));
        return TableCreateResponse.from(orderTable);
    }

    public List<TableFindResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();
        return orderTables.stream()
                .map(TableFindResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableChangeEmptyResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateChangeEmpty(orderTableId, savedOrderTable);

        orderTableDao.update(new OrderTable(orderTableId, empty));

        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        return TableChangeEmptyResponse.from(orderTable);
    }

    private void validateChangeEmpty(final Long orderTableId, final OrderTable savedOrderTable) {
        if (savedOrderTable.isGroupTable()) {
            throw new IllegalArgumentException();
        }
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public TableChangeNumberOfGuestsResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        validateGuestCounts(numberOfGuests);

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateEmptyTable(savedOrderTable);

        savedOrderTable.setNumberOfGuests(numberOfGuests);
        orderTableDao.update(savedOrderTable);
        return TableChangeNumberOfGuestsResponse.from(savedOrderTable);
    }

    private void validateGuestCounts(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmptyTable(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
