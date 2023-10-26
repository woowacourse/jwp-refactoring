package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.supports.MenuFixture;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.model.OrderLineItem;
import kitchenpos.order.domain.model.OrderStatus;
import kitchenpos.order.supports.OrderFixture;
import kitchenpos.order.supports.OrderLineItemFixture;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.supports.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Nested
    class 생성 {

        @Test
        void 빈_주문_테이블에_주문_생성_불가() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(true).build();

            // when & then
            assertThatThrownBy(() -> OrderFixture.fixture().orderTable(orderTable).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 주문 테이블입니다.");
        }

        @Test
        void 성공() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(false).build();

            // when & then
            assertThatNoException()
                .isThrownBy(() -> OrderFixture.fixture().orderTable(orderTable).build());
        }
    }

    @Nested
    class 주문_항목_추가 {

        @Test
        void 중복된_메뉴가_있으면_예외() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(false).build();
            Order order = OrderFixture.fixture().orderTable(orderTable).build();

            Menu menu = MenuFixture.fixture().id(1L).build();
            List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemFixture.fixture().menu(menu).build(),
                OrderLineItemFixture.fixture().menu(menu).build()
            );

            // when & then
            assertThatThrownBy(() -> order.setUpOrderLineItems(orderLineItems))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 메뉴입니다.");
        }

        @Test
        void 성공() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(false).build();
            Order order = OrderFixture.fixture().orderTable(orderTable).build();

            Menu menu1 = MenuFixture.fixture().id(1L).build();
            Menu menu2 = MenuFixture.fixture().id(2L).build();
            List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemFixture.fixture().menu(menu1).build(),
                OrderLineItemFixture.fixture().menu(menu2).build()
            );

            // when
            order.setUpOrderLineItems(orderLineItems);

            // then
            assertThat(order.getOrderLineItems())
                .usingRecursiveComparison()
                .isEqualTo(orderLineItems);
        }
    }

    @Nested
    class 상태_변경 {

        @Test
        void 이미_계산완료된_주문이면_예외() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(false).build();
            Order order = OrderFixture.fixture().orderTable(orderTable).orderStatus(OrderStatus.COMPLETION).build();

            // when & then
            assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 계산 완료된 주문입니다.");
        }

        @Test
        void 성공() {
            // given
            OrderTable orderTable = OrderTableFixture.fixture().empty(false).build();
            Order order = OrderFixture.fixture().orderTable(orderTable).orderStatus(OrderStatus.COOKING).build();

            // when
            order.changeStatus(OrderStatus.MEAL);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        }
    }

    @Nested
    class 그룹해제_가능_판단 {

        @Test
        void 계산_완료면_참() {
            // given
            Order order = OrderFixture.fixture().orderStatus(OrderStatus.COMPLETION).build();

            // when
            boolean result = order.isAbleToUngroup();

            // then
            assertThat(result).isTrue();
        }

        @ValueSource(strings = {"COOKING", "MEAL"})
        @ParameterizedTest
        void 계산_완료가_아니면_거짓(OrderStatus orderStatus) {
            // given
            Order order = OrderFixture.fixture().orderStatus(orderStatus).build();

            // when
            boolean result = order.isAbleToUngroup();

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class 빈테이블_변경_가능_판단 {

        @Test
        void 계산_완료면_참() {
            // given
            Order order = OrderFixture.fixture().orderStatus(OrderStatus.COMPLETION).build();

            // when
            boolean result = order.isAbleToChangeEmpty();

            // then
            assertThat(result).isTrue();
        }

        @ValueSource(strings = {"COOKING", "MEAL"})
        @ParameterizedTest
        void 계산_완료가_아니면_거짓(OrderStatus orderStatus) {
            // given
            Order order = OrderFixture.fixture().orderStatus(orderStatus).build();

            // when
            boolean result = order.isAbleToChangeEmpty();

            // then
            assertThat(result).isFalse();
        }
    }
}
