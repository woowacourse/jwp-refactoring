package kitchenpos.application.order;

import kitchenpos.application.OrderService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderQueryServiceTest extends ApplicationTestConfig {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @DisplayName("[SUCCESS] 전체 주문 목록을 조회한다.")
    @Test
    void success_findAll() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        final Menu menu = Menu.ofEmptyMenuProducts(
                new Name("테스트용 메뉴명"),
                new Price("0"),
                savedMenuGroup
        );

        final Product savedProduct = productRepository.save(new Product(new Name("테스트용 상품명"), new Price("10000")));
        menu.addMenuProducts(List.of(
                MenuProduct.ofWithoutMenu(savedProduct, new Quantity(10)),
                MenuProduct.ofWithoutMenu(savedProduct, new Quantity(10))
        ));
        final Menu savedMenu = menuRepository.save(menu);
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(null, 5, false));

        final Order order = new Order(
                savedOrderTable,
                OrderStatus.COOKING,
                LocalDateTime.now(),
                new OrderLineItems(Collections.emptyList())
        );
        final List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(null, savedMenu, new Quantity(10)));
        order.addOrderLineItems(orderLineItems);

        final Order expected = orderService.create(order);

        // when
        final List<Order> actual = orderService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final Order actualOrder = actual.get(0);

            softly.assertThat(actualOrder.getId()).isEqualTo(expected.getId());
            softly.assertThat(actualOrder.getOrderTable()).isEqualTo(expected.getOrderTable());
            softly.assertThat(actualOrder.getOrderStatus()).isEqualTo(expected.getOrderStatus());
            softly.assertThat(actualOrder.getOrderedTime()).isEqualTo(expected.getOrderedTime());
            softly.assertThat(actualOrder.getOrderLineItems())
                    .usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(expected.getOrderLineItems());
        });
    }
}