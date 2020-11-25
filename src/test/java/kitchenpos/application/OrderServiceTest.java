package kitchenpos.application;

import static java.util.Collections.*;
import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.inmemory.InmemoryMenuDao;
import kitchenpos.dao.inmemory.InmemoryMenuGroupDao;
import kitchenpos.dao.inmemory.InmemoryMenuProductDao;
import kitchenpos.dao.inmemory.InmemoryOrderDao;
import kitchenpos.dao.inmemory.InmemoryOrderLineItemDao;
import kitchenpos.dao.inmemory.InmemoryOrderTableDao;
import kitchenpos.dao.inmemory.InmemoryProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    private OrderService orderService;

    private OrderTableDao orderTableDao;

    private OrderDao orderDao;

    private ProductDao productDao;

    private MenuDao menuDao;

    private MenuGroupDao menuGroupDao;

    private MenuProductDao menuProductDao;

    @BeforeEach
    void setUp() {
        orderTableDao = new InmemoryOrderTableDao();
        orderDao = new InmemoryOrderDao();
        productDao = new InmemoryProductDao();
        menuDao = new InmemoryMenuDao();
        menuGroupDao = new InmemoryMenuGroupDao();
        menuProductDao = new InmemoryMenuProductDao();
        orderService = new OrderService(menuDao, orderDao, new InmemoryOrderLineItemDao(), orderTableDao);
    }

    @DisplayName("list: 전체 주문 목록을 조회한다.")
    @Test
    void list() {
        OrderTable 점유중인테이블 = orderTableDao.save(createTable(null, 5, false));
        Product 후라이드단품 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup 단품그룹 = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu 치킨단품메뉴 = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), 단품그룹.getId(), emptyList()));
        menuProductDao.save(createMenuProduct(치킨단품메뉴.getId(), 후라이드단품.getId(), 1));

        OrderLineItem 단일주문항목 = createOrderLineItem(null, 치킨단품메뉴.getId(), 1);
        Order 새주문요청 = createOrder(점유중인테이블.getId(), null, OrderStatus.COOKING, Lists.list(단일주문항목));
        orderService.create(새주문요청);

        final List<Order> 전체주문목록 = orderService.list();

        assertThat(전체주문목록).hasSize(1);
    }

    @DisplayName("create: 점유중인 테이블에서 메뉴 중복이 없는 하나 이상의 상품 주문시, 주문 추가 후, 생성된 주문 객체를 반환한다.")
    @Test
    void create() {
        OrderTable 점유중인테이블 = orderTableDao.save(createTable(null, 5, false));
        Product 후라이드단품 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup 단품그룹 = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu 후라이드단품메뉴 = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), 단품그룹.getId(), emptyList()));
        menuProductDao.save(createMenuProduct(후라이드단품메뉴.getId(), 후라이드단품.getId(), 1));

        OrderLineItem 단일주문항목 = createOrderLineItem(null, 후라이드단품메뉴.getId(), 1);
        Order 새주문 = orderService.create(
                createOrder(점유중인테이블.getId(), null, OrderStatus.COOKING, Lists.list(단일주문항목)));

        assertAll(
                () -> assertThat(새주문.getId()).isNotNull(),
                () -> assertThat(새주문.getOrderedTime()).isNotNull(),
                () -> assertThat(새주문.getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(새주문.getOrderTableId()).isEqualTo(점유중인테이블.getId()),
                () -> assertThat(새주문.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("create: 점유중인 테이블에서 주문 상품이 없는 경우, 주문 추가 실패 후, IllegalArgumentException 발생")
    @Test
    void create_fail_if_order_contains_no_order_line_item() {
        OrderTable 점유중인테이블 = orderTableDao.save(createTable(null, 5, false));
        Order 주문상품이없는주문요청 = createOrder(점유중인테이블.getId(), null, OrderStatus.COOKING,
                emptyList());

        assertThatThrownBy(() -> orderService.create(주문상품이없는주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 비어있는 테이블에서 주문 요청시, 주문 추가 실패 후, IllegalArgumentException 발생")
    @Test
    void create_fail_if_table_is_empty() {
        OrderTable 비어있는테이블 = orderTableDao.save(createTable(null, 0, true));
        Product 후라이드단품 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup 단품그룹 = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu 후라이드단품메뉴 = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), 단품그룹.getId(), emptyList()));
        menuProductDao.save(createMenuProduct(후라이드단품메뉴.getId(), 후라이드단품.getId(), 1));

        OrderLineItem 후라이드단품주문항목 = createOrderLineItem(null, 후라이드단품메뉴.getId(), 1);
        Order 빈테이블에대한주문요청 = createOrder(비어있는테이블.getId(), null, OrderStatus.COOKING, Lists.list(후라이드단품주문항목));

        assertThatThrownBy(() -> orderService.create(빈테이블에대한주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 점유중인 테이블에서 중복된 중복 메뉴를 포함한 상품 들 주문 요청시, 주문 추가 실패 후, IllegalArgumentException 발생")
    @Test
    void create_fail_if_order_line_item_contains_duplicate_menu() {
        OrderTable 점유중인테이블 = orderTableDao.save(createTable(null, 5, false));
        Product 후라이드단품 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup 단품그룹 = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu 후라이드단품메뉴 = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), 단품그룹.getId(), emptyList()));
        menuProductDao.save(createMenuProduct(후라이드단품메뉴.getId(), 후라이드단품.getId(), 1));

        OrderLineItem 첫번째주문항목 = createOrderLineItem(null, 후라이드단품메뉴.getId(), 1);
        OrderLineItem 첫번째주문항목과같은품목의주문항목 = createOrderLineItem(null, 후라이드단품메뉴.getId(), 2);
        Order 중복메뉴를포함한주문요청 = createOrder(점유중인테이블.getId(), null, OrderStatus.COOKING,
                Lists.list(첫번째주문항목, 첫번째주문항목과같은품목의주문항목));

        assertThatThrownBy(() -> orderService.create(중복메뉴를포함한주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeOrderStatus: 완료 되지 않는 주문의 경우, 주문 상태의 변경 요청시, 상태 변경 후, 변경된 주문 객체를 반환한다.")
    @Test
    void changeOrderStatus() {
        OrderTable 점유중인테이블 = orderTableDao.save(createTable(null, 5, false));
        Order 완료되지않은주문 = orderDao.save(
                createOrder(점유중인테이블.getId(), LocalDateTime.of(2020, 10, 10, 20, 40), OrderStatus.COOKING, emptyList()));

        Long 완료되지않은주문의식별자 = 완료되지않은주문.getId();
        Order 주문의상태변경요청 = createOrder(null, null, OrderStatus.MEAL, emptyList());
        Order 상태변경완료된주문 = orderService.changeOrderStatus(완료되지않은주문의식별자, 주문의상태변경요청);

        assertAll(
                () -> assertThat(상태변경완료된주문.getId()).isEqualTo(완료되지않은주문의식별자),
                () -> assertThat(상태변경완료된주문.getOrderedTime()).isNotNull(),
                () -> assertThat(상태변경완료된주문.getOrderStatus()).isEqualTo("MEAL"),
                () -> assertThat(상태변경완료된주문.getOrderTableId()).isEqualTo(점유중인테이블.getId())
        );
    }

    @DisplayName("changeOrderStatus: 이미 완료한 주문의 상태의 변경 요청시, 상태 변경 실패 후, IllegalArgumentException 발생.")
    @Test
    void changeOrderStatus_fail_if_order_status_is_completion() {
        OrderTable 점유중인테이블 = orderTableDao.save(createTable(null, 5, false));
        Order 완료상태의주문 = orderDao.save(
                createOrder(점유중인테이블.getId(), LocalDateTime.of(2020, 10, 10, 20, 40), OrderStatus.COMPLETION,
                        emptyList()));
        Long 완료된주문의식별자 = 완료상태의주문.getId();
        Order 주문상태변경요청 = createOrder(null, null, OrderStatus.MEAL, emptyList());

        assertThatThrownBy(() -> orderService.changeOrderStatus(완료된주문의식별자, 주문상태변경요청))
                .isInstanceOf(IllegalArgumentException.class);
    }
}