package kitchenpos.acceptance;


import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends AcceptanceTest {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    ProductDao productDao;

    TableGroup 테이블_그룹_1 = new TableGroup();

    OrderTable 주문_테이블_1 = new OrderTable();
    OrderTable 주문_테이블_2 = new OrderTable();

    Order 주문_1 = new Order();
    Order 주문_2 = new Order();

    MenuGroup 한마리메뉴 = new MenuGroup();
    MenuGroup 두마리메뉴 = new MenuGroup();

    Menu 한마리메뉴_후라이드치킨 = new Menu();
    Menu 두마리메뉴_양념_간장치킨 = new Menu();

    Product 후라이드치킨 = new Product();
    Product 양념치킨 = new Product();
    Product 간장치킨 = new Product();

    @BeforeEach
    void setUp() {
        테이블_그룹_1.setCreatedDate(LocalDateTime.now());
        테이블_그룹_1 = tableGroupDao.save(테이블_그룹_1);

        주문_테이블_1.setTableGroupId(테이블_그룹_1.getId());
        주문_테이블_1.setNumberOfGuests(4);
        주문_테이블_1.setEmpty(false);
        주문_테이블_1 = orderTableDao.save(주문_테이블_1);

        주문_테이블_2.setTableGroupId(테이블_그룹_1.getId());
        주문_테이블_2.setNumberOfGuests(2);
        주문_테이블_2.setEmpty(true);
        주문_테이블_2 = orderTableDao.save(주문_테이블_2);

        주문_1.setOrderTableId(주문_테이블_1.getId());
        주문_1.setOrderStatus(OrderStatus.COOKING.name());
        주문_1.setOrderedTime(LocalDateTime.now());
        주문_1 = orderDao.save(주문_1);

        주문_2.setOrderTableId(주문_테이블_1.getId());
        주문_2.setOrderStatus(OrderStatus.COOKING.name());
        주문_2.setOrderedTime(LocalDateTime.now());
        주문_2 = orderDao.save(주문_2);

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
        assertThat(responseEntity.getBody()).hasSize(2);
    }

    @DisplayName("매장에서 발생한 주문 정보를 생성한다")
    @Test
    void createOrder() {
        // given
        Order 주문_3 = new Order();
        주문_3.setOrderTableId(주문_테이블_1.getId());

        OrderLineItem 주문_3_아이템_1 = new OrderLineItem();
        주문_3_아이템_1.setMenuId(한마리메뉴_후라이드치킨.getId());
        주문_3_아이템_1.setQuantity(1L);
        OrderLineItem 주문_3_아이템_2 = new OrderLineItem();
        주문_3_아이템_2.setMenuId(두마리메뉴_양념_간장치킨.getId());
        주문_3_아이템_2.setQuantity(1L);
        주문_3.setOrderLineItems(Arrays.asList(주문_3_아이템_1, 주문_3_아이템_2));

        // when
        ResponseEntity<Order> response = testRestTemplate.postForEntity("/api/orders", 주문_3, Order.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Order 응답된_Order = response.getBody();
        assertThat(응답된_Order.getOrderTableId()).isEqualTo(주문_테이블_1.getId());
        assertThat(응답된_Order.getOrderLineItems()).hasSize(2);
    }

    @DisplayName("매장에서 발생한 orderId에 해당하는 주문 정보를 수정한다\n")
    @Test
    void changeOrderStatus() {
        // given
        Order 변경할_주문 = new Order();
        변경할_주문.setOrderStatus(OrderStatus.MEAL.name());
        Long 주문_2_ID = 주문_2.getId();

        // when
        testRestTemplate.put("/api/orders/" + 주문_2_ID + "/order-status", 변경할_주문);

        // then
        Order 변경된_주문_2 = orderDao.findById(주문_2_ID).get();
        assertThat(변경된_주문_2.getId()).isEqualTo(주문_2.getId());
        assertThat(변경된_주문_2.getOrderTableId()).isEqualTo(주문_2.getOrderTableId());
        assertThat(변경된_주문_2.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
