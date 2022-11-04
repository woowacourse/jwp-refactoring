package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.application.Validator;
import kitchenpos.table.dto.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final Validator validator;

    public TableService(final OrderTableRepository orderTableRepository, final Validator validator) {
        this.orderTableRepository = orderTableRepository;
        this.validator = validator;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        final OrderTable orderTable = request.toEntity();
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.updateEmpty(validator, request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(orderTable);
    }
}
