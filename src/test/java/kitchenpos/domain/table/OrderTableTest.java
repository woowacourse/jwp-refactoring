package kitchenpos.domain.table;

import static kitchenpos.exception.TableException.NotCompletionTableCannotChangeEmptyException;
import static kitchenpos.exception.TableException.TableGroupedTableCannotChangeEmptyException;
import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuFixture.메뉴_상품;
import static kitchenpos.fixture.MenuFixture.메뉴_상품들;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderFixture.주문_상품;
import static kitchenpos.fixture.OrderFixture.주문_상품들;
import static kitchenpos.fixture.OrderTableFixture.손님_정보;
import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.table_group.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTableTest {

    private GuestStatus guestStatus;
    private OrderLineItems orderLineItems;

    @BeforeEach
    void setup() {
        this.guestStatus = 손님_정보(10, false);
        final MenuGroup menuGroup = 메뉴_그룹("메뉴 그룹");
        final MenuProducts menuProducts = 메뉴_상품들(메뉴_상품(상품("상품1", BigDecimal.valueOf(1000L)), 1L));
        this.orderLineItems = 주문_상품들(
                주문_상품(메뉴("메뉴1", 1000L, menuProducts, menuGroup), 1L),
                주문_상품(메뉴("메뉴2", 1000L, menuProducts, menuGroup), 1L)
        );
    }

    @Test
    void 주문_테이블은_주문_상태가_완료가_아니라면_빈_테이블로_변경할_수_없다() {
        // given
        final OrderTable orderTable = new OrderTable(guestStatus);
        final Order order = 주문(orderTable, orderLineItems);

        // when
        orderTable.order(order);

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(NotCompletionTableCannotChangeEmptyException.class);
    }

    @Test
    void 주문_테이블은_단체_지정이_되어_있다면_빈_테이블로_변경할_수_없다() {
        // given
        final OrderTable orderTable = new OrderTable(guestStatus);
        final Order order = 주문(orderTable, orderLineItems);
        final TableGroup tableGroup = 테이블_그룹();

        // when
        orderTable.group(tableGroup);
        orderTable.order(order);

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(TableGroupedTableCannotChangeEmptyException.class);
    }
}
