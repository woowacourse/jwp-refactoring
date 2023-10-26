package kitchenpos.application;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Collections;
import kitchenpos.application.order.OrderService;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Nested
    class 주문_등록 {

        @Test
        void 주문을_등록할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(2);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            final var savedMenu = 단일_메뉴_저장(menu);

            final var orderLineItem = OrderLineItemFixture.주문항목_망고치킨_2개(savedMenu);
            final var request = OrderFixture.주문요청_생성(savedOrderTable, orderLineItem);

            // when
            final var actual = orderService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isNotNull();
                softly.assertThat(actual.getOrderTableId()).isEqualTo(savedOrderTable.getId());
                softly.assertThat(actual.getOrderStatus()).isEqualTo(COOKING.name());
                softly.assertThat(actual.getOrderLineItems()).hasSize(1);
                softly.assertThat(actual.getOrderLineItems().get(0).getMenuId()).isEqualTo(savedMenu.getId());
                softly.assertThat(actual.getOrderLineItems().get(0).getQuantity()).isEqualTo(2);
            });
        }

        @Test
        void 주문_항목이_비어있으면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(2);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var request = OrderFixture.주문요청_생성(savedOrderTable);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목_중_실제_메뉴에_존재하지_않는_메뉴가_있으면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(2);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var invalidMenu = MenuFixture.메뉴_존재X(savedMenuGroup, menuProduct1, menuProduct2);

            final var orderLineItem = OrderLineItemFixture.주문항목_망고치킨_2개(invalidMenu);
            final var request = OrderFixture.주문요청_생성(savedOrderTable, orderLineItem);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문의_주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final var invalidOrderTable = OrderTableFixture.주문테이블_INVALID();

            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            final var savedMenu = 단일_메뉴_저장(menu);

            final var orderLineItem = OrderLineItemFixture.주문항목_망고치킨_2개(savedMenu);
            final var request = OrderFixture.주문요청_생성(invalidOrderTable, orderLineItem);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블이면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.빈테이블_1명();
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            final var savedMenu = 단일_메뉴_저장(menu);

            final var orderLineItem = OrderLineItemFixture.주문항목_망고치킨_2개(savedMenu);
            final var request = OrderFixture.주문요청_생성(savedOrderTable, orderLineItem);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_조리로_등록된다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(2);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            final var savedMenu = 단일_메뉴_저장(menu);

            final var orderLineItem = OrderLineItemFixture.주문항목_망고치킨_2개(savedMenu);
            final var request = OrderFixture.주문요청_생성(savedOrderTable, orderLineItem);

            // when
            final var actual = orderService.create(request);

            // then
            assertThat(actual.getOrderStatus())
                    .isEqualTo(COOKING.name());
        }
    }

    @Nested
    class 주문_목록_조회 {

        @Test
        void 주문_목록을_조회할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(2);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            final var savedMenu = 단일_메뉴_저장(menu);

            final var orderLineItem = OrderLineItemFixture.주문항목_망고치킨_2개(savedMenu);
            final var order = 단일_주문_저장(savedOrderTable, orderLineItem);

            // when
            final var actual = orderService.list();

            // then
            final var expected = Collections.singletonList(OrderResponse.toResponse(order));
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_변경할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(2);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            final var savedMenu = 단일_메뉴_저장(menu);

            final var orderLineItem = OrderLineItemFixture.주문항목_망고치킨_2개(savedMenu);
            final var order = 단일_주문_저장(savedOrderTable, orderLineItem);
            final var request = OrderFixture.주문요청_상태변경_생성(MEAL);

            // when
            final var actual = orderService.changeOrderStatus(order.getId(), request);

            // then
            assertThat(actual.getOrderStatus())
                    .isEqualTo(MEAL.name());
        }

        @Test
        void 주문이_존재하지_않으면_예외가_발생한다() {
            // given
            final var request = OrderFixture.주문요청_상태변경_생성(MEAL);

            // when & then
            final var invalidId = 999999L;
            assertThatThrownBy(() -> orderService.changeOrderStatus(invalidId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_계산_완료면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(2);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            final var savedMenu = 단일_메뉴_저장(menu);

            final var orderLineItem = OrderLineItemFixture.주문항목_망고치킨_2개(savedMenu);
            final var order = 단일_주문_저장(savedOrderTable, orderLineItem);
            order.changeOrderStatus(COMPLETION);
            final var request = OrderFixture.주문요청_상태변경_생성(MEAL);

            // when & then
            final var id = order.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(id, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
