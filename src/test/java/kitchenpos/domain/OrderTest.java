package kitchenpos.domain;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.OrderLineItemFixture.주문상품;
import static kitchenpos.fixture.OrderTableFixture.비지않은_테이블;
import static kitchenpos.fixture.OrderTableFixture.빈테이블;
import static kitchenpos.fixture.ProductFixture.양념치킨_16000;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

class OrderTest {

    @Nested
    class addOrderTable {

        @Test
        void 주문상태를_가지는_주문에_주문테이블을_추가할_수_있다() {
            // given
            final var 두마리메뉴 = 메뉴그룹_두마리메뉴;

            final var 후라이드 = 후라이드_16000;
            final var 양념치킨 = 양념치킨_16000;

            final var 후라이드_1개 = 메뉴상품(후라이드, 1);
            final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

            final var 메뉴1 = 메뉴("후라이드양념", 25000, 두마리메뉴);
            메뉴1.addMenuProducts(List.of(후라이드_1개, 양념치킨_1개));

            final var 메뉴2 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            메뉴2.addMenuProducts(List.of(후라이드_1개));

            final var order = new Order();
            final var table = 비지않은_테이블();

            // when
            order.addOrderTable(table);

            // then
            assertThat(order.getOrderTable()).isEqualTo(table);
        }

        @Test
        void 빈테이블은_추가할_수_없다() {
            // given
            final var 두마리메뉴 = 메뉴그룹_두마리메뉴;

            final var 후라이드 = 후라이드_16000;
            final var 양념치킨 = 양념치킨_16000;

            final var 후라이드_1개 = 메뉴상품(후라이드, 1);
            final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

            final var 메뉴1 = 메뉴("후라이드양념", 25000, 두마리메뉴);
            메뉴1.addMenuProducts(List.of(후라이드_1개, 양념치킨_1개));

            final var 메뉴2 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            메뉴2.addMenuProducts(List.of(후라이드_1개));

            final var order = new Order();
            final var table = 빈테이블();

            // when & then
            assertThatThrownBy(() -> order.addOrderTable(table));
        }
    }

    @Nested
    class addOrderLineItems {

        @Test
        void 주문상태를_가지는_주문에_주문아이템을_추가할_수_있다() {
            // given
            final var 두마리메뉴 = 메뉴그룹_두마리메뉴;

            final var 후라이드 = 후라이드_16000;
            final var 양념치킨 = 양념치킨_16000;

            final var 후라이드_1개 = 메뉴상품(후라이드, 1);
            final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

            final var 메뉴1 = 메뉴("후라이드양념", 25000, 두마리메뉴);
            메뉴1.addMenuProducts(List.of(후라이드_1개, 양념치킨_1개));

            final var 메뉴2 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            메뉴2.addMenuProducts(List.of(후라이드_1개));

            final var order = new Order();
            final var orderLineItem1 = 주문상품(메뉴1, 2);
            final var orderLineItem2 = 주문상품(메뉴2, 3);

            // when
            order.addOrderLineItems(List.of(orderLineItem1, orderLineItem2));

            // then
            assertThat(order.getOrderLineItems().size()).isEqualTo(2);
        }

        @Test
        void 주문아이템은_하나_이상_존재해야_한다() {
            // given
            final var order = new Order();

            // when & then
            assertThatThrownBy(() -> order.addOrderLineItems(List.of()));
        }
    }

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

            final var 메뉴1 = 메뉴("후라이드양념", 25000, 두마리메뉴);
            메뉴1.addMenuProducts(List.of(후라이드_1개, 양념치킨_1개));

            final var 메뉴2 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            메뉴2.addMenuProducts(List.of(후라이드_1개));

            final var order = new Order();
            order.addOrderTable(비지않은_테이블());
            order.addOrderLineItems(List.of(주문상품(메뉴1, 2), 주문상품(메뉴2, 3)));

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

            final var 메뉴1 = 메뉴("후라이드양념", 25000, 두마리메뉴);
            메뉴1.addMenuProducts(List.of(후라이드_1개, 양념치킨_1개));

            final var 메뉴2 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            메뉴2.addMenuProducts(List.of(후라이드_1개));

            final var order = new Order();
            order.addOrderTable(비지않은_테이블());
            order.addOrderLineItems(List.of(주문상품(메뉴1, 2), 주문상품(메뉴2, 3)));
            order.changeOrderStatus(OrderStatus.COMPLETION);

            // when & then
            assertThatThrownBy(() -> order.changeOrderStatus(orderStatus));
        }
    }
}
