package kitchenpos.order.repository;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.common.vo.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.repository.ProductRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("주문 레파지토리 테스트")
class OrderLineItemRepositoryTest {

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    private List<Menu> menus;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
        final Product product = productRepository.save(ProductFixture.상품_엔티티_생성());
        menus = menuRepository.saveAll(MenuFixture.메뉴_엔티티들_생성(3, menuGroup, List.of(product)));
        orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_엔티티_생성());
    }

    @Test
    void 주문_아이디를_통해_모든_주문_항목을_조회한다() {
        // given
        final List<OrderLineItem> orderLineItems = OrderLineItemFixture.주문_상품들_생성(menus);
        final Order order = orderRepository.save(Order.of(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));

        // when
        final List<OrderLineItem> actual = orderLineItemRepository.findAllByOrderId(order.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).isEqualTo(orderLineItems.get(0));
            softAssertions.assertThat(actual.get(1)).isEqualTo(orderLineItems.get(1));
            softAssertions.assertThat(actual.get(2)).isEqualTo(orderLineItems.get(2));
        });
    }
}
