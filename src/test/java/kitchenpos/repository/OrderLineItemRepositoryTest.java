package kitchenpos.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    private Order order;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
        final Product product = productRepository.save(ProductFixture.상품_생성());
        menus = menuRepository.saveAll(MenuFixture.메뉴들_생성(3, menuGroup, List.of(product)));
        final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_생성());
        order = orderRepository.save(OrderFixture.요리_상태의_주문_생성(orderTable, menus.get(0)));
    }

    @Test
    void 주문_아이디를_통해_모든_주문_항목을_조회한다() {
        // given
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.saveAll(OrderLineItemFixture.주문_상품들_생성(order, menus));

        // when
        final List<OrderLineItem> actual = orderLineItemRepository.findAllByOrderId(order.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison()
                          .isEqualTo(orderLineItems.get(0));
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison()
                          .isEqualTo(orderLineItems.get(1));
            softAssertions.assertThat(actual.get(2)).usingRecursiveComparison()
                          .isEqualTo(orderLineItems.get(2));
        });
    }
}
