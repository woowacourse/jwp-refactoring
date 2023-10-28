package kitchenpos.order.application;

import kitchenpos.order.application.dto.OrderTableCreateRequest;
import kitchenpos.order.application.dto.OrderTableResponse;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderTableService(final OrderValidator orderValidator, final OrderTableRepository orderTableRepository) {
        this.orderValidator = orderValidator;
        this.orderTableRepository = orderTableRepository;
    }

    public Long create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = OrderTable.of(request.getNumberOfGuests(), request.getEmpty());
        final OrderTable saveOrderTable = orderTableRepository.save(orderTable);
        return saveOrderTable.getId();
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> findAll() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public void changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);
        orderValidator.validate(orderTableId, savedOrderTable);
        savedOrderTable.updateEmpty(isEmpty);
    }

    public void changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.updateNumberOfGuests(numberOfGuests);
    }
}
