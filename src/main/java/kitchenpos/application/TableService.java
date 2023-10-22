package kitchenpos.application;

import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = OrderTable.forSave(request.getNumberOfGuests(), request.isEmpty(),
                                                         Collections.emptyList());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeEmpty();

        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableUpdateNumberOfGuestsRequest request) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTableRepository.save(orderTable);
    }
}
