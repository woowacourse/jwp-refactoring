package kitchenpos.table.application;

import static kitchenpos.DtoFixture.getEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getMenuCreateRequest;
import static kitchenpos.DtoFixture.getMenuGroupCreateRequest;
import static kitchenpos.DtoFixture.getNotEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getOrderCreateRequest;
import static kitchenpos.DtoFixture.getTableCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.TableGroupCreatRequest;
import kitchenpos.table.dto.response.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create() {
        final TableResponse table1 = 테이블_등록(getEmptyTableCreateRequest());
        final TableResponse table2 = 테이블_등록(getEmptyTableCreateRequest());
        final TableGroupCreatRequest request = getTableCreateRequest(List.of(table1.getId(), table2.getId()));
        final TableGroup savedTableGroup = 단체_지정(request);

        final OrderTable savedTable1 = orderTableDao.findById(table1.getId()).get();
        final OrderTable savedTable2 = orderTableDao.findById(table2.getId()).get();

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isBefore(LocalDateTime.now()),
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(savedTable1.getTableGroupId()).isEqualTo(savedTableGroup.getId()),
                () -> assertThat(savedTable2.getTableGroupId()).isEqualTo(savedTableGroup.getId())
        );
    }

    @DisplayName("단체 지정을 등록한다. - 주문 테이블이 2개보다 적으면 예외를 반환한다.")
    @Test
    void create_exception_tableLessThanTwo() {
        final TableResponse table1 = 테이블_등록(getEmptyTableCreateRequest());
        final TableGroupCreatRequest request = getTableCreateRequest(List.of(table1.getId()));

        assertThatThrownBy(() -> 단체_지정(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록한다. - 비어있지 않은 테이블이 포함되어 있다면 예외를 반환한다.")
    @Test
    void create_exception_containsNotEmptyTable() {
        final TableResponse table1 = 테이블_등록(getEmptyTableCreateRequest());
        final TableResponse table2 = 테이블_등록(getEmptyTableCreateRequest());
        final TableResponse table3 = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final TableGroupCreatRequest request = getTableCreateRequest(
                List.of(table1.getId(), table2.getId(), table3.getId()));

        assertThatThrownBy(() -> 단체_지정(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록한다. - 이미 다른 단체에 지정된 테이블이 포함되어 있다면 예외를 반환한다.")
    @Test
    void create_exception_containsAlreadyGroupedTable() {
        final TableResponse table1 = 테이블_등록(getEmptyTableCreateRequest());
        final TableResponse table2 = 테이블_등록(getEmptyTableCreateRequest());
        final TableGroupCreatRequest request1 = getTableCreateRequest(List.of(table1.getId(), table2.getId()));
        단체_지정(request1);

        final TableResponse table3 = 테이블_등록(getNotEmptyTableCreateRequest(0));
        final TableGroupCreatRequest request2 = getTableCreateRequest(List.of(table1.getId(), table3.getId()));

        assertThatThrownBy(() -> 단체_지정(request2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해체한다.")
    @Test
    void ungroup() {
        final TableResponse table1 = 테이블_등록(getEmptyTableCreateRequest());
        final TableResponse table2 = 테이블_등록(getEmptyTableCreateRequest());
        final TableGroupCreatRequest request = getTableCreateRequest(List.of(table1.getId(), table2.getId()));
        final TableGroup savedTableGroup = 단체_지정(request);

        tableGroupService.ungroup(savedTableGroup.getId());

        final OrderTable savedTable1 = orderTableDao.findById(table1.getId()).get();
        final OrderTable savedTable2 = orderTableDao.findById(table2.getId()).get();

        assertAll(
                () -> assertThat(savedTable1.getTableGroupId()).isNull(),
                () -> assertThat(savedTable2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("단체 지정을 해체한다. - 주문 상태가 COOKING이거나 MEAL인 테이블이 존재한다면 예외를 반환한다.")
    @Test
    void list_exception_orderStatusIsCookingOrMeal() {
        final TableResponse table1 = 테이블_등록(getEmptyTableCreateRequest());
        final TableResponse table2 = 테이블_등록(getEmptyTableCreateRequest());
        final MenuGroupResponse menuGroup = 메뉴_그룹_등록(getMenuGroupCreateRequest());
        final MenuResponse menu = 메뉴_등록(getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos()));

        final TableGroupCreatRequest request = getTableCreateRequest(List.of(table1.getId(), table2.getId()));
        final TableGroup savedTableGroup = 단체_지정(request);

        주문_등록(getOrderCreateRequest(table1.getId(), menu.getId()));

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
