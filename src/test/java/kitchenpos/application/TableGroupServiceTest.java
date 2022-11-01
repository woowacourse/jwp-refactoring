package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.support.DataSupport;
import kitchenpos.support.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private DataSupport dataSupport;

    private OrderTable savedEmptyTable1;
    private OrderTable savedEmptyTable2;

    @BeforeEach
    void saveData() {
        savedEmptyTable1 = dataSupport.saveOrderTable(0, true);
        savedEmptyTable2 = dataSupport.saveOrderTable(0, true);
    }

    @DisplayName("여러 테이블을 단체로 지정할 수 있다.")
    @Test
    void create() {
        // given, when
        final TableGroupRequest request = RequestBuilder.ofTableGroup(savedEmptyTable1, savedEmptyTable2);
        final TableGroupResponse savedTableGroup = tableGroupService.create(request);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @DisplayName("존재하지 않는 테이블을 그룹으로 지정하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableNotFound() {
        // given
        final OrderTable unsavedOrderTable = new OrderTable(0L, null, 0, true);

        // when, then
        final TableGroupRequest request = RequestBuilder.ofTableGroup(savedEmptyTable1, unsavedOrderTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("2개 미만의 테이블을 그룹으로 지정하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableUnder2() {
        final TableGroupRequest request = RequestBuilder.ofTableGroup(savedEmptyTable1);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("비어있지 않은 테이블을 그룹으로 지정하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableNotEmpty() {
        // given
        final OrderTable savedNotEmptyTable = dataSupport.saveOrderTable(0, false);

        // when, then
        final TableGroupRequest request = RequestBuilder.ofTableGroup(savedEmptyTable1, savedNotEmptyTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("이미 그룹으로 지정된 테이블을 그룹으로 지정하면 예외가 발생한다.")
    @Test
    void create_throwsException_ifTableGrouped() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        final OrderTable groupedTable = savedTableGroup.getOrderTables().get(0);

        // when, then
        final TableGroupRequest request = RequestBuilder.ofTableGroup(savedEmptyTable1, groupedTable);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("지정된 단체를 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        final List<OrderTable> orderTables = savedTableGroup.getOrderTables();

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertThat(orderTables)
                .allMatch(orderTable -> orderTable.getTableGroupId() == null);
    }

    @DisplayName("조리중인 테이블이 있는 단체를 해제하면 예외가 발생한다.")
    @Test
    void ungroup_throwsException_ifCooking() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        final OrderTable groupedTable = savedTableGroup.getOrderTables().get(0);
        dataSupport.saveOrderWithoutItem(groupedTable.getId(), OrderStatus.COOKING);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    @DisplayName("식사중인 테이블이 있는 단체를 해제하면 예외가 발생한다.")
    @Test
    void ungroup_throwsException_ifMeal() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        final OrderTable groupedTable = savedTableGroup.getOrderTables().get(0);
        dataSupport.saveOrderWithoutItem(groupedTable.getId(), OrderStatus.MEAL);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }
}
