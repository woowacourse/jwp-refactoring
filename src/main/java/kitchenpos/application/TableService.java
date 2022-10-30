package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = orderTableDao.save(request.toEntity());

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
            .stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
        final OrderTableUpdateEmptyRequest request
    ) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        validateCompletion(orderTableId);
        orderTable.updateEmpty(request.isEmpty());

        return OrderTableResponse.from(orderTableDao.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableUpdateNumberOfGuestsRequest request
    ) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTableDao.save(orderTable));
    }

    private void validateCompletion(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
