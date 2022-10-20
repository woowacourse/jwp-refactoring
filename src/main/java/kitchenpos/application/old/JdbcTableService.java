package kitchenpos.application.old;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class JdbcTableService implements TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public JdbcTableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    @Override
    public OrderTable create(final OrderTableCreateRequest request) {
        validateNumberOfGuests(request.getNumberOfGuests());
        final var orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableDao.save(orderTable);
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("유효하지 않은 고객 인원 수 : " + numberOfGuests);
        }
    }

    @Override
    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    @Override
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(request.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    @Override
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }
}
