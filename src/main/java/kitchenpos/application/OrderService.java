package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuQuantityDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.vo.OrderStatus;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse create(OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문의 메뉴가 존재하지 않습니다.");
        }

        List<MenuQuantityDto> menuQuantities = orderLineItems.stream()
                .map(this::convertToMenuQuantity)
                .collect(Collectors.toList());

        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("테이블 정보가 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블에 주문을 등록할 수 없습니다.");
        }

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        orderRepository.save(order);
        order.addMenus(menuQuantities);
        return OrderResponse.from(order);
    }

    private MenuQuantityDto convertToMenuQuantity(OrderLineItemRequest orderLineItem) {
        Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴입니다."));
        return new MenuQuantityDto(menu, orderLineItem.getQuantity());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        order.checkEditable();
        OrderStatus orderStatus = OrderStatus.from(orderRequest.getOrderStatus());
        order.updateStatus(orderStatus);
        return OrderResponse.from(order);
    }
}
