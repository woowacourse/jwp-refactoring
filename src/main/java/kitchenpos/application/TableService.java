package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.TableEmptyRequest;
import kitchenpos.application.dto.TableNumberOfGuestsRequest;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.common.exception.InvalidTableException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Long create(OrderTableRequest orderTableRequest) {
        OrderTable order = new OrderTable(null, orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        return orderTableDao.save(order);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public void changeEmpty(Long orderTableId, TableEmptyRequest tableEmptyRequest) {
        OrderTable orderTable = getOrderTable(orderTableId);
        validateTableGroup(orderTable);
        validateOrderStatus(orderTable.getId());

        orderTable.updateEmpty(tableEmptyRequest.getEmpty());
        orderTableDao.updateEmpty(orderTable.getId(), orderTable.isEmpty());
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new InvalidTableException("테이블이 존재하지 않습니다."));
    }

    private void validateTableGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new InvalidTableException("단체 지정 정보가 존재합니다.");
        }
    }

    private void validateOrderStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new InvalidOrderException("주문이 완료 상태가 아닙니다.");
        }
    }

    @Transactional
    public void changeNumberOfGuests(Long orderTableId, TableNumberOfGuestsRequest tableNumberOfGuestsRequest) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.updateNumberOfGuests(tableNumberOfGuestsRequest.getNumberOfGuests());
        orderTableDao.updateNumberOfGuests(orderTable.getId(), orderTable.getNumberOfGuests());
    }
}
