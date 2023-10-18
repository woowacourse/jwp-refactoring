package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderDao orderDao;

    private OrderTable mockOrderTable;
    private Menu mockMenu;

    @BeforeEach
    void init() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        mockOrderTable = orderTableDao.save(orderTable);

        final MenuGroup menuGroup = new MenuGroup("테스트 메뉴 그룹");
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        final Menu menu = new Menu(
            "테스트 메뉴",
            BigDecimal.valueOf(10000),
            savedMenuGroup
        );
        mockMenu = menuRepository.save(menu);
    }

    @Test
    void 주문_생성한다() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(mockMenu.getId());
        orderLineItem.setQuantity(2);
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(mockOrderTable.getId());

        // when
        final Order result = orderService.create(order);

        // then
        assertThat(result.getOrderLineItems()).hasSize(1);
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 주문_생성_시_주문라인아이템이_없으면_예외가_발생한다() {
        // given
        final Order order = new Order();
        order.setOrderLineItems(Collections.emptyList());
        order.setOrderTableId(mockOrderTable.getId());

        // when then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_시_주문라인아이템의_메뉴가_존재하지_않으면_예외가_발생한다() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(mockOrderTable.getId());

        // when
        final Long notExistMenuId = 99999L;
        final Optional<Order> emptyOrder = orderDao.findById(notExistMenuId);
        orderLineItem.setMenuId(notExistMenuId);

        // then
        assertThat(emptyOrder).isEmpty();
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_시_테이블이_비어있으면_예외가_발생한다() {
        // given when
        final OrderTable emptyOrderTable = new OrderTable();
        emptyOrderTable.setEmpty(true);
        final OrderTable savedOrderTable = orderTableDao.save(emptyOrderTable);

        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(mockMenu.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(savedOrderTable.getId());

        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(mockMenu.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(mockOrderTable.getId());

        final Order createdOrder = orderService.create(order);

        final Order updateStatusOrder = new Order();
        updateStatusOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when
        final Order result = orderService.changeOrderStatus(createdOrder.getId(),
            updateStatusOrder);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문_상태가_완료된_후에는_상태를_변경할_수_없다(final OrderStatus orderStatus) {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(mockMenu.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(mockOrderTable.getId());

        final Order createdOrder = orderService.create(order);
        createdOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.create(createdOrder);

        // when
        final Order updateStatusOrder = new Order();
        updateStatusOrder.setOrderStatus(orderStatus.name());

        // then
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(createdOrder.getId(), updateStatusOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_메뉴의_상품들_개수와_기존_메뉴의_상품들_개수가_같지않으면_예외가_발생한다() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(mockMenu.getId());
        order.setOrderTableId(mockOrderTable.getId());

        // when
        final OrderLineItem notSavedMenuOrderItem = new OrderLineItem();
        order.setOrderLineItems(List.of(orderLineItem, notSavedMenuOrderItem));

        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_주문_상태를_변경할_경우_예외가_발생한다() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(mockMenu.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(mockOrderTable.getId());

        final Order createdOrder = orderService.create(order);
        createdOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.create(createdOrder);

        // when
        final String notExistOrderStatusName = "없는주문상태";
        final Order updateStatusOrder = new Order();
        updateStatusOrder.setOrderStatus(notExistOrderStatusName);

        // then
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(createdOrder.getId(), updateStatusOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
