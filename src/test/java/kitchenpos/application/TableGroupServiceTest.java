package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("테이블 그룹 테스트")
class TableGroupServiceTest extends ServiceTest {

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void create() {
        // given
        final OrderTable orderTable1 = createOrderTable(null, 1, true);
        final OrderTable orderTable2 = createOrderTable(null, 1, true);

        // when
        final TableGroup actual = createTableGroup(List.of(orderTable1, orderTable2));

        // then
        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @DisplayName("2개 미만의 테이블 그룹 생성에 실패한다")
    @Test
    void create_Fail() {
        // given
        final OrderTable orderTable = createOrderTable(null, 1, true);

        // when & then
        assertThatThrownBy(() -> createTableGroup(List.of(orderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어 있지 않을 경우 그룹생성에 실패한다")
    @Test
    void create_FailEmptyTable() {
        //
        final OrderTable orderTable1 = createOrderTable(null, 3, true);
        final OrderTable orderTable2 = createOrderTable(null, 3, false);

        // when
        assertThatThrownBy(() -> createTableGroup(List.of(orderTable1, orderTable2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = createOrderTable(null, 3, true);
        final OrderTable orderTable2 = createOrderTable(null, 3, true);
        final TableGroup tableGroup = createTableGroup(List.of(orderTable1, orderTable2));

        // when
        tableGroupService.ungroup(tableGroup.getId());
        final List<OrderTable> actual = tableService.list();

        // then
        assertSoftly(
                softly -> {
                    softly.assertThat(actual.get(0).getTableGroupId()).isNull();
                    softly.assertThat(actual.get(1).getTableGroupId()).isNull();
                }
        );
    }

    @DisplayName("조리 중이거나 먹는 중인 테이블이면 그룹 해제에 실패한다")
    @ParameterizedTest()
    @ValueSource(strings = {"MEAL", "COOKING"})
    void ungroup_FailNonExistTable(
            final String state
    ) {
        // given
        final OrderTable orderTable1 = createOrderTable(null, 3, true);
        final OrderTable orderTable2 = createOrderTable(null, 3, true);
        final TableGroup tableGroup = createTableGroup(List.of(orderTable1, orderTable2));

        final Order order = createOrder(orderTable1, List.of(1L, 2L));
        order.setOrderStatus(state);
        orderService.changeOrderStatus(order.getId(), order);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
    }
}
