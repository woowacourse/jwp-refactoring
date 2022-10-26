package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("주문 관련 기능에서")
class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderRestController orderRestController;

    @Nested
    @DisplayName("주문을 생성할 때")
    class CreateOrder {

        @Test
        @DisplayName("조리상태로 주문이 생성된다.")
        void create() {
            MenuProduct menuProduct = createMenuProduct(createProduct("강정치킨", 18000));
            Menu menu = createMenu("강정치킨", 18000, createMenuGroup("추천 메뉴").getId(), List.of(menuProduct));

            Order order = new Order();
            order.setOrderTableId(createOrderTable(2, false).getId());
            order.setOrderLineItems(List.of(createOrderLineItem(menu)));
            ResponseEntity<Order> response = orderRestController.create(order);

            assertThat(response.getBody().getId()).isNotNull();
            assertThat(response.getStatusCodeValue()).isNotNull();
            assertThat(response.getBody().getOrderStatus()).isEqualTo("COOKING");
            assertThat(response.getBody().getOrderLineItems()).isNotNull();
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @Test
            @DisplayName("주문항목이 비어있으면 예외가 발생한다.")
            void createOrderLineEmpty() {
                Order order = new Order();
                order.setOrderTableId(createOrderTable(2, false).getId());
                order.setOrderLineItems(Collections.emptyList());

                assertThatThrownBy(() -> orderRestController.create(order))
                        .hasMessage("주문 항목이 비어있습니다.");
            }

            @Test
            @DisplayName("주문항목의 수와 메뉴의 수가 일치하지 않으면 에외가 발생한다.")
            void createInvalidOrderLine() {
                MenuProduct menuProduct = createMenuProduct(createProduct("강정치킨", 18000));
                Menu menu = createMenu("강정치킨", 18000, createMenuGroup("추천 메뉴").getId(), List.of(menuProduct));

                Order order = new Order();
                order.setOrderTableId(createOrderTable(2, false).getId());
                order.setOrderLineItems(List.of(createOrderLineItem(menu), createOrderLineItem(menu)));

                assertThatThrownBy(() -> orderRestController.create(order))
                        .hasMessage("주문항목의 수와 메뉴의 수가 일치하지 않습니다.");
            }

            @Test
            @DisplayName("주문 테이블이 존재하지 않으면 예외가 발생한다.")
            void createNotFoundOrderTable() {
                MenuProduct menuProduct = createMenuProduct(createProduct("강정치킨", 18000));
                Menu menu = createMenu("강정치킨", 18000, createMenuGroup("추천 메뉴").getId(), List.of(menuProduct));

                Order order = new Order();
                order.setOrderTableId(9999L);
                order.setOrderLineItems(List.of(createOrderLineItem(menu)));

                assertThatThrownBy(() -> orderRestController.create(order))
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("빈 테이블이면 예외가 발새한다.")
            void createOrderTableIsEmpty() {
                MenuProduct menuProduct = createMenuProduct(createProduct("강정치킨", 18000));
                Menu menu = createMenu("강정치킨", 18000, createMenuGroup("추천 메뉴").getId(), List.of(menuProduct));

                Order order = new Order();
                order.setOrderTableId(createOrderTable(2, true).getId());
                order.setOrderLineItems(List.of(createOrderLineItem(menu)));

                assertThatThrownBy(() -> orderRestController.create(order))
                        .hasMessage("빈 테이블입니다.");
            }
        }
    }

    @Test
    @DisplayName("존재하는 주문을 모두 조회한다.")
    void list() {
        OrderLineItem orderLineItem = getOrderLineItem();
        OrderTable orderTable = createOrderTable(2, false);

        createOrder(orderTable.getId(), List.of(orderLineItem));
        createOrder(orderTable.getId(), List.of(orderLineItem));
        createOrder(orderTable.getId(), List.of(orderLineItem));

        ResponseEntity<List<Order>> response = orderRestController.list();

        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Nested
    @DisplayName("주문의 상태를 변경할 때")
    class ChangeStatus {

        @Test
        @DisplayName("식사 상태로 변경한다.")
        void changeOrderStatus() {
            Order order = createOrder(createOrderTable(2, false).getId(), List.of(getOrderLineItem()));
            order.setOrderStatus("MEAL");

            ResponseEntity<Order> response = orderRestController.changeOrderStatus(order.getId(), order);

            assertThat(response.getBody().getOrderStatus()).isEqualTo("MEAL");
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @Test
            @DisplayName("주문이 존재하지 않으면 예외가 발생한다.")
            void changeOrderStatusNotFoundOrder() {
                Order order = new Order();
                order.setOrderStatus("COOKING");

                assertThatThrownBy(() -> orderRestController.changeOrderStatus(order.getId(), order))
                        .hasMessage("주문이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("계산 완료 상태인 주문을 변경하면 예외가 발생한다.")
            void changeOrderStatusCompletion() {
                Order order = createOrder(createOrderTable(2, false).getId(), List.of(getOrderLineItem()));
                order.setOrderStatus("COMPLETION");
                orderRestController.changeOrderStatus(order.getId(), order);

                assertThatThrownBy(() -> orderRestController.changeOrderStatus(order.getId(), order))
                        .hasMessage("계산 완료된 주문입니다.");
            }
        }
    }

    private OrderLineItem getOrderLineItem() {
        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        Menu menu = createMenu("강정치킨", 18000, menuGroup.getId(), List.of(menuProduct));

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);
        return orderLineItem;
    }

    private MenuProduct createMenuProduct(Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);
        return menuProduct;
    }

    private OrderLineItem createOrderLineItem(Menu menu) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);
        return orderLineItem;
    }
}
