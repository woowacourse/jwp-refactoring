package kitchenpos.application;

import static kitchenpos.support.DataFixture.createMenu;
import static kitchenpos.support.DataFixture.createMenuGroup;
import static kitchenpos.support.DataFixture.createOrderTable;
import static kitchenpos.support.DataFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.DatabaseCleanUp;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable savedOrderTable;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
        savedOrderTable = orderTableRepository.save(createOrderTable(1, false));

        final MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("추가메뉴"));
        final Product product = productRepository.save(createProduct("후라이드", 19_000L));
        savedMenu = menuRepository.save(createMenu("후라후라후라", 27_000L, menuGroup, List.of(product)));
    }

    @DisplayName("주문을 저장한다.")
    @Test
    void create() {
        // given
        final OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest(savedMenu.getId(), 2);
        final OrderRequest orderRequest = createOrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));

        // when
        final OrderResponse response = orderService.create(orderRequest);

        // then
        assertAll(
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(response.getId()).isNotNull()
        );
    }

    @DisplayName("주문 저장 시에 주문 항목이 없다면 예외를 반환한다.")
    @Test
    void create_throwException_ifOrderLineItemsEmpty() {
        // given
        final OrderRequest orderRequest = createOrderRequest(savedOrderTable.getId(), List.of());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 없습니다.");
    }

    @DisplayName("없는 메뉴를 주문 시 예외를 반환한다.")
    @Test
    void create_throwException_ifMenuNotExist() {
        // given
        final Long noExistMenuId = 999L;
        final OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest(noExistMenuId, 2);
        final OrderRequest orderRequest = createOrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("없는 메뉴는 주문할 수 없습니다.");
    }

    @DisplayName("없는 테이블에서 주문 시 예외를 반환한다.")
    @Test
    void create_throwException_ifTableNotExist() {
        // given
        final Long noExistTableId = 999L;
        final OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest(savedMenu.getId(), 2);
        final OrderRequest orderRequest = createOrderRequest(noExistTableId, List.of(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("없는 테이블에서는 주문할 수 없습니다.");
    }

    @DisplayName("주문하려는 테이블이 비어있으면 예외를 반환한다.")
    @Test
    void create_throwException_ifTableNotEmpty() {
        // given
        final OrderTable emptyOrderTable = orderTableRepository.save(createOrderTable(0, true));
        final OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest(savedMenu.getId(), 2);
        final OrderRequest orderRequest = createOrderRequest(emptyOrderTable.getId(), List.of(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 중이지 않은 테이블입니다.");
    }

    @DisplayName("전체 주문을 조회한다.")
    @Test
    void findAll() {
        // given
        final OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest(savedMenu.getId(), 2);
        final OrderRequest orderRequest = createOrderRequest(savedOrderTable.getId(), List.of(orderLineItemRequest));
        orderService.create(orderRequest);

        // when, then
        assertThat(orderService.findAll()).hasSize(1);
    }

    private OrderLineItemRequest createOrderLineItemRequest(final Long menuId, final long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    private OrderRequest createOrderRequest(final Long orderTableId,
                                            final List<OrderLineItemRequest> orderLineItemRequests) {
        return new OrderRequest(orderTableId, orderLineItemRequests);
    }
}
