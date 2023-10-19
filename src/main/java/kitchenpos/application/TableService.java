package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.application.dto.request.UpdateOrderTableGuestsRequest;
import kitchenpos.application.dto.response.CreateOrderTableResponse;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.mapper.OrderTableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public CreateOrderTableResponse create(final CreateOrderTableRequest request) {
        OrderTable entity = OrderTableMapper.toOrderTable(request);
        OrderTable saved = orderTableDao.save(entity);
        return CreateOrderTableResponse.from(saved);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final UpdateOrderTableEmptyRequest request) {
        final OrderTable entity = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(COOKING.name(), MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        OrderTable updated = entity.updateEmpty(request.isEmpty());
        OrderTable save = orderTableDao.save(updated);
        return OrderTableResponse.from(save);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final UpdateOrderTableGuestsRequest request) {
        final OrderTable entity = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderTable updated = entity.updateNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableDao.save(updated));
    }
}
