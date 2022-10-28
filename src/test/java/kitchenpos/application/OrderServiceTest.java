package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.generateMenu;
import static kitchenpos.fixture.MenuGroupFixture.generateMenuGroup;
import static kitchenpos.fixture.MenuProductFixture.generateMemberProduct;
import static kitchenpos.fixture.OrderFixture.generateOrderCreateRequest;
import static kitchenpos.fixture.OrderLineItemFixture.generateOrderLineItemRequest;
import static kitchenpos.fixture.OrderTableFixture.generateOrderTable;
import static kitchenpos.fixture.ProductFixture.뿌링클;
import static kitchenpos.fixture.ProductFixture.사이다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.FakeMenuDao;
import kitchenpos.dao.FakeMenuGroupDao;
import kitchenpos.dao.FakeMenuProductDao;
import kitchenpos.dao.FakeOrderDao;
import kitchenpos.dao.FakeOrderLineItemDao;
import kitchenpos.dao.FakeOrderTableDao;
import kitchenpos.dao.FakeProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    private OrderService orderService;

    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private OrderTableDao orderTableDao;
    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;

    private Menu 뿌링클_음료두개_세트;

    @BeforeEach
    void beforeEach() {
        this.menuDao = new FakeMenuDao();
        this.orderDao = new FakeOrderDao();
        this.orderLineItemDao = new FakeOrderLineItemDao();
        this.orderTableDao = new FakeOrderTableDao();
        this.menuGroupDao = new FakeMenuGroupDao();
        this.menuProductDao = new FakeMenuProductDao();
        this.productDao = new FakeProductDao();
        this.orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        Product 사이다_1L = productDao.save(사이다);
        Product 뿌링클_2L = productDao.save(뿌링클);

        MenuGroup 세트메뉴 = generateMenuGroup("세트메뉴");
        MenuGroup 세트메뉴_1L = menuGroupDao.save(세트메뉴);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct 사이다_두개 = menuProductDao.save(generateMemberProduct(사이다_1L, 2));
        MenuProduct 뿌링클_한개 = menuProductDao.save(generateMemberProduct(뿌링클_2L, 1));
        menuProducts.add(사이다_두개);
        menuProducts.add(뿌링클_한개);

        뿌링클_음료두개_세트 = menuDao.save(generateMenu("뿌링클 음료두개 세트", BigDecimal.valueOf(21000), 세트메뉴_1L, menuProducts));
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItemRequest orderLineItemRequest = generateOrderLineItemRequest(뿌링클_음료두개_세트.getId(), 1L);
        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(orderLineItemRequest);
        OrderCreateRequest orderCreateRequest = generateOrderCreateRequest(saveOrderTable.getId(),
                orderLineItemRequests);

        // when
        OrderResponse orderResponse = orderService.create(orderCreateRequest);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 생성 시 주문 메뉴 목록이 비어있다면 예외를 반환한다.")
    void create_WhenEmptyOrderLineItems() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();

        OrderCreateRequest orderCreateRequest = generateOrderCreateRequest(saveOrderTable.getId(),
                orderLineItemRequests);

        // when & then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 비어있을 수 없습니다.");
    }

    // TODO: 예외 상황 수정 필요(현재 중복된 주문일 경우 예외 반환하는형태임)
    @Test
    @DisplayName("존재하지 않는 메뉴를 주문한다면 예외를 반환한다.")
    void create_WhenDifferentOrderLineItemsSize() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItemRequest orderLineItemRequest1 = generateOrderLineItemRequest(뿌링클_음료두개_세트.getId(), 1L);
        OrderLineItemRequest orderLineItemRequest2 = generateOrderLineItemRequest(뿌링클_음료두개_세트.getId(), 1L);
        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(orderLineItemRequest1);
        orderLineItemRequests.add(orderLineItemRequest2);
        OrderCreateRequest orderCreateRequest = generateOrderCreateRequest(saveOrderTable.getId(),
                orderLineItemRequests);

        // when & then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItemRequest orderLineItemRequest = generateOrderLineItemRequest(뿌링클_음료두개_세트.getId(), 1L);
        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(orderLineItemRequest);
        OrderCreateRequest orderCreateRequest = generateOrderCreateRequest(saveOrderTable.getId(),
                orderLineItemRequests);

        orderService.create(orderCreateRequest);

        // when
        List<OrderResponse> orderResponses = orderService.list();

        // then
        assertThat(orderResponses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItemRequest orderLineItemRequest = generateOrderLineItemRequest(뿌링클_음료두개_세트.getId(), 1L);
        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(orderLineItemRequest);
        OrderCreateRequest orderCreateRequest = generateOrderCreateRequest(saveOrderTable.getId(),
                orderLineItemRequests);

        OrderResponse orderResponse = orderService.create(orderCreateRequest);

        // when
        OrderResponse response = orderService.changeOrderStatus(orderResponse.getId(), "MEAL");

        // then
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문의 상태를 변경 시 완료된 주문인 경우 예외를 반환한다.")
    void changeOrderStatus_WhenCompletionOrderStatus() {
        // given
        OrderTable emptyOrderTable = generateOrderTable(null, 0, false);
        OrderTable saveOrderTable = orderTableDao.save(emptyOrderTable);

        OrderLineItemRequest orderLineItemRequest = generateOrderLineItemRequest(뿌링클_음료두개_세트.getId(), 1L);
        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(orderLineItemRequest);
        OrderCreateRequest orderCreateRequest = generateOrderCreateRequest(saveOrderTable.getId(),
                orderLineItemRequests);

        OrderResponse orderResponse = orderService.create(orderCreateRequest);
        OrderResponse completion = orderService.changeOrderStatus(orderResponse.getId(), "COMPLETION");
        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(completion.getId(), "MEAL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문은 상태를 변경할 수 없습니다.");
    }
}
