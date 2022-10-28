package kitchenpos.application;

import kitchenpos.application.dto.convertor.TableConvertor;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        final OrderTable orderTable = TableConvertor.convertToOrderTable(request);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return TableConvertor.convertToOrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();
        return TableConvertor.convertToOrderTableResponses(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = TableConvertor.convertToOrderTable(request);

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 테이블 입니다. [%s]", orderTableId)));

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException(String.format("테이블 그룹이 존재합니다. [%s]", savedOrderTable.getTableGroupId()));
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("테이블의 주문이 완료되지 않았습니다.");
        }

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        final OrderTable changedOrderTable = orderTableDao.save(savedOrderTable);
        return TableConvertor.convertToOrderTableResponse(changedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = TableConvertor.convertToOrderTable(request);
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 테이블 입니다. [%s]", orderTableId)));

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        final OrderTable changedOrderTable = orderTableDao.save(savedOrderTable);
        return TableConvertor.convertToOrderTableResponse(changedOrderTable);
    }
}
