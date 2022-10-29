package kitchenpos.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableCreationDto;
import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
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
    public OrderTableDto createOrderTable(final OrderTableCreationDto orderTableCreationDto) {
        return OrderTableDto.from(orderTableDao.save(OrderTableCreationDto.toEntity(orderTableCreationDto)));
    }

    @Transactional(readOnly = true)
    public List<OrderTableDto> getOrderTables() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final Boolean emptyStatus) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        canChangeEmptyStatus(savedOrderTable);

        final OrderTable orderTable = new OrderTable(savedOrderTable.getId(), savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(), emptyStatus);

        return OrderTableDto.from(orderTableDao.save(orderTable));
    }

    private void canChangeEmptyStatus(final OrderTable orderTable) {
        final Optional<Order> order = orderDao.findByTableId(orderTable.getId());
        if (orderTable.isPartOfTableGroup() || (order.isPresent() && !order.get().isInCompletionStatus())) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        return OrderTableDto.from(orderTableDao.save(savedOrderTable.changeNumberOfGuest(numberOfGuests)));
    }
}
