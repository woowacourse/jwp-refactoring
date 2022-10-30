package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.ChangeNumberOfGuestRequest;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
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
    public OrderTableResponse create(final OrderTableRequest request) {
        return OrderTableResponse.from(
                orderTableDao.save(new OrderTable(request.getNumberOfGuests(), request.getEmpty())));
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테이블이 존재하지 않습니다."));

        validateOrderStatusCompletion(orderTableId);

        savedOrderTable.changeEmpty(request.getEmpty());

        return OrderTableResponse.from(orderTableDao.save(new OrderTable(savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(),
                savedOrderTable.isEmpty())));
    }

    private void validateOrderStatusCompletion(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 또는 식사 중이라면 테이블 상태를 변경할 수 없습니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTableDao.save(new OrderTable(savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(),
                savedOrderTable.isEmpty())));
    }
}
