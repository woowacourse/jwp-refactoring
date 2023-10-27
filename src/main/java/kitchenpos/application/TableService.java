package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.response.OrderTableDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
        OrderTable newOrderTable = new OrderTable(orderTableDto.getNumberOfGuests(),
                orderTableDto.isEmpty());
        OrderTable savedOrderTable = orderTableDao.save(newOrderTable);
        return OrderTableDto.from(savedOrderTable);
    }

    public List<OrderTableDto> list() {
        List<OrderTable> allOrderTables = orderTableDao.findAll();
        return allOrderTables.stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        validateResetToEmptyRequest(orderTableDto);

        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateGroupedOrderTable(savedOrderTable);
        validateOrderCompletionByOrderTableId(orderTableId);

        savedOrderTable.changeToEmptyTable();
        savedOrderTable = orderTableDao.save(savedOrderTable);
        return OrderTableDto.from(savedOrderTable);
    }

    private void validateResetToEmptyRequest(OrderTableDto orderTableDto) {
        if (!orderTableDto.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateGroupedOrderTable(OrderTable savedOrderTable) {
        if (savedOrderTable.isGrouped()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderCompletionByOrderTableId(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTableDto) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(orderTableDto.getNumberOfGuests());
        savedOrderTable = orderTableDao.save(savedOrderTable);
        return OrderTableDto.from(savedOrderTable);
    }
}
