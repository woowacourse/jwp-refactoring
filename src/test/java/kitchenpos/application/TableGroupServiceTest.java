package kitchenpos.application;

import static kitchenpos.application.TestFixture.메뉴_그룹_생성;
import static kitchenpos.application.TestFixture.메뉴_상품_생성;
import static kitchenpos.application.TestFixture.메뉴_생성;
import static kitchenpos.application.TestFixture.상품_생성;
import static kitchenpos.application.TestFixture.주문_상품_생성;
import static kitchenpos.application.TestFixture.주문_생성;
import static kitchenpos.application.TestFixture.주문_테이블_생성;
import static kitchenpos.application.TestFixture.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        final OrderTable expectedOrderTable1 = 주문_테이블을_저장한다(주문_테이블_생성(1, true));
        final OrderTable expectedOrderTable2 = 주문_테이블을_저장한다(주문_테이블_생성(1, true));

        // when
        final TableGroup actual = tableGroupService.create(
                테이블_그룹_생성(List.of(expectedOrderTable1, expectedOrderTable2))
        );

        // then
        assertThat(actual.getOrderTables()).usingElementComparatorOnFields("id")
                .containsExactly(expectedOrderTable1, expectedOrderTable2);
    }

    @Test
    void 테이블_그룹_생성시_주문_테이블이_3개_미만_인_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = 주문_테이블을_저장한다(주문_테이블_생성(1, true));
        final TableGroup tableGroup = 테이블_그룹_생성(List.of(orderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_생성시_주문_테이블이_등록되지_않은_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = 주문_테이블_생성(1, true);
        final OrderTable orderTable2 = 주문_테이블을_저장한다(주문_테이블_생성(1, true));
        final TableGroup tableGroup = 테이블_그룹_생성(List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_생성시_주문_테이블이_비어있지_않은_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable1 = 주문_테이블을_저장한다(주문_테이블_생성(1, false));
        final OrderTable orderTable2 = 주문_테이블을_저장한다(주문_테이블_생성(1, true));
        final TableGroup tableGroup = 테이블_그룹_생성(List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_생성시_주문_테이블이_이미_테이블_그룹을_가진_경우_예외가_발생한다() {
        // given
        final TableGroup tableGroupWithoutOrderTables = 테이블_그룹을_저장한다(테이블_그룹_생성());
        final OrderTable orderTable1 = 주문_테이블을_저장한다(주문_테이블_생성(tableGroupWithoutOrderTables.getId(), true));
        final OrderTable orderTable2 = 주문_테이블을_저장한다(주문_테이블_생성(1, true));
        final TableGroup tableGroup = 테이블_그룹_생성(tableGroupWithoutOrderTables.getId(),
                List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        final OrderTable orderTable1 = 주문_테이블을_저장한다(주문_테이블_생성(1, true));
        final OrderTable orderTable2 = 주문_테이블을_저장한다(주문_테이블_생성(1, true));
        final TableGroup tableGroup = 테이블_그룹을_저장한다(테이블_그룹_생성(List.of(orderTable1, orderTable2)));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> actual = 주문_테이블_전체를_조회한다();
        assertAll(
                () -> assertThat(actual.get(0).getTableGroupId()).isNull(),
                () -> assertThat(actual.get(1).getTableGroupId()).isNull()
        );
    }

    @Test
    void 테이블_그룹_해제시_주문_테이블에_등록되어_있고_주문_상태가_COOKING_인_경우_예외가_발생한다() {
        // given
        final TableGroup tableGroup = 테이블_그룹을_저장한다(테이블_그룹_생성());
        final OrderTable orderTable1 = 주문_테이블을_저장한다(주문_테이블_생성(tableGroup.getId(), true));
        final OrderTable orderTable2 = 주문_테이블을_저장한다(주문_테이블_생성(tableGroup.getId(), true));
        final Product product = 상품을_저장한다(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));
        final MenuProduct menuProduct = 메뉴_상품_생성(product.getId(), 1L);
        final MenuGroup menuGroup = 메뉴_그룹을_저장한다(메뉴_그룹_생성("테스트-메뉴-그룹"));
        final Menu menu = 메뉴를_저장한다(
                메뉴_생성("테스트-메뉴-1", BigDecimal.valueOf(99999), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = 주문_상품_생성(menu.getId());
        주문을_저장한다(주문_생성(List.of(orderLineItem), orderTable1.getId(), OrderStatus.COOKING));
        주문을_저장한다(주문_생성(List.of(orderLineItem), orderTable2.getId(), OrderStatus.COOKING));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_해제시_주문_테이블에_등록되어_있고_주문_상태가_MEAL_인_경우_예외가_발생한다() {
        // given
        final TableGroup tableGroup = 테이블_그룹을_저장한다(테이블_그룹_생성());
        final OrderTable orderTable1 = 주문_테이블을_저장한다(주문_테이블_생성(tableGroup.getId(), true));
        final OrderTable orderTable2 = 주문_테이블을_저장한다(주문_테이블_생성(tableGroup.getId(), true));
        final Product product = 상품을_저장한다(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));
        final MenuProduct menuProduct = 메뉴_상품_생성(product.getId(), 1L);
        final MenuGroup menuGroup = 메뉴_그룹을_저장한다(메뉴_그룹_생성("테스트-메뉴-그룹"));
        final Menu menu = 메뉴를_저장한다(
                메뉴_생성("테스트-메뉴-1", BigDecimal.valueOf(99999), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = 주문_상품_생성(menu.getId());
        주문을_저장한다(주문_생성(List.of(orderLineItem), orderTable1.getId(), OrderStatus.MEAL));
        주문을_저장한다(주문_생성(List.of(orderLineItem), orderTable2.getId(), OrderStatus.MEAL));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
