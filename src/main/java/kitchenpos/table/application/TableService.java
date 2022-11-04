package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.OrderTableRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.domain.repository.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderDao;
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
    public OrderTableResponse create(final OrderTableRequest.Create request) {
        return new OrderTableResponse(
                orderTableDao.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty())));
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll().stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest.Empty request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateTableGroupId(savedOrderTable);
        validateOrderStatus(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());

        return new OrderTableResponse(orderTableDao.save(savedOrderTable));
    }

    private void validateTableGroupId(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmptyTableGroupId()) {
            throw new IllegalArgumentException("[ERROR] TableGroupId가 null입니다.");
        }
    }

    private void validateOrderStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("[ERROR] 주문 상태가 COOKING과 MEAL일때는 빈 주문 테이블로 변경할 수 없습니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableRequest.NumberOfGuest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        validateNumberOfGuests(numberOfGuests);

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateOrderTableIsEmpty(savedOrderTable);

        savedOrderTable.addNumberOfGuests(numberOfGuests);

        return new OrderTableResponse(orderTableDao.save(savedOrderTable));
    }

    private void validateOrderTableIsEmpty(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("[ERROR] 요청 손님의 수가 음수입니다.");
        }
    }
}
