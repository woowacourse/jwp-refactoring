package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = "/initialization.sql")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    private Order order;

    @BeforeEach
    void setup() {
        order = new Order();
    }

    @DisplayName("주문 메뉴(내역)가 비어있을 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderLineItemIsEmpty() {
        //given
        order.setOrderLineItems(Collections.emptyList());

        //when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 하려는 메뉴가 존재하지 않을 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderLineItemIsNotExists() {
        //given
        Long invalidId = 99L;

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(invalidId);

        order.setOrderLineItems(List.of(orderLineItem));

        assertThat(menuDao.findById(invalidId)).isEmpty();

        //when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 한 테이블이 존재하지 않을 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsNotExists() {
        //given
        Long menuId = saveMenu();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(99L);

        assertThat(menuDao.findById(menuId)).isPresent();
        assertThat(orderTableDao.findById(99L)).isEmpty();

        //when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 한 테이블이 주문할 수 없는(empty) 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsEmpty() {
        //given
        Long menuId = saveMenu();
        Long orderTableId = saveOrderTableForEmpty(true);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(orderTableId);

        assertThat(menuDao.findById(menuId)).isPresent();
        assertThat(orderTableDao.findById(orderTableId)).isPresent();
        assertThat(orderTableDao.findById(orderTableId).get().isEmpty()).isTrue();

        //when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 생성될 때, 주문 관련 정보가 변경된다.")
    @Test
    void createSuccessTest_ChangeOrderInformation() {
        //given
        Long menuId = saveMenu();
        Long orderTableId = saveOrderTableForEmpty(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(orderTableId);

        assertThat(order.getOrderStatus()).isNull();
        assertThat(order.getOrderedTime()).isNull();

        //when
        Order savedOrder = orderService.create(order);

        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(order.getOrderedTime()).isNotNull();
    }

    @DisplayName("주문이 생성될 때, 주문에 포함된 메뉴들도 저장된다.")
    @Test
    void createSuccessTest_SaveWithOrderLineItems() {
        //given
        Long menuId = saveMenu();
        Long orderTableId = saveOrderTableForEmpty(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(orderTableId);

        Optional<OrderLineItem> findOrderLineItemBeforeCreatingOrder = orderLineItemDao.findAll()
                .stream()
                .filter(item -> item.getMenuId().equals(menuId))
                .findAny();

        assertThat(findOrderLineItemBeforeCreatingOrder).isEmpty();

        //when
        orderService.create(order);

        //then
        Optional<OrderLineItem> findOrderLineItemAfterCreatingOrder = orderLineItemDao.findAll()
                .stream()
                .filter(item -> item.getMenuId().equals(menuId))
                .findAny();

        assertThat(findOrderLineItemAfterCreatingOrder).isPresent();
    }

    @DisplayName("모든 주문 목록을 조회하면, 각 주문에 포함된 메뉴들도 함께 조회된다.")
    @Test
    void listSuccessTest() {
        //given
        Long menuId = saveMenu();
        Long orderTableId = saveOrderTableForEmpty(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(orderTableId);

        Order savedOrder = orderService.create(order);

        //when
        Order findOrder = orderDao.findById(savedOrder.getId()).get();
        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(findOrder.getId());

        //then
        assertThat(orderLineItems).hasSize(1);
        assertThat(orderLineItems.get(0))
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(orderLineItem);
    }

    @DisplayName("변경하려는 주문이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderIsNotExists() {
        //given
        assertThat(orderDao.findById(99L)).isEmpty();

        //when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(99L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 완료(COMPLETION) 상태인 경우, 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderStatusIsCompletion() {
        //given
        Long menuId = saveMenu();
        Long orderTableId = saveOrderTableForEmpty(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(orderTableId);
        order.setOrderStatus("COMPLETION");
        order.setOrderedTime(LocalDateTime.now());

        //when then
        Order otherOrder = new Order();
        Long savedOrderId = orderDao.save(order).getId();

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, otherOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("올바르지 않은 주문 상태인 경우, 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderStatusIsNotAvailable() {
        //given
        Long menuId = saveMenu();
        Long orderTableId = saveOrderTableForEmpty(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(orderTableId);
        order.setOrderStatus("COOKING");
        order.setOrderedTime(LocalDateTime.now());

        Long savedOrderId = orderDao.save(order).getId();

        //when then
        Order otherOrder = new Order();
        otherOrder.setOrderStatus("sadfasdf");

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, otherOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusSuccessTest() {
        //given
        Long menuId = saveMenu();
        Long orderTableId = saveOrderTableForEmpty(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(orderTableId);
        order.setOrderStatus("COOKING");
        order.setOrderedTime(LocalDateTime.now());

        Long savedOrderId = orderDao.save(order).getId();

        //when
        order.setOrderStatus("COMPLETION");

        orderService.changeOrderStatus(savedOrderId, order);

        //then
        Order findOrder = orderDao.findById(savedOrderId).get();

        assertThat(findOrder.getOrderStatus()).isEqualTo("COMPLETION");
    }

    private Long saveMenu() {
        Long menuGroupId = saveMenuGroup();

        Menu menu = new Menu();
        menu.setName("TestMenu");
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuGroupId(menuGroupId);

        return menuDao.save(menu).getId();
    }

    private Long saveMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("TestMenuGroup");

        return menuGroupDao.save(menuGroup).getId();
    }

    private Long saveOrderTableForEmpty(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

        return orderTableDao.save(orderTable).getId();
    }

}
