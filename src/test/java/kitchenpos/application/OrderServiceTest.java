package kitchenpos.application;

import kitchenpos.application.exception.InvalidOrderStatusToChangeException;
import kitchenpos.application.exception.NotFoundOrDuplicateMenuToOrderExcpetion;
import kitchenpos.common.ServiceTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.exception.InvalidOrderLineItemsToOrder;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.order.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
            orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_엔티티_생성());
            menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
            products = productRepository.saveAll(ProductFixture.상품_엔티티들_생성(2));
            menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
        }

        @Test
        void 주문을_등록한다() {
            // given
            final OrderRequest order = OrderFixture.조리_상태의_주문_요청_dto_생성(orderTable, List.of(menu));

            // when
            final OrderResponse actual = orderService.create(order);

            // then
            assertThat(actual.getId()).isPositive();
        }

        @Test
        void 주문_등록시_저장된_메뉴의_개수가_다르다면_예외를_반환한다() {
            // given
            final Menu unsavedMenu = MenuFixture.메뉴_엔티티_생성(menuGroup, products);
            final List<Menu> includeUnsavedMenu = List.of(menu, unsavedMenu);
            final OrderRequest orderRequest = OrderFixture.조리_상태의_주문_요청_dto_생성(orderTable, includeUnsavedMenu);

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(NotFoundOrDuplicateMenuToOrderExcpetion.class)
                    .hasMessage("존재하지 않는 혹은 중복된 메뉴를 주문 항목으로 설정했습니다.");
        }

        @Test
        void 주문_등록시_저장되지_않은_주문_테이블을_갖는다면_예외를_반환한다() {
            // given
            final OrderRequest orderRequest = OrderFixture.조리_상태의_주문_요청_dto_생성(orderTable, List.of());

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(InvalidOrderLineItemsToOrder.class)
                    .hasMessage("주문 항목이 없습니다.");
        }

        @Test
        void 주문_등록시_존재하지_않은_메뉴로_주문_항목이_있다면_예외룰_반환한다() {
            // given
            final Menu unsavedMenu = MenuFixture.메뉴_엔티티_생성(menuGroup, products);
            final OrderRequest orderRequest = OrderFixture.조리_상태의_주문_요청_dto_생성(orderTable, List.of(unsavedMenu));

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(NotFoundOrDuplicateMenuToOrderExcpetion.class)
                    .hasMessage("존재하지 않는 혹은 중복된 메뉴를 주문 항목으로 설정했습니다.");
        }

        @Test
        void 주문_등록시_중복되는_메뉴로_주문_항목이_있다면_예외룰_반환한다() {
            // given
            final OrderRequest orderRequest = OrderFixture.조리_상태의_주문_요청_dto_생성(orderTable, List.of(menu, menu));

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(NotFoundOrDuplicateMenuToOrderExcpetion.class)
                    .hasMessage("존재하지 않는 혹은 중복된 메뉴를 주문 항목으로 설정했습니다.");
        }
    }

    @Nested
    class 주문_목록_조회 {

        @Test
        void 주문_목록을_조회한다() {
            // given
            final List<OrderTable> orderTable = orderTableRepository.saveAll(OrderTableFixture.주문_테이블_엔티티들_생성(2));
            final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
            final List<Product> products = productRepository.saveAll(ProductFixture.상품_엔티티들_생성(2));
            final Menu menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
            final List<Order> order = orderRepository.saveAll(OrderFixture.조리_상태의_주문_엔티티들_생성(orderTable, menu));

            // when
            final List<OrderResponse> actual = orderService.list();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(2);
                softAssertions.assertThat(actual.get(0).getId()).isEqualTo(order.get(0).getId());
                softAssertions.assertThat(actual.get(1).getId()).isEqualTo(order.get(1).getId());
            });
        }
    }

    @Nested
    class 주문_상태_변경 {

        private OrderTable orderTable;
        private Menu menu;

        @BeforeEach
        void setUp() {
            orderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_엔티티_생성());
            final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
            final List<Product> products = productRepository.saveAll(ProductFixture.상품_엔티티들_생성(2));
            menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
        }

        @Test
        void 주문_상태를_조리에서_식사로_변경한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.조리_상태의_주문_엔티티_생성(orderTable, menu));
            final ChangeOrderStatusRequest changeStatusRequest = new ChangeOrderStatusRequest(OrderStatus.MEAL.name());

            // when
            final OrderResponse actual = orderService.changeOrderStatus(order.getId(), changeStatusRequest);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 주문_상태를_식사에서_계산으로_변경한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.식사_상태의_주문_엔티티_생성(orderTable, menu));
            final ChangeOrderStatusRequest changeStatusRequest = new ChangeOrderStatusRequest(OrderStatus.COMPLETION.name());

            // when
            final OrderResponse actual = orderService.changeOrderStatus(order.getId(), changeStatusRequest);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        }

        @Test
        void 주문_상태를_조리에서_계산으로_변경한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.조리_상태의_주문_엔티티_생성(orderTable, menu));
            final ChangeOrderStatusRequest changeStatusRequest = new ChangeOrderStatusRequest(OrderStatus.COMPLETION.name());

            // when
            final OrderResponse actual = orderService.changeOrderStatus(order.getId(), changeStatusRequest);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        }

        @Test
        void 주문_상태를_계산에서_조리로_변경하면_예외를_반환한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.계산_완료_상태의_주문_생성(orderTable, menu));
            final ChangeOrderStatusRequest changeStatusRequest = new ChangeOrderStatusRequest(OrderStatus.COOKING.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeStatusRequest))
                    .isInstanceOf(InvalidOrderStatusToChangeException.class)
                    .hasMessage("주문이 상태가 계산 완료라면 상태를 변경할 수 없다.");
        }

        @Test
        void 주문_상태를_계산에서_식사로_변경하면_예외를_반환한다() {
            // given
            final Order order = orderRepository.save(OrderFixture.계산_완료_상태의_주문_생성(orderTable, menu));
            final ChangeOrderStatusRequest changeStatusRequest = new ChangeOrderStatusRequest(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeStatusRequest))
                    .isInstanceOf(InvalidOrderStatusToChangeException.class)
                    .hasMessage("주문이 상태가 계산 완료라면 상태를 변경할 수 없다.");
        }
    }
}
