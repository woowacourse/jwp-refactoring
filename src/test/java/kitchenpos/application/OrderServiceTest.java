package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.Fixture.Fixture.orderLineItemFixture;
import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.groups.Tuple.tuple;

@ServiceTest
class OrderServiceTest {

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    private MenuGroup menuGroup;
    private Menu menu;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem;
    private Order order;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(30000), menuGroup));
        orderTable = orderTableRepository.save(new OrderTable(null, 1, false));
        order = orderRepository.save(new Order(orderTable, MEAL.name(), LocalDateTime.now()));
        orderLineItem = orderLineItemRepository.save(orderLineItemFixture(order, menu, 1));
    }

    @Nested
    class 주문등록 {

        @Test
        void 주문을_등록한다() {
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), MEAL.name(), LocalDateTime.now(), List.of(orderLineItem));

            OrderResponse response = orderService.create(orderRequest);

            assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();

                softly.assertThat(response).extracting("orderTableId", "orderStatus")
                        .containsOnly(orderRequest.getOrderTableId(), orderRequest.getOrderStatus());

                List<Menu> menus = response.getOrderLineItems().stream()
                        .map(OrderLineItem::getMenu)
                        .collect(Collectors.toList());
                softly.assertThat(menus).extracting("id")
                        .containsOnly(orderLineItem.getMenuId());
            });
        }

        @Test
        void 주문_항목이_존재하지_않으면_등록할_수_없다() {
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), MEAL.name(), LocalDateTime.now(), List.of());

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("주문 항목이 존재하지 않습니다. 주문을 등록할 수 없습니다.");
        }

        @Test
        void 주문_항목에_존재하지_않는_메뉴가_있으면_등록할_수_없다() {
            Menu notExistMenu = new Menu(null, BigDecimal.ONE, null);
            OrderLineItem wrongOrderLineItem = orderLineItemFixture(null, notExistMenu, 2);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), MEAL.name(),
                    LocalDateTime.now(), List.of(orderLineItem, wrongOrderLineItem));

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문항목에 존재하지 않는 메뉴가 있습니다. 주문을 등록할 수 없습니다.");
        }

        @Test
        void 해당하는_주문_테이블이_존재하지_않으면_등록할_수_없다() {
            OrderRequest orderRequest = new OrderRequest(Long.MIN_VALUE, null, LocalDateTime.now(), List.of(orderLineItem));

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 존재하지 않습니다. 주문을 등록할 수 없습니다.");
        }
    }

    @Nested
    class 주문상태_변경 {
        @Test
        void 주문_상태를_변경할_수_있다() {
            OrderResponse orderResponse = orderService.create(new OrderRequest(orderTable.getId(), MEAL.name(), LocalDateTime.now(), List.of(orderLineItem)));

            OrderRequest orderRequest = new OrderRequest(orderResponse.getOrderTableId(), orderResponse.getOrderStatus(), orderResponse.getOrderedTime(), orderResponse.getOrderLineItems());
            OrderResponse changed = orderService.changeOrderStatus(orderResponse.getId(), orderRequest);

            assertThat(changed.getOrderStatus()).isEqualTo(MEAL.name());
        }

        @Test
        void 주문이_존재하지_않으면_상태를_변경할_수_없다() {
            long notExistOrder = 100000L;
            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrder, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문이 존재하지 않습니다. 주문상태를 변경할 수 없습니다.");
        }

        @Test
        void 주문이_이미_완료된_경우_상태를_변경할_수_없다() {
            OrderResponse orderResponse = orderService.create(
                    new OrderRequest(orderTable.getId(), COMPLETION.name(), LocalDateTime.now(), List.of(orderLineItem))
            );

            OrderRequest changeRequest = new OrderRequest(orderResponse.getOrderTableId(), OrderStatus.COOKING.name(),
                    orderResponse.getOrderedTime(), orderResponse.getOrderLineItems());

            assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), changeRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문이 이미 완료되었습니다. 주문상태를 변경할 수 없습니다.");
        }
    }

    @Test
    void 주문목록을_조회한다() {
        OrderResponse order2 = orderService.create(
                new OrderRequest(orderTable.getId(), MEAL.name(), LocalDateTime.now(), List.of(orderLineItem))
        );
        OrderResponse order3 = orderService.create(
                new OrderRequest(orderTable.getId(), MEAL.name(), LocalDateTime.now(), List.of(orderLineItem))
        );

        List<OrderResponse> orderResponses = orderService.list();

        assertThat(orderResponses).hasSize(3)
                .extracting("id", "orderedTime")
                .containsExactly(tuple(order.getId(), order.getOrderedTime()),
                        tuple(order2.getId(), order2.getOrderedTime()),
                        tuple(order3.getId(), order3.getOrderedTime())
                );
    }
}
