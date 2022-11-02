package kitchenpos.repository;

import static kitchenpos.support.DataFixture.createMenu;
import static kitchenpos.support.DataFixture.createMenuGroup;
import static kitchenpos.support.DataFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private Menu menu;

    @BeforeEach
    void setUp() {
        final Product 후라이드 = productRepository.save(createProduct("후라이드", 19_000L));
        final MenuGroup 세트메뉴 = menuGroupRepository.save(createMenuGroup("세트메뉴"));
        menu = menuRepository.save(createMenu("후라 2마리 세트", 20_000L, 세트메뉴, List.of(후라이드)));
    }

    @DisplayName("TableId와 OrderStatus로 Order를 찾아온다.")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_true_case() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(4, false));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, false));
        final Order order1 = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now(),
                List.of(new OrderLineItem(menu, 4)));
        final Order order2 = new Order(orderTable2, OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(menu, 4)));
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        final boolean exists = orderRepository
                .existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable1.getId(), orderTable2.getId()),
                        List.of(OrderStatus.COOKING, OrderStatus.MEAL));

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("TableId와 OrderStatus로 Order를 찾아온다(status가 없는 경우)")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_false_case() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(4, false));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, false));
        final Order order1 = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now(),
                List.of(new OrderLineItem(menu, 4)));
        final Order order2 = new Order(orderTable2, OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(menu, 4)));
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        final boolean exists = orderRepository
                .existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable1.getId(), orderTable2.getId()),
                        List.of(OrderStatus.COMPLETION));

        // then
        assertThat(exists).isFalse();
    }

    @DisplayName("TableId와 OrderStatus로 Order를 찾아온다(tableId가 없는 경우)")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_false_case2() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(4, false));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, false));
        final Order order1 = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now(),
                List.of(new OrderLineItem(menu, 4)));
        final Order order2 = new Order(orderTable2, OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(menu, 4)));
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        final boolean exists = orderRepository
                .existsByOrderTableIdInAndOrderStatusIn(List.of(999L),
                        List.of(OrderStatus.COOKING, OrderStatus.MEAL));

        // then
        assertThat(exists).isFalse();
    }
}
