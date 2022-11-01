package kitchenpos.application;

import static java.util.stream.Collectors.*;
import static kitchenpos.domain.OrderStatus.COOKING;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableSaveRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {

    private static final List<String> NONE_EMPTY_ORDER_TABLE = List.of(COOKING.name(), OrderStatus.MEAL.name());

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableSaveRequest request) {
        OrderTable orderTable = orderTableDao.save(request.toEntity());
        return new OrderTableResponse(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.getById(orderTableId);
        savedOrderTable.changeEmpty(() ->
                orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, NONE_EMPTY_ORDER_TABLE), request.isEmpty());

        return new OrderTableResponse(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberOfGuestsRequest request) {
        OrderTable savedOrderTable = orderTableDao.getById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return new OrderTableResponse(savedOrderTable);
    }
}
