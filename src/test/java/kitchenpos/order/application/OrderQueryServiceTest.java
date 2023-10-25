package kitchenpos.order.application;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.Product;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderQueryServiceTest extends ApplicationTestConfig {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderTableRepository);
    }

    @DisplayName("[SUCCESS] 전체 주문 목록을 조회한다.")
    @Test
    void success_findAll() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        final Menu menu = Menu.withEmptyMenuProducts(
                new Name("테스트용 메뉴명"),
                Price.from("0"),
                savedMenuGroup
        );

        final Product savedProduct = productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("10000")));
        menu.addMenuProducts(List.of(
                MenuProduct.withoutMenu(savedProduct, new Quantity(10)),
                MenuProduct.withoutMenu(savedProduct, new Quantity(10))
        ));
        final Menu savedMenu = menuRepository.save(menu);
        final OrderTable savedOrderTable = orderTableRepository.save(OrderTable.withoutTableGroup(5, false));

        final Order order = Order.ofEmptyOrderLineItems(savedOrderTable);
        order.addOrderLineItems(List.of(
                OrderLineItem.withoutOrder(savedMenu, new Quantity(10))
        ));
        final Order savedOrder = orderRepository.save(order);

        final OrderResponse expected = OrderResponse.from(savedOrder);

        // when
        final List<OrderResponse> actual = orderService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final OrderResponse actualOrder = actual.get(0);

            softly.assertThat(actualOrder.getId()).isEqualTo(expected.getId());
            softly.assertThat(actualOrder.getOrderTable())
                    .usingRecursiveComparison()
                    .isEqualTo(expected.getOrderTable());
            softly.assertThat(actualOrder.getOrderStatus()).isEqualTo(expected.getOrderStatus());
            softly.assertThat(actualOrder.getOrderedTime()).isEqualTo(expected.getOrderedTime());
            softly.assertThat(actualOrder.getOrderLineItems())
                    .usingRecursiveComparison()
                    .isEqualTo(expected.getOrderLineItems());
        });
    }
}
