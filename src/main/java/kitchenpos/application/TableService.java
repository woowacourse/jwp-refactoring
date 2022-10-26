package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableDao.save(orderTableRequest.toEntity());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, Boolean empty) {
        OrderTable savedOrderTable = getById(orderTableId);
        validateNotCompletedOrderExist(orderTableId);
        savedOrderTable.changeEmpty(empty);
        OrderTable orderTable = orderTableDao.save(savedOrderTable);
        return OrderTableResponse.from(orderTable);
    }

    private void validateNotCompletedOrderExist(Long orderTableId) {
        boolean existNotCompletedOrder = orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        );
        if (existNotCompletedOrder) {
            throw new IllegalArgumentException("주문이 완료되지 않아 상태를 변경할 수 없습니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, Integer numberOfGuests) {
        OrderTable savedOrderTable = getById(orderTableId);
        savedOrderTable.updateNumberOfGuests(numberOfGuests);
        OrderTable orderTable = orderTableDao.save(savedOrderTable);
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable getById(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }
}
