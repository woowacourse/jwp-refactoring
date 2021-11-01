package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderTableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.getEmpty());
        orderTableRepository.save(orderTable);
        return new OrderTableResponse(orderTable);
    }

    public List<OrderTableResponse> findAll() {
        final List<OrderTable> foundAllOrderTables = orderTableRepository.findAll();
        return foundAllOrderTables.stream()
            .map(OrderTableResponse::new)
            .collect(Collectors.toList())
            ;
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable foundOrderTable = findOrderTableById(orderTableId);
        validateOrderTableToChangeEmpty(foundOrderTable);
        foundOrderTable.changeEmpty(orderTableRequest.getEmpty());
        return new OrderTableResponse(foundOrderTable);
    }

    private void validateOrderTableToChangeEmpty(OrderTable foundOrderTable) {
        validateTableGroupIsNull(foundOrderTable);
        validateOrdersStatusOf(foundOrderTable);
    }

    private void validateTableGroupIsNull(OrderTable foundOrderTable) {
        try {
            foundOrderTable.validateTableGroupIsNull();
        } catch (IllegalStateException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private void validateOrdersStatusOf(OrderTable foundOrderTable) {
        if (orderRepository.existsByOrderTableAndOrderStatusIsIn(
            foundOrderTable, OrderStatus.getExceptCompletion())) {
            throw new BadRequestException("OrderTable에 COMPLETION상태가 아닌 Order가 존재합니다.");
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable foundOrderTable = findOrderTableById(orderTableId);
        foundOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());

        return new OrderTableResponse(foundOrderTable);
    }
}
