package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateTableDto;
import kitchenpos.application.dto.request.EmptyTableDto;
import kitchenpos.application.dto.response.TableDto;
import kitchenpos.application.dto.request.UpdateGuestNumberDto;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public TableDto create(final CreateTableDto createTableDto) {
        OrderTable orderTable = new OrderTable(createTableDto.getNumberOfGuests(), createTableDto.getEmpty());
        return TableDto.of(orderTableDao.save(orderTable));
    }

    public List<TableDto> list() {
        return orderTableDao.findAll()
                .stream()
                .map(TableDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableDto changeEmpty(EmptyTableDto emptyTableDto) {
        Long orderTableId = emptyTableDto.getOrderTableId();
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getOngoingStatuses())) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeEmpty(emptyTableDto.getEmpty());
        return TableDto.of(orderTableDao.save(savedOrderTable));
    }

    @Transactional
    public TableDto changeNumberOfGuests(UpdateGuestNumberDto updateGuestNumberDto) {
        final Long orderTableId = updateGuestNumberDto.getOrderTableId();
        final int numberOfGuests = updateGuestNumberDto.getNumberOfGuests();
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return TableDto.of(orderTableDao.save(savedOrderTable));
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
