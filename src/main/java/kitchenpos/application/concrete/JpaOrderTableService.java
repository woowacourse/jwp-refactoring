package kitchenpos.application.concrete;

import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.validator.OrderTableValidator;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class JpaOrderTableService implements TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public JpaOrderTableService(final OrderTableRepository orderTableRepository,
                                final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    @Override
    public OrderTable create(final OrderTableCreateRequest request) {
        final var newOrderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableRepository.save(newOrderTable);
    }

    @Override
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    @Override
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new)
                .changeEmpty(orderTableValidator, request.isEmpty());
    }

    @Transactional
    @Override
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuestsRequest request) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new)
                .changeNumberOfGuests(orderTableValidator, request.getNumberOfGuests());
    }
}
