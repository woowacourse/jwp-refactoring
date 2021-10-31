package kitchenpos.acceptance;

import kitchenpos.domain.*;
import kitchenpos.ui.request.OrderLineItemRequest;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.request.OrderStatusModifyRequest;
import kitchenpos.ui.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends AcceptanceTest {

    OrderTable 비어있지않은_주문_테이블;
    OrderTable 비어있는_주문_테이블;

    Order 주문;

    MenuGroup 한마리메뉴;
    MenuGroup 두마리메뉴;

    Menu 한마리메뉴_중_후라이드치킨;
    Menu 두마리메뉴_중_양념치킨_간장치킨;

    Product 후라이드치킨;
    Product 양념치킨;
    Product 간장치킨;

    MenuProduct 한마리메뉴_후라이드치킨;
    MenuProduct 두마리메뉴_양념치킨;
    MenuProduct 두마리메뉴_간장치킨;

    OrderLineItem 비어있지않은_주문_테이블_한마리메뉴_중_후라이드_치킨;

    @BeforeEach
    void setUp() {
        한마리메뉴 = new MenuGroup.Builder()
                .name("한마리메뉴")
                .build();
        menuGroupRepository.save(한마리메뉴);

        두마리메뉴 = new MenuGroup.Builder()
                .name("두마리메뉴")
                .build();
        menuGroupRepository.save(두마리메뉴);

        후라이드치킨  = new Product.Builder()
                .name("후라이드치킨")
                .price(BigDecimal.valueOf(15000))
                .build();
        productRepository.save(후라이드치킨);

        양념치킨 = new Product.Builder()
                .name("양념치킨")
                .price(BigDecimal.valueOf(16000))
                .build();
        productRepository.save(양념치킨);

        간장치킨 = new Product.Builder()
                .name("간장치킨")
                .price(BigDecimal.valueOf(16000))
                .build();
        productRepository.save(간장치킨);

        한마리메뉴_후라이드치킨 = new MenuProduct.Builder()
                .menu(null)
                .product(후라이드치킨)
                .quantity(1L)
                .build();

        한마리메뉴_중_후라이드치킨 = new Menu.Builder()
                .name("후라이드치킨")
                .price(BigDecimal.valueOf(15000))
                .menuGroup(한마리메뉴)
                .menuProducts(Arrays.asList(한마리메뉴_후라이드치킨))
                .build();
        menuRepository.save(한마리메뉴_중_후라이드치킨);
        menuProductRepository.save(한마리메뉴_후라이드치킨);

        두마리메뉴_양념치킨 = new MenuProduct.Builder()
                .menu(null)
                .product(양념치킨)
                .quantity(1L)
                .build();

        두마리메뉴_간장치킨 = new MenuProduct.Builder()
                .menu(null)
                .product(간장치킨)
                .quantity(1L)
                .build();

        두마리메뉴_중_양념치킨_간장치킨 = new Menu.Builder()
                .name("양념+간장치킨")
                .price(BigDecimal.valueOf(32000))
                .menuGroup(두마리메뉴)
                .menuProducts(Arrays.asList(두마리메뉴_양념치킨, 두마리메뉴_간장치킨))
                .build();
        menuRepository.save(두마리메뉴_중_양념치킨_간장치킨);
        menuProductRepository.save(두마리메뉴_양념치킨);
        menuProductRepository.save(두마리메뉴_간장치킨);

        비어있지않은_주문_테이블 = new OrderTable.Builder()
                .numberOfGuests(4)
                .empty(false)
                .build();
        orderTableRepository.save(비어있지않은_주문_테이블);

        비어있는_주문_테이블 = new OrderTable.Builder()
                .numberOfGuests(0)
                .empty(true)
                .build();
        orderTableRepository.save(비어있는_주문_테이블);

        비어있지않은_주문_테이블_한마리메뉴_중_후라이드_치킨 = new OrderLineItem.Builder()
                .menu(한마리메뉴_중_후라이드치킨)
                .order(주문)
                .quantity(1L)
                .build();

        주문 = new Order.Builder()
                .orderTable(비어있지않은_주문_테이블)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(Arrays.asList(비어있지않은_주문_테이블_한마리메뉴_중_후라이드_치킨))
                .build();
        orderRepository.save(주문);
        orderLineItemRepository.save(비어있지않은_주문_테이블_한마리메뉴_중_후라이드_치킨);
    }

    @DisplayName("매장에서 발생한 주문들에 대한 정보를 반환한다")
    @Test
    void getOrders() {
        // when
        ResponseEntity<OrderResponse[]> responseEntity = testRestTemplate.getForEntity("/api/orders", OrderResponse[].class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(1);
    }

    @DisplayName("매장에서 발생한 주문 정보를 생성한다")
    @Test
    void createOrder() {
        // given
        OrderLineItemRequest 주문_요청_아이템1 = new OrderLineItemRequest();
        주문_요청_아이템1.setMenuId(한마리메뉴_중_후라이드치킨.getId());
        주문_요청_아이템1.setQuantity(1L);

        OrderLineItemRequest 주문_요청_아이템2 = new OrderLineItemRequest();
        주문_요청_아이템2.setMenuId(두마리메뉴_중_양념치킨_간장치킨.getId());
        주문_요청_아이템2.setQuantity(1L);

        OrderRequest 주문_요청 = new OrderRequest();
        주문_요청.setOrderTableId(비어있지않은_주문_테이블.getId());
        주문_요청.setOrderLineItems(Arrays.asList(주문_요청_아이템1, 주문_요청_아이템2));

        // when
        ResponseEntity<OrderResponse> response = testRestTemplate.postForEntity("/api/orders", 주문_요청, OrderResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderResponse 응답된_주문 = response.getBody();
        assertThat(응답된_주문.getOrderTableId()).isEqualTo(비어있지않은_주문_테이블.getId());
        assertThat(응답된_주문.getOrderLineItems()).hasSize(2);
    }

    @DisplayName("매장에서 발생한 주문 정보를 생성시, 오더 테이블이 비어있으면 안된다.")
    @Test
    void cannotCreateOrderWhenOrderTableIsEmpty() {
        // given
        OrderLineItemRequest 주문_요청_아이템 = new OrderLineItemRequest();
        주문_요청_아이템.setMenuId(한마리메뉴_중_후라이드치킨.getId());
        주문_요청_아이템.setQuantity(1L);

        OrderRequest 주문_요청 = new OrderRequest();
        주문_요청.setOrderTableId(비어있는_주문_테이블.getId());
        주문_요청.setOrderLineItems(Arrays.asList(주문_요청_아이템));

        // when
        ResponseEntity<OrderResponse> response = testRestTemplate.postForEntity("/api/orders", 주문_요청, OrderResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("매장에서 발생한 주문 정보를 생성 시, 등록되지 않은 메뉴로는 주문 정보 생성 할 수 없다")
    @Test
    void cannotCreateOrderWithMenuNotRegistered() {
        // given
        Long 등록되지_않은_메뉴ID = 99999999L;

        OrderLineItemRequest 유효하지_않은_주문_아이템 = new OrderLineItemRequest();
        유효하지_않은_주문_아이템.setMenuId(등록되지_않은_메뉴ID);
        유효하지_않은_주문_아이템.setQuantity(1L);

        OrderRequest 유효하지_않은_주문 = new OrderRequest();
        유효하지_않은_주문.setOrderTableId(비어있지않은_주문_테이블.getId());
        유효하지_않은_주문.setOrderLineItems(Arrays.asList(유효하지_않은_주문_아이템));

        // when
        ResponseEntity<OrderResponse> response = testRestTemplate.postForEntity("/api/orders", 유효하지_않은_주문, OrderResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("매장에서 발생한 주문 정보를 생성 시, 중복되는 메뉴로 주문 정보 생성할 수 없다.")
    @Test
    void cannotCreateOrderWithDuplicateMenu() {
        // given
        OrderLineItemRequest 주문_요청_아이템 = new OrderLineItemRequest();
        주문_요청_아이템.setMenuId(한마리메뉴_중_후라이드치킨.getId());
        주문_요청_아이템.setQuantity(1L);

        OrderRequest 유효하지_않은_주문 = new OrderRequest();
        유효하지_않은_주문.setOrderTableId(비어있지않은_주문_테이블.getId());
        유효하지_않은_주문.setOrderLineItems(Arrays.asList(주문_요청_아이템, 주문_요청_아이템));

        // when
        ResponseEntity<OrderResponse> response = testRestTemplate.postForEntity("/api/orders", 유효하지_않은_주문, OrderResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("매장에서 발생한 orderId에 해당하는 주문 정보를 수정한다")
    @Test
    void changeOrderStatus() {
        // given
        OrderStatusModifyRequest 변경할_주문 = new OrderStatusModifyRequest();
        변경할_주문.setOrderStatus(OrderStatus.MEAL.name());
        Long 주문_ID = 주문.getId();

        // when
        testRestTemplate.put("/api/orders/" + 주문_ID + "/order-status", 변경할_주문);

        // then
        Order 변경된_주문 = orderRepository.findById(주문_ID).get();
        assertThat(변경된_주문.getId()).isEqualTo(주문.getId());
        assertThat(변경된_주문.getOrderTableId()).isEqualTo(주문.getOrderTableId());
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("매장에서 발생한 orderId에 해당하는 주문 정보를 수정 시, 해당 주문 정보의 OrderStatus가 이미 COMPLETION이라면 수정할 수 없다.")
    @Test
    void cannotChangeOrderStatusWhenCompletion() {
        // given
        Order 완료된_주문 = new Order.Builder()
                .orderTable(비어있지않은_주문_테이블)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(Arrays.asList(비어있지않은_주문_테이블_한마리메뉴_중_후라이드_치킨))
                .build();
        완료된_주문.changeOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(완료된_주문);

        OrderStatusModifyRequest 변경할_주문 = new OrderStatusModifyRequest();
        변경할_주문.setOrderStatus(OrderStatus.MEAL.name());
        Long 완료된_주문_ID = 완료된_주문.getId();

        // when
        ResponseEntity<Void> response = testRestTemplate.exchange("/api/orders/" + 완료된_주문_ID + "/order-status",
                HttpMethod.PUT, new HttpEntity<>(변경할_주문), Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
