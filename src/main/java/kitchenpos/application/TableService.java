package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.ChangeEmptyRequest;
import kitchenpos.ui.dto.request.ChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(request.getNumberOfGuests());
        orderTable.setEmpty(request.isEmpty());

        return toResponse(orderTableDao.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
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

        return toResponse(orderTableDao.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
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

        return toResponse(orderTableDao.save(savedOrderTable));
    }

    private OrderTableResponse toResponse(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }
}
