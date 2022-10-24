package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.support.MenuFixture.MENU_PRICE_10000;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static kitchenpos.support.MenuProductFixture.MENU_PRODUCT_1;
import static kitchenpos.support.OrderFixture.ORDER_COOKING_1;
import static kitchenpos.support.OrderLineItemFixture.ORDER_LINE_ITEM_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_EMPTY_1;
import static kitchenpos.support.OrderTableFixture.ORDER_TABLE_NOT_EMPTY_1;
import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_10000;
import static kitchenpos.support.TableGroupFixture.TABLE_GROUP_NOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @Test
    void 테이블그룹을_생성한다() {
        // given
        final OrderTable firstSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final OrderTable secondSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(firstSavedOrderTable, secondSavedOrderTable));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_그룹이_2개이상이_아니면_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_그룹테이블이_존재하지_않으면_예외를_발생한다() {
        // given
        final OrderTable firstSavedOrderTable = ORDER_TABLE_EMPTY_1.생성();
        final OrderTable secondSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(firstSavedOrderTable, secondSavedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_테이블이_비어있지_않다면_예외를_발생한다() {
        // given
        final OrderTable fullSavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_NOT_EMPTY_1.생성());
        final OrderTable emptySavedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(fullSavedOrderTable, emptySavedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할_때_묶을_테이블중_이미_테이블_그룹이_있다면_예외를_발생한다() {
        // given
        final OrderTable alreadySavedOrderTable1 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final OrderTable alreadySavedOrderTable2 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        테이블_그룹을_저장한다(TABLE_GROUP_NOW.생성(List.of(alreadySavedOrderTable1, alreadySavedOrderTable2)));

        final OrderTable savedOrderTable = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = TABLE_GROUP_NOW.생성(List.of(alreadySavedOrderTable1, savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_해제할_수_있다() {
        // given
        final Long productId = 제품을_저장한다(PRODUCT_PRICE_10000.생성()).getId();
        final OrderTable alreadySavedOrderTable1 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final OrderTable alreadySavedOrderTable2 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = 테이블_그룹을_저장한다(
                TABLE_GROUP_NOW.생성(List.of(alreadySavedOrderTable1, alreadySavedOrderTable2)));

        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Long menuId = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId, List.of(MENU_PRODUCT_1.생성(productId)))).getId();
        final Order firstOrder = 주문을_저장한다(
                ORDER_COOKING_1.생성(alreadySavedOrderTable1.getId(), List.of(ORDER_LINE_ITEM_1.생성(menuId))));
        final Order secondOrder = 주문을_저장한다(
                ORDER_COOKING_1.생성(alreadySavedOrderTable2.getId(), List.of(ORDER_LINE_ITEM_1.생성(menuId))));
        주문_상태를_변경한다(firstOrder.getId(), COMPLETION);
        주문_상태를_변경한다(secondOrder.getId(), COMPLETION);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final Optional<OrderTable> foundOrderTable1 = tableService.list()
                .stream()
                .filter(orderTable -> orderTable.getId().equals(alreadySavedOrderTable1.getId()))
                .findFirst();

        assertAll(
                () -> assertThat(foundOrderTable1).isPresent(),
                () -> assertThat(foundOrderTable1.get().getTableGroupId()).isNull()
        );
    }

    @Test
    void 테이블_그룹을_해제할_때_주문테이블의_주문상태가_제조중이거나_식사중이면_예외를_발생한다() {
        // given
        final Long productId = 제품을_저장한다(PRODUCT_PRICE_10000.생성()).getId();
        final OrderTable alreadySavedOrderTable1 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final OrderTable alreadySavedOrderTable2 = 주문테이블을_저장한다(ORDER_TABLE_EMPTY_1.생성());
        final TableGroup tableGroup = 테이블_그룹을_저장한다(
                TABLE_GROUP_NOW.생성(List.of(alreadySavedOrderTable1, alreadySavedOrderTable2)));
        final Long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Long menuId = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId, List.of(MENU_PRODUCT_1.생성(productId)))).getId();
        주문을_저장한다(ORDER_COOKING_1.생성(alreadySavedOrderTable1.getId(), List.of(ORDER_LINE_ITEM_1.생성(menuId))));
        주문을_저장한다(ORDER_COOKING_1.생성(alreadySavedOrderTable2.getId(), List.of(ORDER_LINE_ITEM_1.생성(menuId))));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
