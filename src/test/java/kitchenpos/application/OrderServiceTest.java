package kitchenpos.application;

import kitchenpos.common.ServiceTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("주문 서비스 테스트")
class OrderServiceTest extends ServiceTestConfig {

    @Autowired
    OrderService orderService;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderLineItemRepository orderLineItemRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Nested
    class 주문_등록 {

        private OrderTable orderTable;
        private MenuGroup menuGroup;
        private List<Product> products;
        private Menu menu;

        @BeforeEach
        void setUp() {
            orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_생성());
            menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
            products = productRepository.saveAll(ProductFixture.상품들_생성(2));
            menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
        }

        @Test
        void 주문을_등록한다() {
            // given
            final Order order = OrderFixture.조리_상태의_주문_생성(orderTable, menu);

            // when
            final Order actual = orderService.create(order);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getId()).isPositive();
                softAssertions.assertThat(actual).usingRecursiveComparison()
                              .ignoringFields("id")
                              .isEqualTo(order);
            });
        }

        @Test
        void 주문_등록시_저장된_메뉴의_개수가_다르다면_예외를_반환한다() {
            // given
            final Order order = OrderFixture.조리_상태의_주문_생성(orderTable, menu);
            final OrderLineItem usavedOrderLineItem =
                    OrderLineItemFixture.주문_상품_생성(order, MenuFixture.메뉴_엔티티_생성(menuGroup, products));
            order.updateOrderLineItems(List.of(usavedOrderLineItem));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_등록시_저장되지_않은_주문_테이블을_갖는다면_예외를_반환한다() {
            // given
            final Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
            order.updateOrderLineItems(List.of());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_목록_조회 {

        @Test
        void 주문_목록을_조회한다() {
            // given
            final List<OrderTable> orderTable = orderTableRepository.saveAll(OrderTableFixture.주문_테이블들_생성(2));
            final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
            final List<Product> products = productRepository.saveAll(ProductFixture.상품들_생성(2));
            final Menu menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
            final List<Order> order = orderRepository.saveAll(OrderFixture.조리_상태의_주문들_생성(orderTable, menu));

            // when
            final List<Order> actual = orderService.list();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(2);
                softAssertions.assertThat(actual.get(0).getId())
                              .isEqualTo(order.get(0).getId());
                softAssertions.assertThat(actual.get(1).getId())
                              .isEqualTo(order.get(1).getId());
            });
        }
    }

    @Nested
    class 주문_상태_변경 {

        private OrderTable orderTable;
        private Menu menu;

        @BeforeEach
        void setUp() {
            orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_생성());
            final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
            final List<Product> products = productRepository.saveAll(ProductFixture.상품들_생성(2));
            menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
        }

        @Test
        void 주문_상태를_요리에서_식사로_변경한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.조리_상태의_주문_생성(orderTable, menu));

            // when
            final Order actual = orderService.changeOrderStatus(order.getId(), OrderFixture.식사_상태의_주문_생성(orderTable, menu));

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 주문_상태를_식사에서_계산으로_변경한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.식사_상태의_주문_생성(orderTable, menu));

            // when
            final Order actual = orderService.changeOrderStatus(
                    order.getId(),
                    OrderFixture.계산_완료_상태의_주문_생성(orderTable, menu)
            );

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        }

        @Test
        void 주문_상태를_조리에서_계산으로_변경한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.조리_상태의_주문_생성(orderTable, menu));

            // when
            final Order actual = orderService.changeOrderStatus(
                    order.getId(),
                    OrderFixture.계산_완료_상태의_주문_생성(orderTable, menu)
            );

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        }

        @Test
        void 주문_상태를_계산에서_주문으로_변경하면_예외를_반환한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.계산_완료_상태의_주문_생성(orderTable, menu));

            // when & then
            assertThatThrownBy(() ->
                    orderService.changeOrderStatus(order.getId(), OrderFixture.조리_상태의_주문_생성(orderTable, menu))
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태를_계산에서_요리로_변경하면_예외를_반환한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.계산_완료_상태의_주문_생성(orderTable, menu));

            // when & then
            assertThatThrownBy(() ->
                    orderService.changeOrderStatus(order.getId(), OrderFixture.식사_상태의_주문_생성(orderTable, menu))
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
