package kitchenpos.application;

import kitchenpos.application.request.table.ChangeEmptyRequest;
import kitchenpos.application.request.table.ChangeNumberOfGuestsRequest;
import kitchenpos.application.request.table.OrderTableRequest;
import kitchenpos.application.response.ResponseAssembler;
import kitchenpos.application.response.tablegroup.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final ResponseAssembler responseAssembler;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                        final ResponseAssembler responseAssembler) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final var orderTable = asOrderTable(request);
        final var savedOrderTable = orderTableDao.save(orderTable);

        return responseAssembler.orderTableResponse(savedOrderTable);
    }

    private OrderTable asOrderTable(final OrderTableRequest request) {
        final var orderTable = new OrderTable();
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        orderTable.changeEmptyStatusTo(request.isEmpty());
        return orderTable;
    }

    public List<OrderTableResponse> list() {
        final var orderTables = orderTableDao.findAll();
        return responseAssembler.orderTableResponses(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        final var isEmpty = request.isEmpty();

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));

        validateOrderTableNotGrouped(savedOrderTable);
        validateAllOrderTablesCompleted(orderTableId);

        savedOrderTable.changeEmptyStatusTo(isEmpty);

        final var orderTable = orderTableDao.save(savedOrderTable);
        return responseAssembler.orderTableResponse(orderTable);
    }

    private void validateOrderTableNotGrouped(final OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정된 테이블입니다.");
        }
    }

    private void validateAllOrderTablesCompleted(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("계산이 완료되지 않은 테이블입니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        validateNumberOfGuestsNotNegative(numberOfGuests);

        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
        validateOrderTableNotEmpty(orderTable);

        orderTable.updateNumberOfGuests(numberOfGuests);

        final var savedOrderTable = orderTableDao.save(orderTable);
        return responseAssembler.orderTableResponse(savedOrderTable);
    }

    private void validateNumberOfGuestsNotNegative(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수가 될 수 없습니다.");
        }
    }

    private void validateOrderTableNotEmpty(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }
}
