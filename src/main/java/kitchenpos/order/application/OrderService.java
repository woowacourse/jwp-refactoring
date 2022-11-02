package kitchenpos.order.application;

import kitchenpos.order.application.dto.request.OrderRequestAssembler;
import kitchenpos.order.application.dto.request.order.ChangeOrderStatusRequest;
import kitchenpos.order.application.dto.request.order.OrderRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.application.dto.response.OrderResponseAssembler;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRequestAssembler requestAssembler;
    private final OrderResponseAssembler responseAssembler;

    public OrderService(final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderRequestAssembler requestAssembler,
                        final OrderResponseAssembler responseAssembler
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.requestAssembler = requestAssembler;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = asOrderTable(request.getOrderTableId());
        validateOrderTableNotEmpty(orderTable);

        final var order = requestAssembler.asOrder(request);
        final var savedOrder = orderRepository.save(order);
        return responseAssembler.asOrderResponse(savedOrder);
    }

    private OrderTable asOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어 있습니다.");
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return responseAssembler.asOrderResponses(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = asOrder(orderId);
        order.updateOrderStatus(request.getOrderStatus());

        return responseAssembler.asOrderResponse(order);
    }

    private Order asOrder(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}
