package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.table.TableEmptyChangeRequest;
import kitchenpos.application.dto.table.TableGuestChangeRequest;
import kitchenpos.application.dto.table.TableRequest;
import kitchenpos.application.dto.table.TableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
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

    @Transactional
    public TableResponse create(final TableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        return TableResponse.from(savedOrderTable);
    }

    public List<TableResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();

        return orderTables.stream()
            .map(TableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableEmptyChangeRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateEmpty(request.isEmpty());

        final OrderTable changedOrderTable = orderTableDao.save(savedOrderTable);

        return TableResponse.from(changedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableGuestChangeRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        final OrderTable changedOrderTable = orderTableDao.save(savedOrderTable);

        return TableResponse.from(changedOrderTable);
    }
}
