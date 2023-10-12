package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.전체_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;

    @Nested
    @DisplayName("Table Group을 추가한다.")
    class Create {

        @Test
        @DisplayName("테이블 그룹을 정상적으로 생성한다.")
        void success() {
            //given
            final List<OrderTable> savedOrderTables = 전체_주문_테이블().stream()
                .map(tableService::create)
                .collect(Collectors.toList());

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(savedOrderTables);

            //when
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            //then
            tableGroup.getOrderTables().forEach(orderTable -> {
                orderTable.setTableGroupId(savedTableGroup.getId());
                orderTable.setEmpty(false);
            });
            assertThat(savedTableGroup)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderTables.id")
                .isEqualTo(tableGroup);
        }

        @Test
        @DisplayName("ordertable 이 비어있는 경우 예외처리한다.")
        void throwExceptionOrderTablesAreEmpty() {
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Collections.emptyList());

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("저장되지 않은 tableGroup로 요청하는 경우 Exception을 throw한다.")
        void throwExceptionOrderTablesAreNotFound() {
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(전체_주문_테이블());

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("tableGroup안에 orderTable이 비어있지 않은 경우 Exception을 throw한다.")
        void throwExceptionOrderTablesAreNotEmpty() {
            final List<OrderTable> orderTables = 전체_주문_테이블();
            orderTables.forEach(orderTable -> orderTable.setEmpty(false));
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(orderTables);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
