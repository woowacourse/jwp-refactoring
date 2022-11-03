package kitchenpos.application;

import static kitchenpos.fixture.MenuBuilder.aMenu;
import static kitchenpos.fixture.ProductBuilder.aProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService sut;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("주문 항목이 없으면 주문을 생성할 수 없다")
    void throwExceptionWhenNoOrderLineItems() {
        // given
        var orderTable = orderTableRepository.save(new OrderTable(1, false));

        var request = new OrderRequest(orderTable.getId(), Collections.emptyList());

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 존재하지 않습니다");
    }

    @Test
    @DisplayName("주문 항목에 포함된 메뉴는 중복될 수 없다")
    void throwExceptionWhenDuplicateMenu() {
        // given
        var orderTable = orderTableRepository.save(new OrderTable(1, false));
        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();

        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product.getId(), 1L, product.getPrice())))
                .build());

        var orderLineItemRequest1 = new OrderLineItemRequest(menu.getId(), 1L);
        var orderLineItemRequest2 = new OrderLineItemRequest(menu.getId(), 2L);

        var request = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest1, orderLineItemRequest2));

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목의 메뉴가 중복되어 있습니다");
    }

    @Test
    @DisplayName("주문 항목에 포함된 메뉴가 존재하지 않을 경우 예외가 발생한다")
    void throwExceptionWhenNonExistMenu() {
        // given
        var orderTable = orderTableRepository.save(new OrderTable(1, false));
        var orderLineItemRequest = new OrderLineItemRequest(0L, 1L);
        var request = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목의 메뉴가 존재하지 않습니다");
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 주문을 생성할 수 없다")
    void throwExceptionWhenOrderTableDoesNotExist() {
        // given
        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();

        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product.getId(), 1L, product.getPrice())))
                .build());

        var orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
        var request = new OrderRequest(0L, List.of(orderLineItemRequest));

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 존재하지 않습니다");
    }

    @Test
    @DisplayName("주문 테이블이 비어있으면 주문을 생성할 수 없다.")
    void throwExceptionWhenOrderTableIsEmpty() {
        // given
        var orderTable = orderTableRepository.save(new OrderTable(0, true));
        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();
        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product.getId(), 1L, product.getPrice())))
                .build());

        var orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
        var request = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 비어있습니다");
    }

    @Test
    @DisplayName("주문을 생성한다")
    void createOrder() {
        // given
        var orderTable = orderTableRepository.save(new OrderTable(1, false));
        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();
        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product.getId(), 1L, product.getPrice())))
                .build());

        var orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
        var request = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));

        // when
        var response = sut.create(request);

        // then
        assertThat(response.getOrderTableId()).isEqualTo(orderTable.getId());
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(response.getOrderedTime()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(response.getOrderLineItems()).hasSize(1);
        assertThatOrderIdIsSet(response.getOrderLineItems(), response.getId());
    }

    @Test
    @DisplayName("주문 목록을 조회한다")
    void listOrders() {
        var orderTable = orderTableRepository.save(new OrderTable(1, false));
        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();
        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product.getId(), 1L, product.getPrice())))
                .build());
        var savedOrder = orderRepository.save(new Order(orderTable, List.of(new OrderLineItem(menu.getId(), 1L))));

        var response = sut.list();

        assertThat(response).hasSize(1);
        assertThat(response.get(0)).usingRecursiveComparison().isEqualTo(OrderResponse.from(savedOrder));
    }

    @Test
    @DisplayName("입력받은 Id에 해당하는 주문이 존재하지 않으면 주문 상태를 변경할 수 없다")
    void throwExceptionWhenOrderDoesNotExist() {
        // given
        var request = new OrderStatusRequest("MEAL");

        // when && then
        assertThatThrownBy(() -> sut.changeOrderStatus(0L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 존재하지 않습니다");
    }

    @Test
    @DisplayName("주문 상태가 이미 완료 상태인 주문은 상태를 변경할 수 없다")
    void throwExceptionWhenTryToChangeCompletedOrder() {
        // given
        var orderTable = orderTableRepository.save(new OrderTable(1, false));
        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();
        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product.getId(), 1L, product.getPrice())))
                .build());
        var order = orderRepository.save(new Order(orderTable, List.of(new OrderLineItem(menu.getId(), 1L))));
        order.changeStatus(OrderStatus.COMPLETION);
        entityManager.flush();

        var request = new OrderStatusRequest("MEAL");

        // when && then
        assertThatThrownBy(() -> sut.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다");
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        // given
        var orderTable = orderTableRepository.save(new OrderTable(1, false));
        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();
        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product.getId(), 1L, product.getPrice())))
                .build());
        var order = orderRepository.save(new Order(orderTable, List.of(new OrderLineItem(menu.getId(), 1L))));

        var request = new OrderStatusRequest("MEAL");

        // when
        var response = sut.changeOrderStatus(order.getId(), request);

        // then
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    private void assertThatOrderIdIsSet(List<OrderLineItemResponse> orderLineItems, Long orderId) {
        for (var orderLineItem : orderLineItems) {
            assertThat(orderLineItem.getOrderId()).isEqualTo(orderId);
        }
    }
}
