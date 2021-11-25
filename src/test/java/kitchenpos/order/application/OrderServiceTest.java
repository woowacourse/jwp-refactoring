package kitchenpos.order.application;

import kitchenpos.application.BaseServiceTest;
import kitchenpos.application.TestFixtureFactory;
import kitchenpos.builder.OrderBuilder;
import kitchenpos.builder.OrderLineItemBuilder;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class OrderServiceTest extends BaseServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    MenuService menuService;
    @Autowired
    MenuGroupService menuGroupService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    TableService tableService;
    @Autowired
    EntityManager em;

    @DisplayName("[주문 생성] 주문을 정상적으로 생성한다.")
    @Test
    void create() {
        // given
        Menu menu = 메뉴_생성();
        OrderTable table = 활성화된_테이블_생성();
        OrderLineItem orderLineItem = TestFixtureFactory.주문_항목_생성(menu.getId(), 1L);
        Order order = TestFixtureFactory.주문_생성(table, orderLineItem);

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderTableId()).isEqualTo(table.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING");
        assertThat(savedOrder.getOrderedTime()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().getValues())
                .extracting("order")
                .contains(savedOrder);
    }

    @DisplayName("[주문 생성] 주문항목에 있는 메뉴가 존재하지 않으면 예외가 발생한다.")
    @Test
    void createWithOrderLineItemInNonExistMenu() {
        // given
        OrderTable table = 활성화된_테이블_생성();
        OrderLineItem orderLineItem = new OrderLineItemBuilder()
                .seq(null)
                .order(null)
                .menuId(999999L)
                .quantity(1L)
                .build();
        Order order = TestFixtureFactory.주문_생성(table, orderLineItem);

        // when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[주문 생성] 주문한 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createWithNonExistTable() {
        // given
        Menu menu = 메뉴_생성();
        OrderTable table = TestFixtureFactory.테이블_생성(1L, null, 0, false);
        OrderLineItem orderLineItem = TestFixtureFactory.주문_항목_생성(menu.getId(), 1L);
        Order order = TestFixtureFactory.주문_생성(table, orderLineItem);

        // when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[주문 생성] 주문한 테이블이 비활성화 상태면 예외가 발생한다.")
    @Test
    void createWithEmptyTable() {
        // given
        Menu menu = 메뉴_생성();
        OrderTable table = TestFixtureFactory.빈_테이블_생성();
        OrderTable savedOrderTable = tableService.create(table);
        OrderLineItem orderLineItem = TestFixtureFactory.주문_항목_생성(menu.getId(), 1L);
        Order order = TestFixtureFactory.주문_생성(savedOrderTable, orderLineItem);

        // when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[주문 전체 조회] 주문 전체를 조회한다.")
    @Test
    void list() {
        주문_생성();
        List<Order> findOrders1 = orderService.list();
        assertThat(findOrders1).hasSize(1);

        주문_생성();
        List<Order> findOrders2 = orderService.list();
        assertThat(findOrders2).hasSize(2);

        주문_생성();
        List<Order> findOrders3 = orderService.list();
        assertThat(findOrders3).hasSize(3);
    }

    @DisplayName("[주문 상태 변경] 주문의 상태를 식사중, 식사완료로 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        Order cookingOrder = 주문_생성();
        Order requestOrderStatusMeal = new OrderBuilder()
                .orderStatus("MEAL")
                .build();
        Order requestOrderStatusCompletion = new OrderBuilder()
                .orderStatus("COMPLETION")
                .build();
        em.flush();
        em.clear();

        // when
        Order mealOrder = orderService.changeOrderStatus(cookingOrder.getId(), requestOrderStatusMeal);
        em.flush();
        em.clear();
        Order completionOrder = orderService.changeOrderStatus(cookingOrder.getId(), requestOrderStatusCompletion);
        em.flush();
        em.clear();

        // then
        assertThat(cookingOrder.getOrderStatus()).isEqualTo("COOKING");
        assertThat(mealOrder.getOrderStatus()).isEqualTo("MEAL");
        assertThat(completionOrder.getOrderStatus()).isEqualTo("COMPLETION");
    }

    @DisplayName("[주문 상태 변경] 주문 ID가 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeOrderStatusWithNonExistOrder() {
        // given
        Order requestOrderStatusMeal = new OrderBuilder()
                .orderStatus("MEAL")
                .build();

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(9999L, requestOrderStatusMeal))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[주문 상태 변경] 이미 완료된 주문을 상태 변경하면 예외가 발생한다.")
    @Test
    void changeOrderStatusWithCompletionOrder() {
        // given
        Order cookingOrder = 주문_생성();
        Order requestOrderStatusCompletion = new OrderBuilder()
                .orderStatus("COMPLETION")
                .build();
        Order completionOrder = orderService.changeOrderStatus(cookingOrder.getId(), requestOrderStatusCompletion);

        Order requestOrderStatusMeal = new OrderBuilder()
                .orderStatus("MEAL")
                .build();

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrder.getId(), requestOrderStatusMeal))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu 메뉴_생성() {
        Product product = TestFixtureFactory.상품_후라이드_치킨();
        Product savedProduct = productRepository.save(product);
        MenuGroup menuGroup = TestFixtureFactory.메뉴그룹_인기_메뉴();
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        MenuProduct menuProduct = TestFixtureFactory.메뉴상품_매핑_생성(savedProduct.getId(), 1L);
        Menu menu = TestFixtureFactory.메뉴_후라이드_치킨_한마리(savedMenuGroup.getId(), menuProduct);
        return menuService.create(menu);
    }

    private OrderTable 활성화된_테이블_생성() {
        OrderTable orderTable = TestFixtureFactory.빈_테이블_생성();
        OrderTable savedOrderTable = tableService.create(orderTable);
        return tableService.changeEmpty(savedOrderTable.getId(), TestFixtureFactory.테이블_생성(false));
    }

    private Order 주문_생성() {
        Menu menu = 메뉴_생성();
        OrderTable table = 활성화된_테이블_생성();
        OrderLineItem orderLineItem = TestFixtureFactory.주문_항목_생성(menu.getId(), 1L);
        Order order = TestFixtureFactory.주문_생성(table, orderLineItem);
        return orderService.create(order);
    }
}