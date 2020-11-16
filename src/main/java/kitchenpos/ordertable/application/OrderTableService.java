package kitchenpos.ordertable.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.OrderTableEmptyChangeRequest;
import kitchenpos.ordertable.dto.OrderTableGuestsChangeRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderTableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableEmptyChangeRequest request) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        //TODO: 고치기
        boolean isCookingOrMeal = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));

        savedOrderTable.changeEmpty(request.isEmpty(), isCookingOrMeal);

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableGuestsChangeRequest request) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(savedOrderTable);
    }
}
