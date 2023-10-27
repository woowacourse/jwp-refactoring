package kitchenpos.order.application;

import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.exception.NotFoundOrDuplicateMenuToOrderExcpetion;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.common.vo.OrderStatus;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.order.ui.dto.ChangeOrderStatusRequest;
import kitchenpos.order.ui.dto.OrderLineItemDto;
import kitchenpos.order.ui.dto.OrderRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validateOrderLineItemsIsExists(orderRequest.getOrderLineItems());

        final OrderTable orderTable =
                orderTableRepository.findById(orderRequest.getOrderTableId())
                                    .orElseThrow(() -> new NotFoundOrderTableException("해당 주문 테이블이 존재하지 않습니다."));
        final OrderLineItems orderLineItems = convertToOrderLineItem(orderRequest);
        final Order order = orderRequest.toEntity(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private void validateOrderLineItemsIsExists(final List<OrderLineItemDto> orderLineItemDtos) {
        if (orderLineItemDtos.size() != menuRepository.countByIdIn(convertToIds(orderLineItemDtos))) {
            throw new NotFoundOrDuplicateMenuToOrderExcpetion("존재하지 않는 혹은 중복된 메뉴를 주문 항목으로 설정했습니다.");
        }
    }

    private OrderLineItems convertToOrderLineItem(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (final OrderLineItemDto orderLineItemDto : orderRequest.getOrderLineItems()) {
            final Menu menu = menuRepository.findById(orderLineItemDto.getMenuId())
                                            .orElseThrow(() -> new NotFoundMenuException("해당 메뉴가 존재하지 않습니다."));
            final OrderLineItem orderLineItem = orderLineItemDto.toEntity(menu);
            orderLineItems.add(orderLineItem);
        }

        return new OrderLineItems(orderLineItems);
    }

    private List<Long> convertToIds(final List<OrderLineItemDto> orderLineItemDtos) {
        return orderLineItemDtos.stream()
                                .map(OrderLineItemDto::getMenuId)
                                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.updateOrderLineItems(findAllOrderListItems(order.getId()));
        }

        return orders.stream()
                     .map(OrderResponse::from)
                     .collect(Collectors.toList());
    }

    private OrderLineItems findAllOrderListItems(final Long orderId) {
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);

        return new OrderLineItems(orderLineItems);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest changeStatusRequest) {
        final Order order = orderRepository.findById(orderId)
                                                .orElseThrow(() -> new NotFoundOrderException("해당 주문이 존재하지 않습니다."));

        order.updateOrderStatus(OrderStatus.valueOf(changeStatusRequest.getOrderStatus()));

        return OrderResponse.from(order);
    }
}
