package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.OrderTableCreateDto;
import kitchenpos.application.dto.OrderTableUpdateGuestDto;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.table.OrderStatusChecker;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderStatusChecker orderStatusChecker;

    public TableService(final OrderTableRepository orderTableRepository,
        final OrderStatusChecker orderStatusChecker) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusChecker = orderStatusChecker;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateDto orderTableCreateDto) {
        final OrderTable newOrderTable = OrderTable.of(orderTableCreateDto.getNumberOfGuests());

        return orderTableRepository.save(newOrderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeEmpty(orderStatusChecker, true);

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
        final OrderTableUpdateGuestDto orderTableUpdateGuestDto) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeGuests(orderTableUpdateGuestDto.getNumberOfGuests());

        return orderTableRepository.save(savedOrderTable);
    }
}
