package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체로 지정된 테이블은 상태를 변경할 수 없습니다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 완료되지 않아 상태를 변경할 수 없습니다.");
        }

        savedOrderTable.changeEmpty(empty);

        OrderTable orderTable = orderTableDao.save(savedOrderTable);
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, Integer numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("1명 이상으로 변경할 수 있습니다.");
        }

        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 손님 수 변경을 할 수 없습니다.");
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }
}
