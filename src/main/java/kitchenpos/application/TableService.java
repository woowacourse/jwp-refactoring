package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTableDto orderTableDto) {
        OrderTable orderTableForSave =
                OrderTable.ofNullId(null, orderTableDto.getNumberOfGuests(), orderTableDto.isEmpty());
        return orderTableDao.save(orderTableForSave);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable savedOrderTable = searchOrderTable(orderTableId);
        validateOrderTableAlreadyInGroup(savedOrderTable);
        validateOrderStatus(orderTableId);
        savedOrderTable.updateEmpty(orderTableDto.isEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    private OrderTable searchOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] OrderTable 을 찾을 수 없습니다."));
    }

    private void validateOrderTableAlreadyInGroup(final OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("[ERROR] OrderTable 에 단체 지정이 되어 있습니다.");
        }
    }

    private void validateOrderStatus(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )) {
            throw new IllegalArgumentException("[ERROR] 테이블 상태를 변경할 수 없습니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTableDto) {
        final int numberOfGuests = orderTableDto.getNumberOfGuests();
        validateNumberOfGuests(numberOfGuests);
        final OrderTable savedOrderTable = searchOrderTable(orderTableId);
        validateEmptyOrderTable(savedOrderTable);
        savedOrderTable.updateNumberOfGuests(numberOfGuests);
        return orderTableDao.save(savedOrderTable);
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("[ERROR] 손님수를 0명 미만으로 변경할 수 없습니다.");
        }
    }

    private void validateEmptyOrderTable(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블에 손님수를 변경할 수 없습니다.");
        }
    }
}
