package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableMapper;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.OrderTableValidator;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.dto.request.UpdateOrderTableGuestsRequest;
import kitchenpos.dto.response.CreateOrderTableResponse;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableMapper orderTableMapper;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(OrderTableMapper orderTableMapper, final OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.orderTableMapper = orderTableMapper;
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public CreateOrderTableResponse create(final CreateOrderTableRequest request) {
        OrderTable entity = orderTableMapper.toOrderTable(request);
        return CreateOrderTableResponse.from(orderTableRepository.save(entity));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final UpdateOrderTableEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTableValidator.validateUpdateEmpty(orderTable);
        final OrderTable updated = orderTable.updateEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(updated));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final UpdateOrderTableGuestsRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
        final OrderTable updated = orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        orderTableValidator.validateUpdateGuestNumber(updated);
        return OrderTableResponse.from(orderTableRepository.save(updated));
    }
}
