package kitchenpos.application;

import kitchenpos.application.exception.NotFoundMenuException;
import kitchenpos.application.exception.NotFoundOrDuplicateMenuToOrderExcpetion;
import kitchenpos.application.exception.NotFoundOrderException;
import kitchenpos.application.exception.NotFoundOrderTableException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.order.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.order.OrderLineItemDto;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
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
        final List<OrderLineItem> orderLineItems = convertToOrderLineItem(orderRequest);
        final Order order = orderRequest.toEntity(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private void validateOrderLineItemsIsExists(final List<OrderLineItemDto> orderLineItemDtos) {
        if (orderLineItemDtos.size() != menuRepository.countByIdIn(convertToIds(orderLineItemDtos))) {
            throw new NotFoundOrDuplicateMenuToOrderExcpetion("존재하지 않는 혹은 중복된 메뉴를 주문 항목으로 설정했습니다.");
        }
    }

    private List<OrderLineItem> convertToOrderLineItem(final OrderRequest orderRequest) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

        for (final OrderLineItemDto orderLineItemDto : orderRequest.getOrderLineItems()) {
            final Menu menu = menuRepository.findById(orderLineItemDto.getMenuId())
                                            .orElseThrow(() -> new NotFoundMenuException("해당 메뉴가 존재하지 않습니다."));
            final OrderLineItem orderLineItem = orderLineItemDto.toEntity(menu);
            savedOrderLineItems.add(orderLineItem);
        }

        return savedOrderLineItems;
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
            order.updateOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders.stream()
                     .map(OrderResponse::from)
                     .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest changeStatusRequest) {
        final Order order = orderRepository.findById(orderId)
                                                .orElseThrow(() -> new NotFoundOrderException("해당 주문이 존재하지 않습니다."));

        order.updateOrderStatus(OrderStatus.valueOf(changeStatusRequest.getOrderStatus()));

        return OrderResponse.from(order);
    }
}
