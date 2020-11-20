package kitchenpos.ui;

import static kitchenpos.utils.TestObjects.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.application.OrderService;
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

@SuppressWarnings("NonAsciiCharacters")
class OrderRestControllerTest extends ControllerTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    OrderDao orderDao;

    @DisplayName("create: 테이블, 주문 라인 목록과 함께 주문 추가 요청을 한다. 새 주문 생성 성공 후 201 응답을 반환한다.")
    @Test
    void create() throws Exception {
        OrderTable 점유중인테이블 = orderTableDao.save(createTable(null, 5, false));
        Product 후라이드단품 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup 단품메뉴그룹 = menuGroupDao.save(createMenuGroup("단품 메뉴"));
        Menu 후라이드한마리세트 = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), 단품메뉴그룹.getId(), null));
        menuProductDao.save(createMenuProduct(후라이드한마리세트.getId(), 후라이드단품.getId(), 1));

        OrderLineItem 주문항목 = createOrderLineItem(null, 후라이드한마리세트.getId(), 1);
        Order 생성하려는주문 = createOrder(점유중인테이블.getId(), null, OrderStatus.COOKING, Lists.list(주문항목));

        final String 주문추가_API_URL = "/api/orders";
        final ResultActions resultActions = create(주문추가_API_URL, 생성하려는주문);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.orderTableId", notNullValue()))
                .andExpect(jsonPath("$.orderStatus", is("COOKING")))
                .andExpect(jsonPath("$.orderedTime", notNullValue()))
                .andExpect(jsonPath("$.orderLineItems", hasSize(1)));
    }

    @DisplayName("list: 전체 주문 목록 조회 요청시, 200 상태 코드와 함께, 전체 주문 내역을 반환한다.")
    @Test
    void list() throws Exception {
        OrderTable 점유중인테이블 = orderTableDao.save(createTable(null, 5, false));
        Product 후라이드단품 = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup 단품메뉴그룹 = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu 후라이드한마리세트 = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), 단품메뉴그룹.getId(), null));
        menuProductDao.save(createMenuProduct(후라이드한마리세트.getId(), 후라이드단품.getId(), 1));

        OrderLineItem 주문항목 = createOrderLineItem(null, 후라이드한마리세트.getId(), 1);
        Order 생성하려는주문 = createOrder(점유중인테이블.getId(), null, OrderStatus.COOKING, Lists.list(주문항목));
        orderService.create(생성하려는주문);

        final String 주문목록조회_API_URL = "/api/orders";
        final ResultActions resultActions = findList(주문목록조회_API_URL);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("changeOrderStatus: 요리 완료 상태가 아닌 경우, 주문 현재 진행 상태 변경 요청시 변경 후, 200 상태코드와, 변경한 주문 내용을 반환한다.")
    @Test
    void changeOrderStatus() throws Exception {
        OrderTable 점유중인테이블 = orderTableDao.save(createTable(null, 5, false));
        Order 완료되지않은주문 = orderDao.save(
                createOrder(점유중인테이블.getId(), LocalDateTime.of(2020, 10, 10, 20, 40), OrderStatus.COOKING, null));

        Order 변경하려는주문내용 = createOrder(null, null, OrderStatus.MEAL, null);
        Long 기존의주문Id = 완료되지않은주문.getId();

        String 주문상태변경_API_URL = "/api/orders/{orderId}/order-status";
        final ResultActions resultActions = updateByPathIdAndBody(주문상태변경_API_URL, 기존의주문Id, 변경하려는주문내용);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.orderTableId", notNullValue()))
                .andExpect(jsonPath("$.orderStatus", is("MEAL")))
                .andExpect(jsonPath("$.orderedTime", notNullValue()));

    }
}