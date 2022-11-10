package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.request.OrderTableCreateRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableDao orderTableDao;
    private final TableValidator tableValidator;

    public TableService(final OrderTableDao orderTableDao, final TableValidator tableValidator) {
        this.orderTableDao = orderTableDao;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTable orderTable = orderTableDao.save(orderTableCreateRequest.toOrderTable());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        tableValidator.validateTable(orderTableId);
        savedOrderTable.changeEmpty(empty);
        final OrderTable orderTable = orderTableDao.save(savedOrderTable);
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        final OrderTable orderTable = orderTableDao.save(savedOrderTable);
        return OrderTableResponse.from(orderTable);
    }
}
