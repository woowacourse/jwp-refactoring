package kitchenpos.domain.table_group;

import static kitchenpos.exception.TableGroupException.HasAlreadyGroupedTableException;
import static kitchenpos.exception.TableGroupException.HasEmptyTableException;
import static kitchenpos.exception.TableGroupException.NoMinimumOrderTableSizeException;
import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuFixture.메뉴_상품;
import static kitchenpos.fixture.MenuFixture.메뉴_상품들;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderFixture.주문_상품;
import static kitchenpos.fixture.OrderFixture.주문_상품들;
import static kitchenpos.fixture.OrderTableFixture.손님_정보;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.TableGroupException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class TableGroupTest {

    @Test
    void 두_개_이상의_주문_테이블은_단체지정을_할_수_있다() {
        // given
        final OrderTable orderTable1 = 주문_테이블(손님_정보(10, false));
        final OrderTable orderTable2 = 주문_테이블(손님_정보(10, false));

        // expected
        assertDoesNotThrow(() -> new TableGroup(List.of(orderTable1, orderTable2)));
    }

    @Test
    void 주문_테이블_개수가_두_개_보다_작다면_단체지정을_할_수_없다() {
        // given
        final OrderTable orderTable = 주문_테이블(손님_정보(10, false));

        // expected
        assertThatThrownBy(() -> new TableGroup(List.of(orderTable)))
                .isInstanceOf(NoMinimumOrderTableSizeException.class);
    }

    @Test
    void 빈_테이블이_있을_경우_단체지정을_할_수_없다() {
        // given
        final OrderTable orderTable1 = 주문_테이블(손님_정보(10, true));
        final OrderTable orderTable2 = 주문_테이블(손님_정보(10, false));

        // expected
        assertThatThrownBy(() -> new TableGroup(List.of(orderTable1, orderTable2)))
                .isInstanceOf(HasEmptyTableException.class);
    }

    @Test
    void 이미_단체지정이_된_테이블이_있을_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = 주문_테이블(손님_정보(10, false));
        final OrderTable orderTable2 = 주문_테이블(손님_정보(10, false));
        final TableGroup otherTableGroup = 테이블_그룹();
        orderTable1.group(otherTableGroup);

        // expected
        assertThatThrownBy(() -> new TableGroup(List.of(orderTable1, orderTable2)))
                .isInstanceOf(HasAlreadyGroupedTableException.class);
    }

    @Test
    void 조리_중인_주문이_있는_주문_테이블이_있는_경우_그룹_해제를_하면_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = 주문_테이블(손님_정보(10, false));
        final OrderTable orderTable2 = 주문_테이블(손님_정보(10, false));
        final Order order = 주문하다(orderTable1);
        orderTable1.order(order);
        order.changeOrderStatus(OrderStatus.COOKING);

        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when
        assertThatThrownBy(tableGroup::upGroup)
                .isInstanceOf(TableGroupException.CannotUngroupException.class);
    }

    private Order 주문하다(final OrderTable orderTable) {
        final MenuGroup menuGroup = 메뉴_그룹("메뉴그룹");
        final Product product = 상품("치킨", BigDecimal.valueOf(1000));
        final MenuProduct menuProduct = 메뉴_상품(product, 1L);
        final Menu menu = 메뉴("메뉴", 1000L, 메뉴_상품들(menuProduct), menuGroup);
        final OrderLineItem orderLineItem = 주문_상품(menu, 1L);
        final OrderLineItems orderLineItems = 주문_상품들(orderLineItem);
        return 주문(orderTable, orderLineItems);
    }

    @Test
    void 식사_중인_주문이_있는_주문_테이블이_있는_경우_그룹_해제를_하면_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = 주문_테이블(손님_정보(10, false));
        final OrderTable orderTable2 = 주문_테이블(손님_정보(10, false));
        final Order order = 주문하다(orderTable1);
        orderTable1.order(order);
        order.changeOrderStatus(OrderStatus.MEAL);

        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when
        assertThatThrownBy(tableGroup::upGroup)
                .isInstanceOf(TableGroupException.CannotUngroupException.class);
    }
}
