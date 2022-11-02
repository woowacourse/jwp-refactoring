package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.TableChangeEmptyRequest;
import kitchenpos.table.dto.request.TableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.request.TableCreateRequest;
import kitchenpos.table.dto.response.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderStatusValidator orderStatusValidator;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderStatusValidator orderStatusValidator, final OrderTableDao orderTableDao) {
        this.orderStatusValidator = orderStatusValidator;
        this.orderTableDao = orderTableDao;
    }

    public TableResponse create(final TableCreateRequest request) {
        return TableResponse.from(orderTableDao.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        return orderTableDao.findAll().stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    public TableResponse changeEmpty(final Long orderTableId, final TableChangeEmptyRequest request) {
        validateOrderStatus(orderTableId);
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        if (request.isEmpty()) {
            savedOrderTable.empty();
        } else {
            savedOrderTable.fill();
        }
        return TableResponse.from(orderTableDao.save(savedOrderTable));
    }

    private void validateOrderStatus(final Long orderTableId) {
        if (orderStatusValidator.existsByIdAndStatusIn(orderTableId, List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return TableResponse.from(orderTableDao.save(savedOrderTable));
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
