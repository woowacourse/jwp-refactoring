package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.OrderTableConvertEmptyStatusException;
import kitchenpos.repository.TableRepository;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final TableRepository tableRepository;
    private final OrderDao orderDao;

    public TableService(OrderDao orderDao, TableRepository tableRepository) {
        this.orderDao = orderDao;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderTable create(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable =
                new OrderTable(orderTableCreateRequest.getNumberOfGuests(), orderTableCreateRequest.getEmpty());

        return tableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return tableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        validateOrderTableStatus(orderTableId);

        return tableRepository.changeEmpty(orderTableId, orderTableChangeEmptyRequest.getEmpty());
    }

    private void validateOrderTableStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderTableConvertEmptyStatusException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId,
                                           OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest) {
        return tableRepository.changeNumberOfGuests(orderTableId,
                orderTableChangeNumberOfGuestsRequest.getNumberOfGuests());
    }
}
