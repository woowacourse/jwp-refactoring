package kitchenpos.application;

import static java.lang.Long.MAX_VALUE;
import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문_상품;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
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
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest implements ServiceTest {

    private Menu menu;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLineItemDao orderLineItemDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹("메뉴 그룹"));
        this.menu = menuDao.save(메뉴(menuGroup.getId(), "메뉴", BigDecimal.valueOf(18_000L)));
    }

    public static MenuGroup 메뉴_그룹(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    @Test
    void 주문은_최소_한_개_이상의_주문_상품을_가져야한다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        final Order order = 주문(orderTable.getId());

        // expected
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문은_존재하지_않는_메뉴를_가진_주문_상품을_가질_수_없다() {
        // given
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        final Order order = 주문(orderTable.getId(), 주문_상품(MAX_VALUE, 10L));

        // expected
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문은_특정_하나의_주문_테이블에_속해야한다() {
        // given
        final OrderLineItem orderLineItem = 주문_상품(menu.getId(), 1L);
        final Order order = 주문(null, orderLineItem);

        // expected
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈자리일_경우_주문을_할_수_없다() {
        // given
        final OrderLineItem orderLineItem = 주문_상품(menu.getId(), 1L);
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, true));
        final Order order = 주문(orderTable.getId(), orderLineItem);

        //expected
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_완료되면_주문_상태를_조리_중으로_변경한다() {
        // given
        final OrderLineItem orderLineItem = 주문_상품(menu.getId(), 1L);
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        final Order order = 주문(orderTable.getId(), orderLineItem);

        // when
        final Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 주문이_완료_되면_주문_상품은_해당_주문에_귀속된다() {
        // given
        final OrderLineItem orderLineItem = 주문_상품(menu.getId(), 1L);
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        final Order order = 주문(orderTable.getId(), orderLineItem);

        // when
        final Order savedOrder = orderService.create(order);

        // then
        orderLineItem.setOrderId(savedOrder.getId());
        List<OrderLineItem> allByOrderId = orderLineItemDao.findAllByOrderId(savedOrder.getId());
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(allByOrderId).isNotEmpty();
            softly.assertThat(allByOrderId)
                    .usingRecursiveComparison()
                    .comparingOnlyFields("orderId")
                    .isEqualTo(Collections.singletonList(orderLineItem));
        });
    }

    @Test
    void 존재_하지_않는_주문에_대해서_상태를_변경할_수_없다() {
        // given
        final OrderLineItem orderLineItem = 주문_상품(menu.getId(), 1L);
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        final Order order = 주문(orderTable.getId(), orderLineItem);

        // expected
        assertThatThrownBy(() -> orderService.changeOrderStatus(MAX_VALUE, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_완료된_주문_상태는_변경할_수_없다() {
        // given
        final OrderLineItem orderLineItem = 주문_상품(menu.getId(), 1L);
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        final Order order = orderDao.save(주문(orderTable.getId(), OrderStatus.COMPLETION, orderLineItem));

        // expected
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_변경한다() {
        // given
        final OrderLineItem orderLineItem = 주문_상품(menu.getId(), 1L);
        final OrderTable orderTable = orderTableDao.save(주문_테이블(10, false));
        final Order order = orderDao.save(주문(orderTable.getId(), OrderStatus.MEAL, orderLineItem));

        // when
        Order savedOrder = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }
}

