package kitchenpos.application;

import static kitchenpos.helper.MenuGroupHelper.*;
import static kitchenpos.helper.MenuHelper.*;
import static kitchenpos.helper.OrderHelper.*;
import static kitchenpos.helper.OrderTableHelper.*;
import static kitchenpos.helper.ProductHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@SpringBootTest
@Sql("/truncate.sql")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuProductDao menuProductDao;

    private Menu 메뉴;

    private static Stream<Arguments> provideOrderStatus() {
        return Stream.of(
                Arguments.of(OrderStatus.COOKING),
                Arguments.of(OrderStatus.MEAL)
        );
    }

    @BeforeEach
    void setUp() {
        MenuGroup 메뉴_그룹 = menuGroupDao.save(createMenuGroup("메뉴그룹"));
        Product 상품 = productDao.save(createProduct("프라이드", BigDecimal.valueOf(15_000L)));
        메뉴 = menuDao.save(createMenu("메뉴", BigDecimal.valueOf(30_000L), 메뉴_그룹.getId(), Collections.emptyList()));
        menuProductDao.save(createMenuProduct(1L, 메뉴.getId(), 상품.getId(), 2L));
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 0, false));
        OrderLineItem 주문할_메뉴 = createOrderLineItem(null, 메뉴.getId(), 1);
        Order 생성할_주문 = createOrder(주문_테이블.getId(), null, null, Collections.singletonList(주문할_메뉴));

        // when
        Order 주문 = orderService.create(생성할_주문);

        // then
        assertAll(
                () -> assertThat(주문.getId()).isNotNull(),
                () -> assertThat(주문.getOrderedTime()).isEqualTo(생성할_주문.getOrderedTime()),
                () -> assertThat(주문.getOrderStatus()).isEqualTo(생성할_주문.getOrderStatus()),
                () -> assertThat(주문.getOrderTableId()).isEqualTo(생성할_주문.getOrderTableId()),
                () -> assertThat(주문.getOrderLineItems().get(0).getSeq()).isNotNull(),
                () -> assertThat(주문.getOrderLineItems().get(0).getMenuId())
                        .isEqualTo(생성할_주문.getOrderLineItems().get(0).getMenuId()),
                () -> assertThat(주문.getOrderLineItems().get(0).getOrderId())
                        .isEqualTo(생성할_주문.getOrderLineItems().get(0).getOrderId()),
                () -> assertThat(주문.getOrderLineItems().get(0).getQuantity())
                        .isEqualTo(생성할_주문.getOrderLineItems().get(0).getQuantity())
        );
    }

    @DisplayName("주문시 하나 미만의 메뉴를 포함하면 예외가 발생한다.")
    @Test
    void create_MenuUnderOne_ExceptionThrown() {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 0, false));
        Order 생성할_주문 = createOrder(주문_테이블.getId(), null, null, Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(생성할_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문시 등록되지 않는 메뉴를 포함하면 예외가 발생한다.")
    @Test
    void create_MenuIsNotExist_ExceptionThrown() {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 0, false));
        OrderLineItem 주문할_메뉴 = createOrderLineItem(null, 100L, 1);
        Order 생성할_주문 = createOrder(주문_테이블.getId(), null, null, Collections.singletonList(주문할_메뉴));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(생성할_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문시 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void create_TableIsEmpty_ExceptionThrown() {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 0, true));
        OrderLineItem 주문할_메뉴 = createOrderLineItem(null, 메뉴.getId(), 1);
        Order 생성할_주문 = createOrder(주문_테이블.getId(), null, null, Collections.singletonList(주문할_메뉴));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(생성할_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 0, false));
        OrderLineItem 주문할_메뉴 = createOrderLineItem(null, 메뉴.getId(), 1);
        Order 주문_1 = orderService.create(createOrder(주문_테이블.getId(), null, null, Collections.singletonList(주문할_메뉴)));
        Order 주문_2 = orderService.create(createOrder(주문_테이블.getId(), null, null, Collections.singletonList(주문할_메뉴)));

        // when
        List<Order> 주문목록 = orderService.list();

        // then
        assertAll(
                () -> assertThat(주문목록).hasSize(2),
                () -> assertThat(주문목록.get(0).getId()).isEqualTo(주문_1.getId()),
                () -> assertThat(주문목록.get(1).getId()).isEqualTo(주문_2.getId())
        );
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @MethodSource("provideOrderStatus")
    @ParameterizedTest
    void changeOrderStatus(OrderStatus orderStatus) {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 0, false));
        OrderLineItem 주문할_메뉴 = createOrderLineItem(null, 메뉴.getId(), 1);
        Order 주문 = orderService.create(createOrder(주문_테이블.getId(), null, null, Collections.singletonList(주문할_메뉴)));
        Order 변경할_주문_상태 = createOrder(주문_테이블.getId(), orderStatus.name(), null, Collections.emptyList());

        // when
        Order 변경된_주문_상태 = orderService.changeOrderStatus(주문.getId(), 변경할_주문_상태);

        // then
        assertThat(변경된_주문_상태.getOrderStatus()).isEqualTo(변경할_주문_상태.getOrderStatus());
    }

    @DisplayName("주문 상태를 변경할 때 이미 계산 완료된 주문인 경우 상태를 변경할 수 없다.")
    @MethodSource("provideOrderStatus")
    @ParameterizedTest
    void changeOrderStatus_OrderStatusAlreadyComplete_ExceptionThrown(OrderStatus orderStatus) {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 0, false));
        OrderLineItem 주문할_메뉴 = createOrderLineItem(null, 메뉴.getId(), 1);
        Order 주문 = orderService.create(
                createOrder(주문_테이블.getId(), OrderStatus.COMPLETION.name(), null, Collections.singletonList(주문할_메뉴)));
        Order 완료된_주문_상태 = createOrder(null, OrderStatus.COMPLETION.name(), null, Collections.emptyList());
        orderService.changeOrderStatus(주문.getId(), 완료된_주문_상태);
        Order 변경할_주문_상태 = createOrder(null, orderStatus.name(), null, Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 변경할_주문_상태))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
