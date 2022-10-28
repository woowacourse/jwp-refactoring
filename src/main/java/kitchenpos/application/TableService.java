package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.TableEmptyRequest;
import kitchenpos.application.dto.TableNumberOfGuestsRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableResponse;
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
    public OrderTableResponse create(OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableDao.save(
                new OrderTable(null, orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty()));

        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public void changeEmpty(Long orderTableId, TableEmptyRequest tableEmptyRequest) {
        OrderTable orderTable = orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        orderTableDao.save(
                new OrderTable(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                        tableEmptyRequest.getEmpty()));
    }

    @Transactional
    public void changeNumberOfGuests(Long orderTableId, TableNumberOfGuestsRequest tableNumberOfGuestsRequest) {
        int numberOfGuests = tableNumberOfGuestsRequest.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        orderTableDao.save(
                new OrderTable(orderTable.getId(), orderTable.getTableGroupId(), numberOfGuests, orderTable.isEmpty()));
    }
}
