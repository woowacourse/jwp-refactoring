package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.fixture.OrderTableFixture.테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
public class OrderServiceTest {

    @Autowired
    private OrderService sut;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private Menu menu;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹("피자"));
        menu = menuDao.save(메뉴("피자", 8900L, menuGroup.getId()));
    }

    @Nested
    class 주문을_할_때 {

        @Test
        void 주문_항목_목록이_없다면_예외를_던진다() {
            // given
            Order order = 주문(1L, List.of());

            // expect
            assertThatThrownBy(() -> sut.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목 목록이 있어야 합니다.");
        }

        @Test
        void 등록되지_않은_메뉴를_주문하면_예외를_던진다() {
            // given
            OrderLineItem orderLineItem = 주문_항목(null, MAX_VALUE, 2L);
            Order order = 주문(1L, List.of(orderLineItem));

            // expect
            assertThatThrownBy(() -> sut.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록되지 않은 메뉴를 주문할 수 없습니다.");
        }

        @Test
        void 등록되지_않은_테이블에서_주문을_하는_경우_예외를_던진다() {
            // given
            OrderLineItem orderLineItem = 주문_항목(null, menu.getId(), 2L);
            Order order = 주문(MAX_VALUE, List.of(orderLineItem));

            // expect
            assertThatThrownBy(() -> sut.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록되지 않은 테이블에서 주문을 할 수 없습니다.");
        }

        @Test
        void 빈_테이블에서_주문을_하는_경우_예외를_던진다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(true));
            OrderLineItem orderLineItem = 주문_항목(null, menu.getId(), 2L);
            Order order = 주문(orderTable.getId(), List.of(orderLineItem));

            // expect
            assertThatThrownBy(() -> sut.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("빈 테이블인 경우 주문을 할 수 없습니다.");
        }

        @Test
        void 주문에_성공하는_경우_주문의_상태가_조리중으로_변경된다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(false));
            OrderLineItem orderLineItem = 주문_항목(null, menu.getId(), 2L);
            Order order = 주문(orderTable.getId(), List.of(orderLineItem));

            // when
            Order savedOrder = sut.create(order);

            // then
            Order findOrder = orderDao.findById(savedOrder.getId()).get();
            assertThat(findOrder.getOrderStatus()).isEqualTo(COOKING.name());
        }
    }

    @Test
    void 주문_목록을_조회한다() {
        // given
        OrderTable orderTable = orderTableDao.save(테이블(false));
        Order order1 = orderDao.save(주문(orderTable.getId(), COOKING));
        OrderLineItem orderLineItem1 = orderLineItemDao.save(주문_항목(order1.getId(), menu.getId(), 2L));
        order1.setOrderLineItems(List.of(orderLineItem1));
        Order order2 = orderDao.save(주문(orderTable.getId(), COOKING));
        OrderLineItem orderLineItem2 = orderLineItemDao.save(주문_항목(order2.getId(), menu.getId(), 2L));
        order2.setOrderLineItems(List.of(orderLineItem2));

        // when
        List<Order> orders = sut.list();

        // then
        assertThat(orders)
                .usingRecursiveComparison()
                .isEqualTo(List.of(order1, order2));
    }

    @Nested
    class 주문의_상태를_변경할_때 {

        @Test
        void 존재하지_않는_주문이라면_예외가_발생한다() {
            // expect
            assertThatThrownBy(() -> sut.changeOrderStatus(MAX_VALUE, 주문(1L, COMPLETION)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문을 찾을 수 없습니다.");
        }

        @Test
        void 완료된_주문이라면_예외가_발생한다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(false));
            Order order = orderDao.save(주문(orderTable.getId(), COMPLETION));
            order.setOrderStatus(COOKING.name());

            // when
            assertThatThrownBy(() -> sut.changeOrderStatus(order.getId(), order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("완료된 주문의 상태는 변경할 수 없습니다.");
        }

        @Test
        void 주문의_상태가_정상적으로_변경한다() {
            // given
            OrderTable orderTable = orderTableDao.save(테이블(false));
            Order order = orderDao.save(주문(orderTable.getId(), COOKING));
            order.setOrderStatus(MEAL.name());

            // when
            sut.changeOrderStatus(order.getId(), order);

            // then
            Order savedOrder = orderDao.findById(order.getId()).get();
            assertThat(savedOrder.getOrderStatus()).isEqualTo(MEAL.name());
        }
    }
}
