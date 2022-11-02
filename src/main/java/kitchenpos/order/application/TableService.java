package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.order.dto.request.ChangeOrderTableNumberOfGuestRequest;
import kitchenpos.order.dto.request.CreateOrderTableRequest;
import kitchenpos.order.dto.response.OrderTableResponse;
import kitchenpos.order.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final CreateOrderTableRequest request) {
        final OrderTable orderTable = orderTableRepository.save(
            new OrderTable(request.getNumberOfGuests(), request.isEmpty()));

        return new OrderTableResponse(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeOrderTableEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTableValidator.validateChangeEmpty(orderTable.getId());

        orderTable.changeEmpty(request.isEmpty());

        return new OrderTableResponse(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final ChangeOrderTableNumberOfGuestRequest request) {

        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable orderTable = findOrderTableById(orderTableId);

        orderTable.changeNumberOfGuests(numberOfGuests);

        return new OrderTableResponse(orderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블입니다."));
    }

}
