package kitchenpos.table.application;

import kitchenpos.table.application.dto.GuestNumberRequest;
import kitchenpos.table.application.dto.OrderTableRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.OrderTableResponses;
import kitchenpos.table.application.dto.TableEmptyRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderValidator;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = orderTableWith(request);
        return new OrderTableResponse(orderTableRepository.save(orderTable));
    }

    public OrderTableResponses list() {
        return new OrderTableResponses(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final TableEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.checkTableGroupId();
        orderValidator.validate(orderTableId);
        savedOrderTable.updateEmpty(request.getEmpty());

        return new OrderTableResponse(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final GuestNumberRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.checkValidity();
        savedOrderTable.updateNumberOfGuests(request.getNumberOfGuests());

        return new OrderTableResponse(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable orderTableWith(OrderTableRequest request) {
        return OrderTable.builder()
                .id(request.getId())
                .build();
    }

}
