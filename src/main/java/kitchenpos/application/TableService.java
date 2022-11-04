package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderTableResponse create(OrderTable request) {
        OrderTable savedOrderTable = orderTableDao.save(request);
        return new OrderTableResponse(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블을 비우기위한 테이블이 존재하지 않습니다."));

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중이거나 식사중인 테이블이 있는 경우 테이블을 비울 수 없습니다.");
        }

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return new OrderTableResponse(orderTableDao.save(savedOrderTable));
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTable orderTable) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블의 손님 수를 변경하기 위한 테이블이 존재하지 않습니다."));

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블의 손님 수를 변경하기 위한 테이블이 비어있습니다.");
        }

        OrderTable table = orderTableDao.save(
                new OrderTable(
                        savedOrderTable.getId(),
                        savedOrderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(),
                        savedOrderTable.isEmpty()
                )
        );
        return new OrderTableResponse(table);
    }
}
