package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableEmptyUpdateRequest;
import kitchenpos.dto.TableGuestUpdateRequest;
import kitchenpos.dto.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public TableResponse create(TableCreateRequest request) {
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty()));

        return TableResponse.from(savedOrderTable);
    }

    public List<TableResponse> list() {
        List<OrderTable> orderTables = orderTableDao.findAll();

        return orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(Long orderTableId, TableEmptyUpdateRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        OrderTable updatedOrderTable = orderTableDao.save(request.toUpdateEntity(savedOrderTable));

        return TableResponse.from(updatedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(Long orderTableId, TableGuestUpdateRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        };

        OrderTable updatedOrderTable = orderTableDao.save(request.toUpdateEntity(savedOrderTable));

        return TableResponse.from(updatedOrderTable);
    }
}
