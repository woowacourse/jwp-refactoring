package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.양념_치킨;
import static kitchenpos.application.fixture.MenuFixture.포테이토_피자;
import static kitchenpos.application.fixture.MenuFixture.후라이드_치킨;
import static kitchenpos.application.fixture.MenuGroupFixture.치킨;
import static kitchenpos.application.fixture.MenuGroupFixture.피자;
import static kitchenpos.application.fixture.MenuProductFixture.메뉴_상품_생성;
import static kitchenpos.application.fixture.ProductFixture.양념_치킨;
import static kitchenpos.application.fixture.ProductFixture.포테이토_피자;
import static kitchenpos.application.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private Menu friedChicken;
    private Menu seasonedChicken;
    private Menu potatoPizza;

    @DisplayName("메뉴 및 메뉴 그룹 및 주문 테이블 생성")
    @BeforeEach
    void setUp() {
        Product productChicken1 = productDao.save(후라이드_치킨());
        Product productChicken2 = productDao.save(양념_치킨());
        MenuGroup chickenMenuGroup = menuGroupDao.save(치킨());

        Menu menuChicken1 = 후라이드_치킨(chickenMenuGroup);
        Menu menuChicken2 = 양념_치킨(chickenMenuGroup);

        MenuProduct menuProductChicken1 = 메뉴_상품_생성(menuChicken1, productChicken1, 1);
        MenuProduct menuProductChicken2 = 메뉴_상품_생성(menuChicken2, productChicken2, 1);

        menuChicken1.setMenuProducts(Collections.singletonList(menuProductChicken1));
        menuChicken2.setMenuProducts(Collections.singletonList(menuProductChicken2));

        friedChicken = menuDao.save(menuChicken1);
        seasonedChicken = menuDao.save(menuChicken2);

        Product productPizza = productDao.save(포테이토_피자());
        MenuGroup pizzaMenuGroup = menuGroupDao.save(피자());

        Menu menuPizza = 포테이토_피자(pizzaMenuGroup);
        MenuProduct menuProductPizza = 메뉴_상품_생성(menuPizza, productPizza, 1);

        menuPizza.setMenuProducts(Collections.singletonList(menuProductPizza));

        potatoPizza = menuDao.save(menuPizza);
    }


    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        OrderTable orderTable1 = orderTableDao.save(주문_테이블_생성());
        OrderTable orderTable2 = orderTableDao.save(주문_테이블_생성());
        TableGroup tableGroup = tableGroupDao.save(단체_지정_생성(orderTable1, orderTable2));

        Order order1 = 주문_생성_및_저장(orderTable1);
        OrderLineItem orderLineItem1 = 주문_항목_생성(order1, friedChicken, 1);
        orderLineItemDao.save(orderLineItem1);

        Order order2 = 주문_생성_및_저장(orderTable2);
        OrderLineItem orderLineItem2 = 주문_항목_생성(order2, friedChicken, 1);
        orderLineItemDao.save(orderLineItem2);

        // when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders).hasSize(2);
    }

    @DisplayName("주문 시 주문 항목이 비어있으면 예외를 발생한다.")
    @Test
    void orderWithEmptyOrderLineItem() {
        // given
        OrderTable orderTable1 = orderTableDao.save(주문_테이블_생성());
        OrderTable orderTable2 = orderTableDao.save(주문_테이블_생성());
        TableGroup tableGroup = tableGroupDao.save(단체_지정_생성(orderTable1, orderTable2));

        Order order1 = 주문_생성(orderTable1);
        OrderLineItem orderLineItem1 = 주문_항목_생성(order1, friedChicken, 1);

        // when & then
        assertThatThrownBy(
                () -> orderService.create(order1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 비어있습니다.");
    }

    @DisplayName("주문 시 등록되어있는 메뉴의 수와 다르면 예외를 발생한다")
    @Test
    void orderWithDifferentMenuSize() {
        // given
        OrderTable orderTable1 = orderTableDao.save(주문_테이블_생성());
        OrderTable orderTable2 = orderTableDao.save(주문_테이블_생성());
        TableGroup tableGroup = tableGroupDao.save(단체_지정_생성(orderTable1, orderTable2));

        Order order1 = 주문_생성(orderTable1);
        OrderLineItem orderLineItem1 = 주문_항목_생성(friedChicken, 1);
        OrderLineItem orderLineItem2 = 주문_항목_생성(friedChicken, 1);
        order1.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(order1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 수가 부족합니다.");
    }

    @DisplayName("주문 시 order table id로 ordertable을 찾아서 없으면 예외를 발생한다.")
    @Test
    void orderWithNotExistedOrderTableId() {
        // given
        Order order1 = 주문_생성(0L);
        OrderLineItem orderLineItem1 = 주문_항목_생성(friedChicken, 1);
        order1.setOrderLineItems(Arrays.asList(orderLineItem1));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(order1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 존재하지 않습니다.");
    }

    @DisplayName("주문 시 order table의 emtry가 true이면 예외를 발생한다.")
    @Test
    void orderWithEmptyOrderTableId() {
        // given
        OrderTable orderTable1 = orderTableDao.save(빈_주문_테이블_생성());
        TableGroup tableGroup = tableGroupDao.save(단체_지정_생성(orderTable1));

        Order order1 = 주문_생성(orderTable1);
        OrderLineItem orderLineItem1 = 주문_항목_생성(friedChicken, 1);
        order1.setOrderLineItems(Arrays.asList(orderLineItem1));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(order1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문 테이블입니다.");
    }

    @DisplayName("주문 시 정상적으로 주문된다.")
    @Test
    void orderSuccessful() {
        // given
        OrderTable orderTable1 = orderTableDao.save(주문_테이블_생성());
        OrderTable orderTable2 = orderTableDao.save(주문_테이블_생성());
        TableGroup tableGroup = tableGroupDao.save(단체_지정_생성(orderTable1, orderTable2));

        Order order1 = 주문_생성(orderTable1);
        OrderLineItem orderLineItem1 = 주문_항목_생성(order1, friedChicken, 1);
        order1.setOrderLineItems(Arrays.asList(orderLineItem1));

        // when
        Order savedOrder = orderService.create(order1);

        // then
        assertAll(
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("주문 상태 변경 시 order id 에 대하여 order가 존재하지 않으면 예외를 발생한다.")
    @Test
    void updateStatusWithNotExistedOrder() {
        // given
        Order order = 주문_상태_변경(OrderStatus.MEAL);

        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(1L, order)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문번호입니다.");
    }

    @DisplayName("주문 상태 변경 시 주문 상태가 이미 완료이면 예외를 발생한다.")
    @Test
    void updateStatusWithCompletedOrder() {
        // given
        OrderTable orderTable1 = orderTableDao.save(주문_테이블_생성());
        OrderTable orderTable2 = orderTableDao.save(주문_테이블_생성());
        TableGroup tableGroup = tableGroupDao.save(단체_지정_생성(orderTable1, orderTable2));

        Order order1 = 주문_생성_및_저장(orderTable1);
        OrderLineItem orderLineItem1 = 주문_항목_생성(order1, friedChicken, 1);
        orderLineItemDao.save(orderLineItem1);

        Order order = 주문_상태_변경(OrderStatus.COMPLETION);

        orderService.changeOrderStatus(order1.getId(), order);

        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(order1.getId(), order)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다.");
    }

    @DisplayName("주문 상태 변경시 정상 저장")
    @Test
    void updateOrderStatus() {
        // given
        OrderTable orderTable1 = orderTableDao.save(주문_테이블_생성());
        OrderTable orderTable2 = orderTableDao.save(주문_테이블_생성());
        TableGroup tableGroup = tableGroupDao.save(단체_지정_생성(orderTable1, orderTable2));

        Order order1 = 주문_생성_및_저장(orderTable1);
        OrderLineItem orderLineItem1 = 주문_항목_생성(order1, friedChicken, 1);
        orderLineItemDao.save(orderLineItem1);

        // when
        Order order = 주문_상태_변경(OrderStatus.COMPLETION);
        Order changedOrder = orderService.changeOrderStatus(order1.getId(), order);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    private Order 주문_상태_변경(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        return order;
    }

    private OrderLineItem 주문_항목_생성(final Menu menu, final long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    private OrderLineItem 주문_항목_생성(final Order order, final Menu menu, final long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);
        orderLineItem.setOrderId(order.getId());

        return orderLineItem;
    }

    private Order 주문_생성(final OrderTable orderTable) {
        Order order = new Order();
        order.setOrderTableId(orderTable.getId());

        return order;
    }

    private Order 주문_생성_및_저장(final OrderTable orderTable) {
        Order order = 주문_생성(orderTable);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());

        return orderDao.save(order);
    }

    private Order 주문_생성(final Long orderTableId) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);

        return order;
    }

    private TableGroup 단체_지정_생성(final OrderTable... orderTables) {
        List<OrderTable> orderTableList = Arrays.stream(orderTables)
                .collect(Collectors.toList());

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTableList);

        return tableGroup;
    }

    private OrderTable 주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(2);

        return orderTable;
    }


    private OrderTable 빈_주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        return orderTable;
    }
}
