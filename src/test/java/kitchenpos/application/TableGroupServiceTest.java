package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @Nested
    class 그룹_생성_메소드는 {

        @Test
        void 입력받은_테이블로_그룹을_저장한다() {
            // given
            OrderTable orderTable1 = 빈_테이블을_저장한다();
            OrderTable orderTable2 = 빈_테이블을_저장한다();

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            // when
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // then
            List<OrderTable> orderTables = savedTableGroup.getOrderTables();

            Assertions.assertAll(() -> {
                assertThat(savedTableGroup.getId()).isNotNull();
                assertThat(orderTables).extracting(OrderTable::getTableGroupId, OrderTable::isEmpty)
                        .containsOnly(tuple(savedTableGroup.getId(), false));
            });
        }

        @Test
        void 그룹화하려는_테이블의_수가_2보다_작다면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = 빈_테이블을_저장한다();

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블_리스트가_비어있다면_예외가_발생한다() {
            // given
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블이_존재하지_않는다면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = 빈_테이블을_저장한다();
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setNumberOfGuests(0);
            orderTable2.setEmpty(true);

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블이_비어있지_않다면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = 빈_테이블을_저장한다();
            OrderTable orderTable2 = 테이블을_저장한다(4);

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹화하려는_테이블이_이미_다른_테이블_그룹에_속해있다면_예외가_발생한다() {
            // given
            OrderTable orderTable1 = 빈_테이블을_저장한다();
            OrderTable orderTable2 = 빈_테이블을_저장한다();

            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            tableGroupService.create(tableGroup);

            OrderTable orderTable3 = 빈_테이블을_저장한다();
            TableGroup newTableGroup = new TableGroup();
            newTableGroup.setOrderTables(List.of(orderTable2, orderTable3));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제_메소드는 {

        @Test
        void 테이블_그룹을_해제한다() {
            // given
            OrderTable orderTable1 = 빈_테이블을_저장한다();
            OrderTable orderTable2 = 빈_테이블을_저장한다();
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // when
            tableGroupService.ungroup(savedTableGroup.getId());

            // then
            List<Long> findTableGroupIds = tableService.list()
                    .stream()
                    .filter(table -> table.getId().equals(orderTable1.getId()) ||
                            table.getId().equals(orderTable2.getId()))
                    .map(OrderTable::getTableGroupId)
                    .collect(Collectors.toList());

            assertThat(findTableGroupIds).containsOnly((Long) null);
        }

        // TODO
        @Disabled
        @Test
        void 해제하려는_테이블들의_주문_상태가_계산_완료_상태가_아니라면_예외가_발생한다() {
            // given


            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}