package kitchenpos.application;

import static kitchenpos.domain.fixture.MenuFixture.후라이드_치킨_세트의_가격과_메뉴_상품_리스트는;
import static kitchenpos.domain.fixture.MenuGroupFixture.치킨_세트;
import static kitchenpos.domain.fixture.MenuProductFixture.상품_하나;
import static kitchenpos.domain.fixture.OrderFixture.주문_1번의_주문_항목들은;
import static kitchenpos.domain.fixture.OrderTableFixture.비어있는_테이블;
import static kitchenpos.domain.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.fake.FakeMenuDao;
import kitchenpos.dao.fake.FakeMenuGroupDao;
import kitchenpos.dao.fake.FakeOrderTableDao;
import kitchenpos.dao.fake.FakeProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.fixture.OrderTableFixture;
import kitchenpos.repository.OrderRepository;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
@DisplayName("Order 서비스 테스트")
class OrderServiceTest {

    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTableDao orderTableDao;

    private Menu 저장된_후라이드_치킨_세트_메뉴;
    private OrderTable 저장된_주문_테이블;

    @BeforeEach
    void setUp() {
        final MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
        final ProductDao productDao = new FakeProductDao();
        final MenuDao menuDao = new FakeMenuDao();
        orderTableDao = new FakeOrderTableDao();

        orderService = new OrderService(orderRepository, menuDao, orderTableDao);

        final Product savedProduct = productDao.save(후라이드_치킨());
        final MenuGroup savedMenuGroup = menuGroupDao.save(치킨_세트());

        final MenuProduct menuProduct = 상품_하나(savedProduct.getId());
        final Menu menu = 후라이드_치킨_세트의_가격과_메뉴_상품_리스트는(
            savedMenuGroup.getId(), BigDecimal.valueOf(15_000), List.of(menuProduct)
        );

        저장된_후라이드_치킨_세트_메뉴 = menuDao.save(menu);
        저장된_주문_테이블 = orderTableDao.save(OrderTableFixture.비어있지_않는_테이블());
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(저장된_후라이드_치킨_세트_메뉴.getId(), 1);
        final OrderRequest request = new OrderRequest(저장된_주문_테이블.getId(), List.of(orderLineItemRequest));

        final OrderResponse response = orderService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("주문 등록 시 주문 항목이 메뉴에 존재해야 한다")
    @Test
    void createOrderLineItemIsNotExist() {
        final long notSavedMenuId = 0L;

        final OrderLineItemRequest notSavedOrderLineItemRequest = new OrderLineItemRequest(notSavedMenuId, 1);
        final OrderRequest request = new OrderRequest(저장된_주문_테이블.getId(), List.of(notSavedOrderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("존재하지 않는 메뉴가 포함되어 있습니다.");
    }

    @DisplayName("주문 등록 시 주문 테이블이 존재해야 한다")
    @Test
    void createOrderTableIsNotExist() {
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(저장된_후라이드_치킨_세트_메뉴.getId(), 1);

        long notSavedOrderTableId = 0L;
        final OrderRequest request = new OrderRequest(notSavedOrderTableId, List.of(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("존재하지 않는 테이블입니다.");
    }

    @DisplayName("주문 등록 시 주문 테이블이 비어있으면 안된다")
    @Test
    void createOrderTableIsEmpty() {
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(저장된_후라이드_치킨_세트_메뉴.getId(), 1);
        final OrderTable savedEmptyOrderTable = orderTableDao.save(비어있는_테이블());

        final OrderRequest request = new OrderRequest(savedEmptyOrderTable.getId(), List.of(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("테이블이 비어있으면 안됩니다.");
    }

    @DisplayName("주문의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfOrder = 5;
        for (int i = 0; i < numberOfOrder; i++) {
            final OrderLineItem orderLineItem = new OrderLineItem(1L, 1);
            final Order order = 주문_1번의_주문_항목들은(저장된_주문_테이블.getId(), List.of(orderLineItem));
            orderRepository.save(order);
        }

        final List<OrderResponse> responses = orderService.list();

        assertThat(responses).hasSize(numberOfOrder);
    }

    @DisplayName("주문의 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1);
        final Order order = 주문_1번의_주문_항목들은(저장된_주문_테이블.getId(), List.of(orderLineItem));
        final Order savedOrder = orderRepository.save(order);
        final OrderChangeRequest newOrder = new OrderChangeRequest(OrderStatus.COMPLETION.name());

        final OrderResponse response = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문의 상태 변경 시 주문이 존재해야 한다")
    @Test
    void changeOrderStatusOrderIsNotExist() {
        final long notSavedOrderId = 0L;

        final OrderChangeRequest request = new OrderChangeRequest(OrderStatus.COMPLETION.name());
        assertThatThrownBy(() -> orderService.changeOrderStatus(notSavedOrderId, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("존재하지 않는 주문입니다.");
    }
}
