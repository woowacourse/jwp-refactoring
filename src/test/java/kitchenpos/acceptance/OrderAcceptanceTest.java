package kitchenpos.acceptance;


import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends AcceptanceTest {

    OrderTable 주문_테이블 = new OrderTable();

    Order 주문 = new Order();

    MenuGroup 한마리메뉴 = new MenuGroup();
    MenuGroup 두마리메뉴 = new MenuGroup();

    Menu 한마리메뉴_후라이드치킨 = new Menu();
    Menu 두마리메뉴_양념_간장치킨 = new Menu();

    Product 후라이드치킨 = new Product();
    Product 양념치킨 = new Product();
    Product 간장치킨 = new Product();

    @BeforeEach
    void setUp() {
        주문_테이블.setNumberOfGuests(4);
        주문_테이블.setEmpty(false);
        주문_테이블 = orderTableDao.save(주문_테이블);

        주문.setOrderTableId(주문_테이블.getId());
        주문.setOrderStatus(OrderStatus.COOKING.name());
        주문.setOrderedTime(LocalDateTime.now());
        주문 = orderDao.save(주문);

        한마리메뉴.setName("한마리메뉴");
        한마리메뉴 = menuGroupDao.save(한마리메뉴);

        두마리메뉴.setName("두마리메뉴");
        두마리메뉴 = menuGroupDao.save(두마리메뉴);

        후라이드치킨.setName("후라이드치킨");
        후라이드치킨.setPrice(BigDecimal.valueOf(15000));
        후라이드치킨 = productDao.save(후라이드치킨);

        양념치킨.setName("양념치킨");
        양념치킨.setPrice(BigDecimal.valueOf(16000));
        양념치킨 = productDao.save(양념치킨);

        간장치킨.setName("간장치킨");
        간장치킨.setPrice(BigDecimal.valueOf(16000));
        간장치킨 = productDao.save(간장치킨);

        한마리메뉴_후라이드치킨.setName("후라이드치킨");
        한마리메뉴_후라이드치킨.setPrice(BigDecimal.valueOf(15000));
        한마리메뉴_후라이드치킨.setMenuGroupId(한마리메뉴.getId());
        한마리메뉴_후라이드치킨 = menuDao.save(한마리메뉴_후라이드치킨);

        두마리메뉴_양념_간장치킨.setName("양념+간장치킨");
        두마리메뉴_양념_간장치킨.setPrice(BigDecimal.valueOf(32000));
        두마리메뉴_양념_간장치킨.setMenuGroupId(두마리메뉴.getId());
        두마리메뉴_양념_간장치킨 = menuDao.save(두마리메뉴_양념_간장치킨);
    }

    @DisplayName("매장에서 발생한 주문들에 대한 정보를 반환한다")
    @Test
    void getOrders() {
        // when
        ResponseEntity<Order[]> responseEntity = testRestTemplate.getForEntity("/api/orders", Order[].class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(1);
    }

    @DisplayName("매장에서 발생한 주문 정보를 생성한다")
    @Test
    void createOrder() {
        // given
        Order 주문3 = new Order();
        주문3.setOrderTableId(주문_테이블.getId());

        OrderLineItem 주문3_아이템1 = new OrderLineItem();
        주문3_아이템1.setMenuId(한마리메뉴_후라이드치킨.getId());
        주문3_아이템1.setQuantity(1L);

        OrderLineItem 주문3_아이템2 = new OrderLineItem();
        주문3_아이템2.setMenuId(두마리메뉴_양념_간장치킨.getId());
        주문3_아이템2.setQuantity(1L);
        주문3.setOrderLineItems(Arrays.asList(주문3_아이템1, 주문3_아이템2));

        // when
        ResponseEntity<Order> response = testRestTemplate.postForEntity("/api/orders", 주문3, Order.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Order 응답된_주문 = response.getBody();
        assertThat(응답된_주문.getOrderTableId()).isEqualTo(주문_테이블.getId());
        assertThat(응답된_주문.getOrderLineItems()).hasSize(2);
    }

    @DisplayName("매장에서 발생한 orderId에 해당하는 주문 정보를 수정한다")
    @Test
    void changeOrderStatus() {
        // given
        Order 변경할_주문 = new Order();
        변경할_주문.setOrderStatus(OrderStatus.MEAL.name());
        Long 주문_ID = 주문.getId();

        // when
        testRestTemplate.put("/api/orders/" + 주문_ID + "/order-status", 변경할_주문);

        // then
        Order 변경된_주문 = orderDao.findById(주문_ID).get();
        assertThat(변경된_주문.getId()).isEqualTo(주문.getId());
        assertThat(변경된_주문.getOrderTableId()).isEqualTo(주문.getOrderTableId());
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
