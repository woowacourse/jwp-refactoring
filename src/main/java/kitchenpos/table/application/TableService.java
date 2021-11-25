package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.repository.OrderDao;
import kitchenpos.table.repository.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

@Service
public class TableService {
    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    public TableService(OrderTableDao orderTableDao, OrderDao orderDao) {
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        return orderTableDao.save(orderTableRequest.toEntity());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = findById(orderTableId);
        validateStatus(orderTableId);
        savedOrderTable.changeEmpty(empty);
        return savedOrderTable;
    }

    private void validateStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리 혹은 식사 중에는 빈 테이블로 변경할 수 없습니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return savedOrderTable;
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                            .orElseThrow(() -> new IllegalArgumentException("OrderTableId에 해당하는 주문 테이블이 존재하지 않습니다."));
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableDao.findAllByTableGroupId(tableGroupId);
    }
}
