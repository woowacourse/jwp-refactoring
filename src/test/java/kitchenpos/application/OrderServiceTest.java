package kitchenpos.application;

import com.sun.tools.javac.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.vo.order.ChangeOrderStatusRequest;
import kitchenpos.vo.order.OrderLineItemRequest;
import kitchenpos.vo.order.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/h2-truncate.sql"})
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private Menu savedMenu;
    private MenuProduct savedMenuProduct;
    private Product savedProduct;
    private MenuGroup savedMenuGroup;

    private OrderTable savedOrderTable;

    @BeforeEach
    void setup() {
        Product product = new Product("치킨", BigDecimal.valueOf(10000L));
        savedProduct = productRepository.save(product);

        MenuGroup menuGroup = new MenuGroup("즐겨찾는 음식");
        savedMenuGroup = menuGroupRepository.save(menuGroup);

        Menu menu = new Menu("두마리치킨", BigDecimal.valueOf(20000), savedMenuGroup.getId());
        savedMenu = menuRepository.save(menu);

        MenuProduct menuProduct = new MenuProduct(savedMenu, savedProduct, 2l);
        savedMenuProduct = menuProductRepository.save(menuProduct);

        OrderTable orderTable = new OrderTable(4, false);
        savedOrderTable = orderTableRepository.save(orderTable);
    }

    @Test
    @DisplayName("주문 등록에 성공한다.")
    void succeedInRegisteringOrder() {
        // given
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));

        // when
        Order savedOrder = orderService.create(orderRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrder.getId()).isNotNull();
            softly.assertThat(savedOrder.getOrderLineItems()).hasSize(1);
        });
    }

    @Test
    @DisplayName("주문 항목이 없으면 주문 등록 시 예외가 발생한다.")
    void failToRegisterOrderWithNonExistOrderLine() {
        // given
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), null);

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 주문 항목으로는 주문을 등록할 수 없습니다.");
    }

    @Test
    @DisplayName("등록되지 않은 메뉴 항목을 사용하여 주문 등록 시 예외가 발생한다.")
    void failToRegisterOrderWithNonRegisteredOrderLine() {
        // given
        Long unsavedMenuId = 1000L;
        OrderLineItemRequest wrongOrderLineItemRequest = new OrderLineItemRequest(unsavedMenuId, 2);

        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), List.of(wrongOrderLineItemRequest));

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 메뉴에는 주문을 등록할 수 없습니다.");
    }

    @Test
    @DisplayName("등록된 테이블이 존재하지 않으면 주문 등록 시 예외가 발생한다.")
    void failToRegisterOrderWithNonRegisteredTable() {
        // given
        Long unsavedTableId = 1000L;

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);

        OrderRequest orderRequest = new OrderRequest(unsavedTableId, List.of(orderLineItemRequest));

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 테이블에는 주문을 등록할 수 없습니다.");
    }

    @Test
    @DisplayName("등록된 테이블이 비어있을 경우 주문 등록 시 예외가 발생한다.")
    void failToRegisterOrderWithNonEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        OrderTable savedEmptyTable = orderTableRepository.save(orderTable);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);

        OrderRequest orderRequest = new OrderRequest(savedEmptyTable.getId(), List.of(orderLineItemRequest));

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈테이블은 주문을 등록할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 내역을 조회하는데 성공한다.")
    void succeedInSearchingOrderList() {
        // given
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);

        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));
        orderService.create(orderRequest);

        // when & then
        assertThat(orderService.list()).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태 변경에 성공한다.")
    void succeedInChangingOrderStatus() {
        // given
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);

        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));
        Order savedOrder = orderService.create(orderRequest);

        // when
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(OrderStatus.MEAL.name());

        Order chagedOrder = orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusRequest);

        // then
        assertThat(chagedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("등록된 주문이 존재하지 않은 상태에서 주문 상태 변경 시 예외가 발생한다.")
    void failToChangeOrderStatusWithNonRegisteredOrder() {
        // given
        Long unsavedOrderId = 1000L;

        // when
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(OrderStatus.MEAL.name());

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(unsavedOrderId, changeOrderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 주문은 주문 상태를 바꿀 수 없습니다.");
    }

    @Test
    @DisplayName("주문 상태가 'COMPLETION'인 경우 주문 상태 변경 시 예외가 발생한다.")
    void failToChangeOrderStatusWithCompletionOrderStatus() {
        // given
        Order order = new Order(savedOrderTable, OrderStatus.COMPLETION.name(), LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // when
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(OrderStatus.MEAL.name());

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료 상태의 주문은 상태 변경이 불가능합니다.");
    }
}
