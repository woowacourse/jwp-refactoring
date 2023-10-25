package kitchenpos.application.order;

import kitchenpos.application.dto.OrderChangeStatusRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.menu;
import static kitchenpos.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.fixture.OrderFixture.order;
import static kitchenpos.fixture.OrderFixture.orderRequest;
import static kitchenpos.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.fixture.OrderTableFixtrue.orderTable;
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
        OrderTable savedOrderTable = orderTableRepository.save(orderTable(10, false));
        OrderRequest orderRequest = orderRequest(savedOrderTable.getId(), List.of());

        // expect
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 하나 이상이여야 합니다");
    }

    @Test
    void 주문_항목의_메뉴가_존재하지_않는다면_예외가_발생한다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(orderTable(10, false));
        OrderRequest request = orderRequest(savedOrderTable.getId(), List.of(orderLineItem(1L, 1L)));

        // expect
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 항목이 있습니다");
    }

    @Test
    void 주문_테이블이_존재하지_않는다면_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(menuGroup("menuGroup"));
        Menu menu = menuRepository.save(menu("메뉴", 10000L, menuGroup.getId(), List.of()));
        OrderRequest request = orderRequest(MAX_VALUE, List.of(orderLineItem(menu.getId(), 1L)));

        // expect
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다");
    }

    @Test
    void 주문을_생성한다() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(orderTable(10, false));
        MenuGroup menuGroup = menuGroupRepository.save(menuGroup("menuGroup"));
        Menu menu = menuRepository.save(menu("메뉴", 10000L, menuGroup.getId(), List.of()));
        OrderRequest request = orderRequest(savedOrderTable.getId(), List.of(orderLineItem(menu.getId(), 1L)));

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
        OrderTable orderTable = orderTableRepository.save(orderTable(10, false));
        Order order1 = orderRepository.save(order(orderTable.getId(), MEAL, List.of(orderLineItem(1L, 3))));

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
        OrderTable orderTable = orderTableRepository.save(orderTable(10, false));
        Order order = order(orderTable.getId(), OrderStatus.COMPLETION, List.of(orderLineItem(1L, 10)));
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
        OrderTable orderTable = orderTableRepository.save(orderTable(10, false));
        Order order = order(orderTable.getId(), COOKING, List.of(orderLineItem(1L, 10)));
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
