package kitchenpos.ordertable.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.exception.TableEmptyDisabledException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.GuestNumber;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.OrderTableEmptyChangeRequest;
import kitchenpos.ordertable.dto.OrderTableGuestNumberChangeRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.exception.OrderTableNotFoundException;
import kitchenpos.ordertable.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private static final String SETTING_EMPTY_DISABLED_BY_ORDER_NOT_COMPLETE_EXCEPTION =
            "조리중이거나 식사중인 테이블의 empty를 변경할 수 없습니다.";

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    public TableService(OrderRepository orderRepository, TableRepository tableRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = new OrderTable(
                new GuestNumber(orderTableCreateRequest.getNumberOfGuests()), orderTableCreateRequest.isEmpty());
        OrderTable savedOrderTable = tableRepository.save(orderTable);
        return new OrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return tableRepository.findAll()
                .stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId,
                                          OrderTableEmptyChangeRequest orderTableEmptyChangeRequest) {
        OrderTable savedOrderTable = tableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        validateOrderStatusIfExists(orderTableId);
        savedOrderTable.setEmpty(orderTableEmptyChangeRequest.isEmpty());
        return new OrderTableResponse(tableRepository.save(savedOrderTable));
    }

    private void validateOrderStatusIfExists(Long orderTableId) {
        Optional<Order> order = orderRepository.findByOrderTableId(orderTableId);
        if (order.isPresent() && order.get().isNotCompletionOrderStatus()) {
            throw new TableEmptyDisabledException(SETTING_EMPTY_DISABLED_BY_ORDER_NOT_COMPLETE_EXCEPTION);
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableGuestNumberChangeRequest
            orderTableGuestNumberChangeRequest) {
        OrderTable savedOrderTable = tableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeGuestNumber(new GuestNumber(orderTableGuestNumberChangeRequest.getNumberOfGuests()));
        return new OrderTableResponse(tableRepository.save(savedOrderTable));
    }
}
