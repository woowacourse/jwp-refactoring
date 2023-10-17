package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.Fixture.Fixture.menuFixture;
import static kitchenpos.Fixture.Fixture.menuGroupFixture;
import static kitchenpos.Fixture.Fixture.orderFixture;
import static kitchenpos.Fixture.Fixture.orderLineItemFixture;
import static kitchenpos.Fixture.Fixture.orderTableFixture;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class OrderServiceTest {

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    private MenuGroup menuGroup;
    private Menu menu;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(menuGroupFixture("메뉴 그룹"));
        menu = menuRepository.save(menuFixture("메뉴", BigDecimal.valueOf(30000), menuGroup.getId(), null));
        orderTable = orderTableDao.save(orderTableFixture(null, 1, false));
        orderLineItem = orderLineItemFixture(null, menu.getId(), 1);
    }

    @Nested
    class 주문등록 {

        @Test
        void 주문을_등록한다() {
            Order order = orderFixture(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem));

            Order savedOrder = orderService.create(order);

            assertSoftly(softly -> {
                softly.assertThat(savedOrder.getId()).isNotNull();
                softly.assertThat(savedOrder).usingRecursiveComparison()
                        .ignoringFields("id", "orderLineItems")
                        .isEqualTo(order);

                softly.assertThat(savedOrder.getOrderLineItems()).extracting("orderId", "menuId")
                        .containsOnly(Tuple.tuple(orderLineItem.getOrderId(), orderLineItem.getMenuId()));
            });
        }

        @Test
        void 주문_항목이_존재하지_않으면_등록할_수_없다() {
            Order order = orderFixture(orderTable.getId(), null, LocalDateTime.now(), List.of());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목이 존재하지 않습니다. 주문을 등록할 수 없습니다.");
        }

        @Test
        void 주문_항목에_존재하지_않는_메뉴가_있으면_등록할_수_없다() {
            long notExistMenuId = Long.MIN_VALUE;
            OrderLineItem wrongOrderLineItem = orderLineItemFixture(null, notExistMenuId, 2);
            Order order = orderFixture(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem, wrongOrderLineItem));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문항목에 존재하지 않는 메뉴가 있습니다. 주문을 등록할 수 없습니다.");
        }

        @Test
        void 해당하는_주문_테이블이_존재하지_않으면_등록할_수_없다() {
            Order order = orderFixture(Long.MIN_VALUE, null, LocalDateTime.now(), List.of(orderLineItem));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 존재하지 않습니다. 주문을 등록할 수 없습니다.");
        }
    }

    @Nested
    class 주문상태_변경 {
        @Test
        void 주문_상태를_변경할_수_있다() {
            Order order = orderService.create(orderFixture(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem)));
            order.setOrderStatus(MEAL.name());

            Order updated = orderService.changeOrderStatus(order.getId(), order);

            assertThat(updated.getOrderStatus()).isEqualTo(MEAL.name());
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
            Order completedOrder = orderService.create(orderFixture(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem)));
            completedOrder.setOrderStatus(COMPLETION.name());

            orderService.changeOrderStatus(completedOrder.getId(), completedOrder);

            assertThatThrownBy(() -> orderService.changeOrderStatus(completedOrder.getId(), completedOrder))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문이 이미 완료되었습니다. 주문상태를 변경할 수 없습니다.");
        }
    }

    @Test
    void 주문목록을_조회한다() {
        Order order1 = orderService.create(orderFixture(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem)));
        Order order2 = orderService.create(orderFixture(orderTable.getId(), null, LocalDateTime.now(), List.of(orderLineItem)));

        List<Order> orderList = orderService.list();

        assertThat(orderList).hasSize(2)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("orderLineItems")
                .containsExactly(order1, order2);
    }
}
