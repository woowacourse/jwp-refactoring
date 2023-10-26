package kitchenpos.domain;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.OrderLineItemFixture.주문상품;
import static kitchenpos.fixture.ProductFixture.양념치킨_16000;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

class OrderTest {

    @Nested
    class changeOrderStatus {

        @ParameterizedTest
        @EnumSource(mode = Mode.INCLUDE, names = {"COOKING", "MEAL"})
        void 주문상태를_변경할_수_있다(OrderStatus orderStatus) {
            // given
            final var 두마리메뉴 = 메뉴그룹_두마리메뉴;

            final var 후라이드 = 후라이드_16000;
            final var 양념치킨 = 양념치킨_16000;

            final var 후라이드_1개 = 메뉴상품(후라이드, 1);
            final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

            final var 메뉴1 = 메뉴("후라이드양념", 25000, 두마리메뉴, List.of(후라이드_1개, 양념치킨_1개));
            final var 메뉴2 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(후라이드_1개));

            final var tableId = 1L;
            final var orderItems = List.of(주문상품(메뉴1, 2), 주문상품(메뉴2, 3));
            final var order = new Order(tableId, orderItems);

            // when
            order.changeOrderStatus(orderStatus);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
        }

        @ParameterizedTest
        @EnumSource(mode = Mode.INCLUDE, names = {"COOKING", "MEAL"})
        void 주문상태가_완료인_경우_변경할_수_있다(OrderStatus orderStatus) {
            // given
            final var 두마리메뉴 = 메뉴그룹_두마리메뉴;

            final var 후라이드 = 후라이드_16000;
            final var 양념치킨 = 양념치킨_16000;

            final var 후라이드_1개 = 메뉴상품(후라이드, 1);
            final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

            final var 메뉴1 = 메뉴("후라이드양념", 25000, 두마리메뉴, List.of(후라이드_1개, 양념치킨_1개));
            final var 메뉴2 = 메뉴("싼후라이드", 10000, 두마리메뉴, List.of(후라이드_1개));

            final var tableId = 1L;
            final var orderItems = List.of(주문상품(메뉴1, 2), 주문상품(메뉴2, 3));
            final var order = new Order(tableId, orderItems);
            order.changeOrderStatus(OrderStatus.COMPLETION);

            // when & then
            assertThatThrownBy(() -> order.changeOrderStatus(orderStatus));
        }
    }
}
