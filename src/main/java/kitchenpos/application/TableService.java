package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.request.OrderTableCreateRequest;
import kitchenpos.application.request.OrderTableUpdateRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableDao.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 OrderTable 입니다."));

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("해당 OrderTable이 이미 TableGroup에 속해있습니다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("해당 OrderTable의 Order중 아직 완료되지 않은 것이 존재합니다.");
        }

        savedOrderTable.setEmpty(request.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 음수일 수 없습니다.");
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 OrderTable 입니다."));

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("해당 OrderTable이 empty 상태 입니다.");
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }
}
