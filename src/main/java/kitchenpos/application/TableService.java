package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;

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
        validateTableIsInGroup(savedOrderTable);
        validateStatus(orderTableId);
        savedOrderTable.changeEmpty(empty);
        return savedOrderTable;
    }

    private void validateTableIsInGroup(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException("주문 테이블 그룹에 속해 있어 변경할 수 없습니다.");
        }
    }

    private void validateStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리 혹은 식사 중에는 빈 테이블로 변경할 수 없습니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 0 이상이어야 합니다.");
        }

        final OrderTable savedOrderTable = findById(orderTableId);
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 손님의 수를 변경할 수 없습니다.");
        }
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
