package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableEmptyStatusRequest;
import kitchenpos.dto.OrderTableGuestRequest;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderDao jdbcTemplateOrderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao jdbcTemplateOrderDao, final OrderTableDao orderTableDao) {
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableRequest) {
        return OrderTableResponse.from(orderTableDao.save(orderTableRequest.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableEmptyStatusRequest emptyStatusRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 order table 입니다."));

        validateNotCompletedOrder(orderTableId);
        savedOrderTable.changeEmpty(emptyStatusRequest.isEmpty());

        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }

    private void validateNotCompletedOrder(final Long orderTableId) {
        if (jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 진행 중입니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableGuestRequest guestRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 order table 입니다."));

        savedOrderTable.changeNumberOfGuest(guestRequest.getNumberOfGuests());

        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }
}
