package kitchenpos.order.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.request.OrderTableCreateRequest;
import kitchenpos.order.dto.request.OrderTableEmptyChangeRequest;
import kitchenpos.order.dto.request.OrderTableGuestsChangeRequest;
import kitchenpos.order.dto.response.OrderTableResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
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
