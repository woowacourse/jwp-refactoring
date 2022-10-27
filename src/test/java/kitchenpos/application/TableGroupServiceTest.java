package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTestEnvironment {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹을 등록할 수 있다.")
    void create() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);

        // when
        final TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertThat(actual.getOrderTables())
                .usingElementComparatorOnFields("id")
                .containsExactly(savedTable1, savedTable2, savedTable3);
    }

    @Test
    @DisplayName("2개 미만의 테이블을 테이블 그룹으로 등록할 수 없다.")
    void create_ExceptionOrderTablesLowerThanTwo() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionNotSavedOrderTable() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, orderTable3);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않는 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionEmptyOrderTable() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(false, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 테이블 그룹에 속한 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionAlreadyCreatedTableGroup() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        final TableGroup tableGroup1 = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup savedTableGroup1 = serviceDependencies.save(tableGroup1);
        savedTable1.setTableGroupId(savedTableGroup1.getId());
        serviceDependencies.save(savedTable1);

        final TableGroup tableGroup2 = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 테이블 그룹을 해제할 수 있다.")
    void ungroup() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup savedTableGroup = serviceDependencies.save(tableGroup);

        // when, then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("해제하려는 그룹에 속한 주문 테이블이 조리나 식사 상태면 안된다.")
    void ungroup_exeptionOrderTableCookingOrMeal(final OrderStatus orderStatus) {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = serviceDependencies.save(orderTable1);
        final OrderTable savedTable2 = serviceDependencies.save(orderTable2);
        final OrderTable savedTable3 = serviceDependencies.save(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup savedTableGroup = serviceDependencies.save(tableGroup);

        final Order order = OrderFixture.create(savedTable1, orderStatus);
        serviceDependencies.save(order);
        savedTable1.setTableGroupId(savedTableGroup.getId());
        serviceDependencies.save(savedTable1);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
