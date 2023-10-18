package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.refactoring.domain.MenuGroup;
import kitchenpos.refactoring.domain.MenuGroupRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;
    private Menu menu;

    @BeforeEach
    void init() {
        orderTable = orderTableDao.save(OrderTableFixture.create(false, 4));
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("Leo's Pick"));
        menu = menuDao.save(MenuFixture.create("후라이드", 1000, savedMenuGroup.getId(), null));
    }

    @Nested
    class 주문을_생성할_때 {

        @Test
        void success() {
            // given
            OrderLineItem orderLineItem = OrderLineItemFixture.create(menu.getId(), 2L);
            Order order = OrderFixture.create(orderTable.getId(), List.of(orderLineItem));

            // when
            Order actual = orderService.create(order);

            // then
            assertThat(actual.getOrderLineItems()).hasSize(1);
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        @Test
        void 주문_상품이_비어있으면_실패() {
            // given
            Order order = OrderFixture.create(orderTable.getId(), Collections.emptyList());

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴에_등록되지_않은_주문_상품이_있으면_실패() {
            // given
            OrderLineItem orderLineItem = OrderLineItemFixture.create(0L, 1L);

            Order order = OrderFixture.create(orderTable.getId(), List.of(orderLineItem));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 등록되지_않은_주문테이블이면_실패() {
            // given
            OrderLineItem orderLineItem = OrderLineItemFixture.create(menu.getId(), 2L);
            Order order = OrderFixture.create(0L, List.of(orderLineItem));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있으면_실패() {
            // given
            OrderLineItem orderLineItem = OrderLineItemFixture.create(menu.getId(), 2L);
            OrderTable emptyTable = orderTableDao.save(OrderTableFixture.create(true, 0));
            Order order = OrderFixture.create(emptyTable.getId(), List.of(orderLineItem));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 주문_목록_조회() {
        // given
        OrderLineItem orderLineItem = OrderLineItemFixture.create(menu.getId(), 2L);
        Order order = OrderFixture.create(orderTable.getId(), List.of(orderLineItem));

        Order savedOrder = orderService.create(order);

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(List.of(savedOrder));
    }

    @Nested
    class 주문_상태를_변경할_때 {

        @Test
        void success() {
            // given
            OrderLineItem orderLineItem = OrderLineItemFixture.create(menu.getId(), 2L);
            Order order = OrderFixture.create(orderTable.getId(), List.of(orderLineItem));

            Order savedOrder = orderService.create(order);

            Order updateStatusOrder = OrderFixture.create(OrderStatus.MEAL, orderTable.getId());

            // when
            Order result = orderService.changeOrderStatus(savedOrder.getId(), updateStatusOrder);

            // then
            assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 등록되어있지_않은_주문이면_실패() {
            // given
            Order updateStatusOrder = OrderFixture.create(OrderStatus.MEAL, orderTable.getId());

            // when
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L, updateStatusOrder))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 주문_상태가_완료이면_실패(OrderStatus orderStatus) {
            // given
            OrderLineItem orderLineItem = OrderLineItemFixture.create(menu.getId(), 10L);

            Order order = OrderFixture.create(orderTable.getId(), List.of(orderLineItem));
            Order savedOrder = orderService.create(order);

            savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
            orderService.create(savedOrder);

            Long savedOrderId = savedOrder.getId();
            Order updateStatusOrder = OrderFixture.create(orderStatus, orderTable.getId());

            // when
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, updateStatusOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }


    }
}
