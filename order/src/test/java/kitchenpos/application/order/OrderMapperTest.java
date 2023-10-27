package kitchenpos.application.order;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderMenuProduct;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private Menu menu;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
        Product product = productRepository.save(상품("치즈 피자", 8900L));
        menuProduct = 메뉴_상품(product, 1L);
        menu = menuRepository.save(메뉴("피자", 8900L, menuGroup.getId(), List.of(menuProduct)));
    }

    @Test
    void Order로_변환한다() {
        // given
        OrderCreateRequest request = new OrderCreateRequest(1L, List.of(
                new OrderLineItemRequest(menu.getId(), 2)
        ));

        // when
        Order order = orderMapper.toOrder(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            
            OrderLineItem orderLineItem = order.getOrderLineItems().getItems().get(0);
            softly.assertThat(orderLineItem.getQuantity()).isEqualTo(2);
            softly.assertThat(orderLineItem.getOrderMenu().getPrice()).isEqualTo(menu.getPrice());
            softly.assertThat(orderLineItem.getOrderMenu().getName()).isEqualTo(menu.getName());

            OrderMenuProduct orderMenuProduct = orderLineItem.getOrderMenu().getOrderMenuProducts().get(0);
            softly.assertThat(orderMenuProduct.getName()).isEqualTo(menuProduct.getName());
            softly.assertThat(orderMenuProduct.getPrice()).isEqualTo(menuProduct.getPrice());
            softly.assertThat(orderMenuProduct.getQuantity()).isEqualTo(menuProduct.getQuantity());
        });
    }
}
