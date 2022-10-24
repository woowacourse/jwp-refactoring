package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TableGroupService 클래스의")
class TableGroupServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("orderTable 목록을 그룹화한다.")
        void success() {
            // given
            OrderTable orderTable1 = tableService.create(saveOrderTable(2, true));
            OrderTable orderTable2 = tableService.create(saveOrderTable(4, true));
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

            // when
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // then
            Optional<TableGroup> actual = tableGroupDao.findById(savedTableGroup.getId());
            assertThat(actual).isPresent();
        }

        @Test
        @DisplayName("그룹화할 orderTable이 2개 미만인 경우 예외를 던진다.")
        void orderTableSize_SmallerThanTwo_ExceptionThrown() {
            // given
            OrderTable orderTable1 = tableService.create(saveOrderTable(2, true));
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Collections.singletonList(orderTable1));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화할 orderTable이 존재하지 않는 경우 예외를 던진다.")
        void orderTable_NotExist_ExceptionThrown() {
            // given
            OrderTable orderTable1 = tableService.create(saveOrderTable(2, true));
            OrderTable orderTable2 = createOrderTable(4, true);
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화할 orderTable이 빈 테이블이 아닌 경우 예외를 던진다.")
        void orderTable_NotEmpty_ExceptionThrown() {
            // given
            OrderTable orderTable1 = tableService.create(saveOrderTable(2, true));
            OrderTable orderTable2 = tableService.create(saveOrderTable(4, false));
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화할 orderTable이 이미 그룹에 속한 경우 예외를 던진다.")
        void orderTable_alreadyGrouped_ExceptionThrown() {
            // given
            OrderTable orderTable1 = tableService.create(saveOrderTable(2, true));
            OrderTable orderTable2 = tableService.create(saveOrderTable(4, true));
            OrderTable orderTable3 = tableService.create(saveOrderTable(4, true));
            OrderTable orderTable4 = tableService.create(saveOrderTable(4, true));
            TableGroup tableGroup1 = new TableGroup();
            tableGroup1.setOrderTables(Arrays.asList(orderTable1, orderTable2));
            tableGroupService.create(tableGroup1);
            TableGroup tableGroup2 = new TableGroup();
            tableGroup2.setOrderTables(Arrays.asList(orderTable2, orderTable3, orderTable4));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("ungroup 메서드는")
    class Ungroup {

        @Test
        @DisplayName("그룹을 해제한다.")
        void success() {
            // given
            OrderTable orderTable1 = tableService.create(saveOrderTable(2, true));
            OrderTable orderTable2 = tableService.create(saveOrderTable(4, true));
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            assertAll(
                    () -> assertThat(orderTableDao.findById(orderTable1.getId()).get().getTableGroupId()).isNull(),
                    () -> assertThat(orderTableDao.findById(orderTable1.getId()).get().isEmpty()).isFalse(),
                    () -> assertThat(orderTableDao.findById(orderTable2.getId()).get().getTableGroupId()).isNull(),
                    () -> assertThat(orderTableDao.findById(orderTable2.getId()).get().isEmpty()).isFalse()
            );
        }

        @Disabled("order 테스트 추가 후 구현하기")
        @Test
        @DisplayName("orderTable에 해당하는 order의 orderStatus가 COMPLETION이 아닌 경우 예외를 던진다.")
        void orderStatus_NotCompletion_ExceptionThrown() {
        }
    }
}
