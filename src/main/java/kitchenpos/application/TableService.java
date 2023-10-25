package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {

    private final OrderOrderTableService orderOrderTableService;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderOrderTableService orderOrderTableService, OrderTableRepository orderTableRepository) {
        this.orderOrderTableService = orderOrderTableService;
        this.orderTableRepository = orderTableRepository;
    }

    public Long create() {
        return orderTableRepository.save(new OrderTable()).getId();
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public void changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);
        savedOrderTable.validateTableGroupIdIsNull();
        orderOrderTableService.validateOrderTableIdAndOrderStatusIn(orderTableId);
        savedOrderTable.updateEmpty(isEmpty);
    }

    public void changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);
        savedOrderTable.validateIsNotEmpty();
        savedOrderTable.updateNumberOfGuests(numberOfGuests);
    }
}
