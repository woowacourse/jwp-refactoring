package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGuestChangeRequest;
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
    public OrderTableResponse create(final OrderTableCreateRequest orderTable) {
        OrderTable nullTable = new OrderTable(null, null,
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
        return toOrderTableResponse(orderTableDao.save(nullTable));
    }

    private OrderTableResponse toOrderTableResponse(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }


    public List<OrderTableResponse> list() {
        return toOrderTableResponses(orderTableDao.findAll());
    }

    private List<OrderTableResponse> toOrderTableResponses(List<OrderTable> savedOrderTables) {
        return savedOrderTables.stream()
                .map(this::toOrderTableResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 테이블은 존재하지 않는다."));

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("테이블은 단체지정이 없어야 한다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("테이블의 주문이 있다면 COMPLETION 상태여야 한다.");
        }

        savedOrderTable.updateEmpty(orderTableChangeEmptyRequest.isEmpty());

        return toOrderTableResponse(orderTableDao.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final TableGuestChangeRequest orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블 고객 수는 0 이상이어야 한다.");
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 테이블은 존재하지 않는다."));

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블은 차있어야 한다.");
        }

        savedOrderTable.updateNumberOfGuests(numberOfGuests);

        return toOrderTableResponse(orderTableDao.save(savedOrderTable));
    }
}
