package kitchenpos.application.order;

import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderChangeStatusRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.support.ServiceTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class OrderServiceTest {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 주문_항목이_없다면_예외가_발생한다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(10, false));
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), List.of());

        // expect
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 하나 이상이여야 합니다");
    }

    @Test
    void 주문_항목의_메뉴가_존재하지_않는다면_예외가_발생한다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(10, false));
        OrderRequest request = new OrderRequest(savedOrderTable.getId(), List.of(new OrderRequest.OrderLineItemRequest(1L, 1L)));

        // expect
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 항목이 있습니다");
    }

    @Test
    void 주문_테이블이_존재하지_않는다면_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Menu menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(10000L), menuGroup.getId(), List.of()));
        OrderRequest request = new OrderRequest(MAX_VALUE, List.of(new OrderRequest.OrderLineItemRequest(menu.getId(), 1L)));

        // expect
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다");
    }

    @Test
    void 주문을_생성한다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(10, false));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Menu menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(10000L), menuGroup.getId(), List.of()));
        OrderRequest request = new OrderRequest(savedOrderTable.getId(), List.of(new OrderRequest.OrderLineItemRequest(menu.getId(), 1L)));

        // when
        OrderResponse savedOrder = orderService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING);
            softly.assertThat(savedOrder.getOrderTableId()).isEqualTo(request.getOrderTableId());
            softly.assertThat(savedOrder.getOrderedTime()).isNotNull();
            softly.assertThat(savedOrder.getOrderLineItems()).map(OrderResponse.OrderLineItemResponse::getSeq)
                    .isNotNull();
        });
    }

    @Test
    void 주문을_조회한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(10, false));
        Order order1 = orderRepository.save(new Order(orderTable.getId(), MEAL, List.of(new OrderLineItem(1L, 3))));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(Long.class)
                .isEqualTo(Stream.of(order1)
                        .map(OrderResponse::from)
                        .collect(Collectors.toList()));
    }

    @Test
    void 주문의_상태를_변경할_때_주문이_존재하지_않으면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(MAX_VALUE, new OrderChangeStatusRequest(COOKING)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 존재하지 않습니다");
    }

    @Test
    void 주문의_상태를_변경할_때_상태가_완료면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(10, false));
        Order order = new Order(orderTable.getId(), OrderStatus.COMPLETION, List.of(new OrderLineItem(1L, 10)));
        Order savedOrder = orderRepository.save(order);
        OrderChangeStatusRequest request = new OrderChangeStatusRequest(COOKING);

        // expect
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료 상태의 주문은 변경할 수 없습니다");
    }

    @Test
    void 주문의_상태를_변경한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(10, false));
        Order order = new Order(orderTable.getId(), COOKING, List.of(new OrderLineItem(1L, 10)));
        Order savedOrder = orderRepository.save(order);
        OrderChangeStatusRequest newOrder = new OrderChangeStatusRequest(OrderStatus.MEAL);

        // when
        OrderResponse changedOrder = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

        // then
        assertSoftly(softly -> {
            softly.assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
            softly.assertThat(changedOrder.getId()).isEqualTo(savedOrder.getId());
        });
    }
}
