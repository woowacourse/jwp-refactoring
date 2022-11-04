package kitchenpos.order.application;

import static kitchenpos.common.fixture.MenuFixtures.generateMenu;
import static kitchenpos.common.fixture.MenuGroupFixtures.generateMenuGroup;
import static kitchenpos.common.fixture.MenuProductFixtures.generateMenuProduct;
import static kitchenpos.common.fixture.OrderFixtures.generateOrder;
import static kitchenpos.common.fixture.OrderFixtures.generateOrderChangeOrderStatusRequest;
import static kitchenpos.common.fixture.OrderFixtures.generateOrderSaveRequest;
import static kitchenpos.common.fixture.OrderTableFixtures.generateOrderTable;
import static kitchenpos.common.fixture.ProductFixtures.generateProduct;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.order.dto.OrderChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemSaveRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderSaveRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductDao productDao;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderService orderService;

    @Autowired
    public OrderServiceTest(final MenuGroupRepository menuGroupRepository,
                            final ProductDao productDao,
                            final MenuRepository menuRepository,
                            final OrderRepository orderRepository,
                            final OrderTableRepository orderTableRepository,
                            final OrderService orderService) {
        this.menuGroupRepository = menuGroupRepository;
        this.productDao = productDao;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderService = orderService;
    }

    @Test
    void order를_생성한다() {
        MenuGroup 한마리메뉴 = menuGroupRepository.save(generateMenuGroup("한마리메뉴"));
        Product 후라이드 = productDao.save(generateProduct("후라이드"));

        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));
        Menu menu = menuRepository.save(generateMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(), menuProducts));

        OrderTable orderTable = orderTableRepository.save(generateOrderTable(0, false));
        OrderLineItemSaveRequest orderLineItemSaveRequest = new OrderLineItemSaveRequest(menu.getId(), 1);

        OrderResponse actual = orderService.create(
                generateOrderSaveRequest(orderTable.getId(), List.of(orderLineItemSaveRequest)));

        assertAll(() -> {
            assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(actual.getOrderLineItems()).hasSize(1);
        });
    }

    @Test
    void orderLineItems가_비어있는_경우_예외를_던진다() {
        OrderSaveRequest request = generateOrderSaveRequest(0L, List.of());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderLineItems에_속하는_menuId가_존재하지_않는_경우_예외를_던진다() {
        OrderTable orderTable = orderTableRepository.save(generateOrderTable(0, false));
        OrderLineItemSaveRequest orderLineItemSaveRequest = new OrderLineItemSaveRequest(0L, 1);

        assertThatThrownBy(
                () -> orderService.create(generateOrderSaveRequest(orderTable.getId(),
                        List.of(orderLineItemSaveRequest, orderLineItemSaveRequest))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTable이_비어있는_경우_예외를_던진다() {
        MenuGroup 한마리메뉴 = menuGroupRepository.save(generateMenuGroup("한마리메뉴"));
        Product 후라이드 = productDao.save(generateProduct("후라이드"));

        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));
        Menu menu = menuRepository.save(generateMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(), menuProducts));

        OrderTable orderTable = orderTableRepository.save(generateOrderTable(0, true));
        OrderLineItemSaveRequest orderLineItemSaveRequest = new OrderLineItemSaveRequest(menu.getId(), 1);

        assertThatThrownBy(
                () -> orderService.create(
                        generateOrderSaveRequest(orderTable.getId(), List.of(orderLineItemSaveRequest))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_list를_조회한다() {
        OrderTable orderTable = orderTableRepository.save(generateOrderTable(0, true));
        orderRepository.save(generateOrder(orderTable.getId(), OrderStatus.COOKING, List.of()));
        orderRepository.save(generateOrder(orderTable.getId(), OrderStatus.COOKING, List.of()));

        List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(2);
    }

    @Test
    void order의_상태를_변경한다() {
        OrderTable orderTable = orderTableRepository.save(generateOrderTable(0, true));
        Order order = orderRepository.save(generateOrder(orderTable.getId(), OrderStatus.COOKING, List.of()));
        OrderChangeOrderStatusRequest request = generateOrderChangeOrderStatusRequest(OrderStatus.MEAL);

        OrderResponse actual = orderService.changeOrderStatus(order.getId(), request);

        assertAll(() -> {
            assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        });
    }

    @Test
    void order의_상태가_COMPLETION인_경우_예외를_던진다() {
        OrderTable orderTable = orderTableRepository.save(generateOrderTable(0, true));
        Order order = orderRepository.save(generateOrder(orderTable.getId(), OrderStatus.COMPLETION, List.of()));
        OrderChangeOrderStatusRequest request = generateOrderChangeOrderStatusRequest(OrderStatus.MEAL);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
