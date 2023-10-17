package kitchenpos.application;

import kitchenpos.application.dto.TableChangeEmptyStatusRequest;
import kitchenpos.application.dto.TableChangeNumberOfGuestRequest;
import kitchenpos.application.dto.TableCreateRequest;
import kitchenpos.application.dto.TableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public TableResponse create(final TableCreateRequest request) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(null);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(request.getNumberOfGuest());
        orderTable.setEmpty(request.getEmpty());

        return TableResponse.from(orderTableDao.save(orderTable));
    }

    public List<TableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(TableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableChangeEmptyStatusRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(request.getEmpty());

        return TableResponse.from(orderTableDao.save(savedOrderTable));
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableChangeNumberOfGuestRequest request) {
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

        return TableResponse.from(orderTableDao.save(savedOrderTable));
    }
}
