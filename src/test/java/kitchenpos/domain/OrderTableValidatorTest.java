package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.application.ServiceTest;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTableValidatorTest extends ServiceTest {

    @Autowired
    private OrderTableValidator orderTableValidator;

    @Test
    void 빈_테이블로_변경할_때_이미_단체_지정된_경우_예외가_발생한다() {
        // given
        final var orderTable1 = OrderTableFixture.빈테이블_1명();
        final var orderTable2 = OrderTableFixture.빈테이블_1명();
        final var savedOrderTables = 복수_주문테이블_저장(orderTable1, orderTable2);

        final var tableGroup = TableGroupFixture.단체지정_여러_테이블(savedOrderTables);
        단일_단체지정_저장(tableGroup);
        savedOrderTables.forEach(it -> it.group(tableGroup.getId()));
        orderTable1.changeEmpty(true);

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateChangeEmpty(orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블로_변경할_때_테이블들의_주문_상태가_조리_또는_식사면_예외가_발생한다() {
        // given
        final var orderTable1 = OrderTableFixture.빈테이블_1명();
        final var orderTable2 = OrderTableFixture.빈테이블_1명();
        final var savedOrderTables = 복수_주문테이블_저장(orderTable1, orderTable2);

        final var tableGroup = TableGroupFixture.단체지정_여러_테이블(savedOrderTables);
        단일_단체지정_저장(tableGroup);

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

        final var order = 단일_주문_저장(orderTable1, orderLineItem);
        order.changeOrderStatus(MEAL);

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateChangeEmpty(orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수를_변경할_때_빈_테이블인_경우_예외가_발생한다() {
        // given
        final var orderTable = OrderTableFixture.빈테이블_1명();
        orderTable.changeNumberOfGuests(10);

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateChangeNumberOfGuests(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수를_변경할_때_방문한_손님_수가_0보다_작으면_예외가_발생한다() {
        // given
        final var orderTable = OrderTableFixture.주문테이블_N명(1);
        orderTable.changeNumberOfGuests(-1);

        // when & then
        assertThatThrownBy(() -> orderTableValidator.validateChangeNumberOfGuests(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
