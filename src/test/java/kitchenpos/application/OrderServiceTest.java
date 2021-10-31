package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Order Service 테스트")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("Order를 생성할 때")
    @Nested
    class createOrder {

        @DisplayName("Order의 OrderLineItem이 없다면 예외가 발생한다.")
        @Test
        void orderLineItemsEmptyException() {
            // given
            TableGroup tableGroup = tableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = menuDao.save(Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts));

            Order order = Order를_생성한다(orderTable.getId(), new ArrayList<>());

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("동일한 메뉴를 시킬 때 Quantity가 아닌 OrderLineItem을 추가해서 OrderLineItem 개수와 Menu 개수가 다를 경우 예외가 발생한다.")
        @Test
        void orderLineItemCountNonMatchWithMenuException() {
            // given
            TableGroup tableGroup = tableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = menuDao.save(Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts));

            OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu.getId(), 1);
            OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu.getId(), 1);

            Order order = Order를_생성한다(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("orderTable이 실제로 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void orderTableNotFoundException() {
            // given
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu1 = menuDao.save(Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts));
            Menu menu2 = menuDao.save(Menu를_생성한다("색다른 메뉴", 6_600, menuGroup.getId(), menuProducts));

            OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu1.getId(), 1);
            OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu2.getId(), 1);

            Order order = Order를_생성한다(-1L, Arrays.asList(orderLineItem1, orderLineItem2));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order의 OrderTable의 상태가 이미 Empty일 경우 예외가 발생한다.")
        @Test
        void orderTableStatusEmptyException() {
            // given
            TableGroup tableGroup = tableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId(), true));
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu1 = menuDao.save(Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts));
            Menu menu2 = menuDao.save(Menu를_생성한다("색다른 메뉴", 6_600, menuGroup.getId(), menuProducts));

            OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu1.getId(), 1);
            OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu2.getId(), 1);

            Order order = Order를_생성한다(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("정상적으로 저장될 경우 orderLineItem도 함께 저장된다.")
        @Test
        void success() {
            // given
            TableGroup tableGroup = tableGroupDao.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
            MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

            Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu1 = menuDao.save(Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts));
            Menu menu2 = menuDao.save(Menu를_생성한다("색다른 메뉴", 6_600, menuGroup.getId(), menuProducts));

            OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu1.getId(), 1);
            OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu2.getId(), 1);

            Order order = Order를_생성한다(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            Order savedOrder = orderService.create(order);

            // then
            assertThat(savedOrder.getId()).isNotNull();
            assertThat(savedOrder.getOrderedTime()).isBeforeOrEqualTo(LocalDateTime.now());
            assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
            assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
            assertThat(savedOrder.getOrderLineItems()).extracting("seq").isNotEmpty();
        }
    }

    @DisplayName("모든 Product를 조회한다.")
    @Test
    void list() {
        // given
        List<Order> beforeSavedOrders = orderService.list();
        beforeSavedOrders.add(orderService를_통해_Order를_생성한다());

        // when
        List<Order> afterSavedOrders = orderService.list();

        // then
        assertThat(afterSavedOrders).hasSize(beforeSavedOrders.size());
        assertThat(afterSavedOrders).usingRecursiveComparison()
            .isEqualTo(beforeSavedOrders);
    }

    @DisplayName("Order의 상태를 변경할 때")
    @Nested
    class ChangeOrderStatus {

        @DisplayName("ID에 해당하는 Order가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistOrderIdException() {
            // given
            Order statusOrder = Status_변화용_Order를_생성한다(COOKING);

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, statusOrder))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order의 상태가 이미 COMPLETION일 경우 예외가 발생한다.")
        @Test
        void alreadyCompletionStatusException() {
            // given
            Order order = orderService를_통해_Order를_생성한다();
            Order statusOrder = Status_변화용_Order를_생성한다(COMPLETION);

            orderService.changeOrderStatus(order.getId(), statusOrder);

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), Status_변화용_Order를_생성한다(COOKING)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order의 상태가 정상적으로 변경되고 반환된다.")
        @Test
        void success() {
            // given
            Order order = orderService를_통해_Order를_생성한다();
            Order statusOrder = Status_변화용_Order를_생성한다(COMPLETION);

            // when
            Order changedOrder = orderService.changeOrderStatus(order.getId(), statusOrder);

            // then
            assertThat(changedOrder.getOrderStatus()).isEqualTo(statusOrder.getOrderStatus());
        }
    }

    private Order orderService를_통해_Order를_생성한다() {
        TableGroup tableGroup = tableGroupDao.save(TableGroup을_생성한다());
        OrderTable orderTable = orderTableDao.save(OrderTable을_생성한다(2, tableGroup.getId()));
        MenuGroup menuGroup = menuGroupDao.save(MenuGroup을_생성한다("엄청난 그룹"));

        Product 치즈버거 = productDao.save(Product를_생성한다("치즈버거", 4_000));
        Product 콜라 = productDao.save(Product를_생성한다("치즈버거", 1_600));
        MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
        MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
        List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

        Menu menu1 = menuDao.save(Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts));
        Menu menu2 = menuDao.save(Menu를_생성한다("색다른 메뉴", 6_600, menuGroup.getId(), menuProducts));

        OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu1.getId(), 1);
        OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu2.getId(), 1);

        return orderService.create(Order를_생성한다(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2)));
    }

    private Order Order를_생성한다(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }

    private Order Status_변화용_Order를_생성한다(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());

        return order;
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, Long tableGroupId) {
        return OrderTable을_생성한다(numberOfGuests, tableGroupId, false);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, Long tableGroupId, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(isEmpty);

        return orderTable;
    }

    private TableGroup TableGroup을_생성한다() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroup;
    }

    private OrderLineItem OrderLineItem을_생성한다(Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    private Menu Menu를_생성한다(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }

    private MenuProduct MenuProduct를_생성한다(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    private Product Product를_생성한다(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}