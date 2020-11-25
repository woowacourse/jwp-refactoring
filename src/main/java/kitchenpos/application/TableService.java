package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableResponseDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponseDto create() {
        OrderTable saved = orderTableDao.save(new OrderTable());
        return OrderTableResponseDto.from(saved);
    }

    public List<OrderTableResponseDto> list() {
        List<OrderTable> orderTables = orderTableDao.findAll();
        return OrderTableResponseDto.listOf(orderTables);
    }

    @Transactional
    public OrderTableResponseDto changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태이블입니다."));

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("테이블에 들어간 주문 중 '조리' 또는 '식사 중' 상태인 주문이 남아있습니다.");
        }

        savedOrderTable.changeEmpty(orderTable.isEmpty());
        OrderTable saved = orderTableDao.save(savedOrderTable);

        return OrderTableResponseDto.from(saved);
    }

    @Transactional
    public OrderTableResponseDto changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        OrderTable saved = orderTableDao.save(savedOrderTable);
        return OrderTableResponseDto.from(saved);
    }
}
