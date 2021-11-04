package kitchenpos.application.tablegroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupServiceCreateTest extends TableGroupServiceTest {

    protected static final Boolean NOT_EMPTY_STATE = false;
    private static final List<OrderTable> NULL_TABLES = null;

    @DisplayName("단체를 생성할 때, 주문 테이블이 비어선 안 된다.")
    @Test
    void withNullOrderTables() {
        //given
        standardTableGroup.setOrderTables(NULL_TABLES);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성할 때, 주문 테이블이 2개보다 작아선 안 된다.")
    @Test
    void withBelowTwoOrderTables() {
        //given
        List<OrderTable> zeroOrderTables = new LinkedList<>();
        standardTableGroup.setOrderTables(zeroOrderTables);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성할 때, 저장한 주문 테이블과 원래 테이블의 갯수가 달라선 안된다.")
    @Test
    void withChangedTableNumbers() {
        //given
        List<OrderTable> zeroOrderTables = new LinkedList<>();
        given(
            orderTableDao.findAllByIdIn(Arrays.asList(FIRST_TABLE_ID, SECOND_TABLE_ID))).willReturn(
            zeroOrderTables);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성할 때, 저장한 주문 테이블이 비어있어야만 한다.")
    @Test
    void withFullTable() {
        //given
        standardTableGroup.getOrderTables()
            .forEach(table -> table.setEmpty(NOT_EMPTY_STATE));
        given(
            orderTableDao.findAllByIdIn(Arrays.asList(FIRST_TABLE_ID, SECOND_TABLE_ID))).willReturn(
            standardOrderTables);

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성할 때, 저장된 테이블들이 테이블 그룹에 할당되어 있으면 안된다.")
    @Test
    void withGrouppedTable() {
        //given
        given(
            orderTableDao.findAllByIdIn(Arrays.asList(FIRST_TABLE_ID, SECOND_TABLE_ID))).willReturn(
            standardOrderTables);
        standardTableGroup.getOrderTables()
            .forEach(table -> table.setTableGroupId(BASIC_TABLE_GROUP_ID));

        //when

        //then
        assertThatThrownBy(() -> tableGroupService.create(standardTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 생성한다.")
    @Test
    void createTableGroup() {
        //given
        given(
            orderTableDao.findAllByIdIn(Arrays.asList(FIRST_TABLE_ID, SECOND_TABLE_ID))).willReturn(
            standardOrderTables);
        given(tableGroupDao.save(standardTableGroup)).willReturn(standardTableGroup);

        //when
        TableGroup tableGroup = tableGroupService.create(standardTableGroup);

        //then
        assertAll(
            () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(BASIC_TABLE_NUMBER),
            () -> assertThat(tableGroup.getId()).isEqualTo(BASIC_TABLE_GROUP_ID),
            () -> assertThat(tableGroup.getCreatedDate()).isNotEqualTo(BASIC_CREATED_TIME)
        );
    }

}
