package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGuestChangeRequest;
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
    public OrderTableResponse create(final OrderTableCreateRequest orderTable) {
        OrderTable nullTable = new OrderTable(orderTable.getNumberOfGuests(), orderTable.isEmpty());
        return toOrderTableResponse(orderTableDao.save(nullTable));
    }

    public List<OrderTableResponse> list() {
        return toOrderTableResponses(orderTableDao.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        validateOrdersCompleted(orderTableId);

        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .changeEmpty(orderTableChangeEmptyRequest.isEmpty());
        return toOrderTableResponse(orderTableDao.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final TableGuestChangeRequest changeRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .validateTableIsFull()
                .placeNumberOfGuests(changeRequest.getNumberOfGuests());
        return toOrderTableResponse(orderTableDao.save(orderTable));
    }

    private OrderTableResponse toOrderTableResponse(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    private List<OrderTableResponse> toOrderTableResponses(List<OrderTable> savedOrderTables) {
        return savedOrderTables.stream()
                .map(this::toOrderTableResponse)
                .collect(Collectors.toList());
    }

    private void validateOrdersCompleted(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("테이블의 주문이 있다면 COMPLETION 상태여야 한다.");
        }
    }
}
