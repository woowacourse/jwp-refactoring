package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableEmptyUpdateRequest;
import kitchenpos.dto.request.TableGuestUpdateRequest;
import kitchenpos.dto.response.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
        OrderTable savedOrderTable = orderTableDao.save(request.toEntity());
        return TableResponse.from(savedOrderTable);
    }

    public List<TableResponse> list() {
        List<OrderTable> orderTables = orderTableDao.findAll();
        return toTableResponses(orderTables);
    }

    private List<TableResponse> toTableResponses(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(Long orderTableId, TableEmptyUpdateRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.validateNotInTableGroup();
        validateIsCompletedOrder(orderTableId);
        OrderTable updatedOrderTable = orderTableDao.save(request.toUpdateEntity(savedOrderTable));

        return TableResponse.from(updatedOrderTable);
    }

    private void validateIsCompletedOrder(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public TableResponse changeNumberOfGuests(Long orderTableId, TableGuestUpdateRequest request) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateNotEmpty();
        OrderTable updatedOrderTable = orderTableDao.save(request.toUpdateEntity(savedOrderTable));

        return TableResponse.from(updatedOrderTable);
    }
}
