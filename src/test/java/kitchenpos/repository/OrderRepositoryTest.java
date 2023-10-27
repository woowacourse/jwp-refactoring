package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.vo.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.support.DataCleaner;
import kitchenpos.support.DataDependentIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderRepositoryTest extends DataDependentIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DataCleaner dataCleaner;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }

    @DisplayName("주어진 주문 상태들, 테이블들 중 하나라도 가지는 주문이 있는지 확인한다.")
    @Test
    void existsByOrderTableInAndOrderStatusIn() {
        // given
        final OrderTable orderTable1 = new OrderTable(3, false);
        final OrderTable orderTable2 = new OrderTable(2, false);
        orderTableRepository.saveAll(List.of(orderTable1, orderTable2));

        final Order order1 = new Order(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(), List.of(new OrderLineItem(createMenuAndGetId(), 1)));
        final Order order2 = new Order(orderTable2.getId(), OrderStatus.COOKING, LocalDateTime.now(), List.of(new OrderLineItem(createMenuAndGetId(), 1)));
        orderRepository.saveAll(List.of(order1, order2));

        // when
        final boolean exists = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable1.getId()), List.of(OrderStatus.COOKING));

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("주어진 주문 상태들 중 하나라도 가지고, 특정 테이블을 가지는 주문이 있는지 확인한다.")
    @Test
    void existsByOrderTableAndOrderStatusIn() {
        // given
        final OrderTable orderTable1 = new OrderTable(3, false);
        orderTableRepository.save(orderTable1);

        final Order order1 = new Order(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(), List.of(new OrderLineItem(createMenuAndGetId(), 1)));
        orderRepository.save(order1);

        // when
        final boolean exists = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(), List.of(OrderStatus.COOKING));

        // then
        assertThat(exists).isTrue();
    }

    private long createMenuAndGetId() {
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("product", Price.from(BigDecimal.valueOf(1000L))));
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(product.getId(), 1));
        final Menu menu = Menu.of("menu", Price.from(BigDecimal.valueOf(1000L)), menuGroup.getId(), menuProducts);
        menuRepository.save(menu);

        return menu.getId();
    }
}
