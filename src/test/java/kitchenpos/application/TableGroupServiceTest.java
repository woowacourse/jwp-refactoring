package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupOrderTableCreateRequest;

class TableGroupServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 테이블 그룹을 생성한다.")
        void create() {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable(true);
            OrderTable orderTable2 = createAndSaveOrderTable(true);
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                new ArrayList<TableGroupOrderTableCreateRequest>() {{
                    add(new TableGroupOrderTableCreateRequest(orderTable1.getId()));
                    add(new TableGroupOrderTableCreateRequest(orderTable2.getId()));
                }}
            );

            // when
            TableGroup savedTableGroup = tableGroupService.create(request);

            // then
            assertThat(savedTableGroup.getId()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 테이블인 경우 예외가 발생한다.")
        void invalidOrderTable() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                new ArrayList<TableGroupOrderTableCreateRequest>() {{
                    add(new TableGroupOrderTableCreateRequest(0L));
                    add(new TableGroupOrderTableCreateRequest(0L));
                }}
            );

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 테이블입니다.");
        }

        @Test
        @DisplayName("비어있지 않은 테이블인 경우 예외가 발생한다.")
        void notEmptyOrderTable() {
            // given
            OrderTable orderTable1 = createAndSaveOrderTable(false);
            OrderTable orderTable2 = createAndSaveOrderTable(false);
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                new ArrayList<TableGroupOrderTableCreateRequest>() {{
                    add(new TableGroupOrderTableCreateRequest(orderTable1.getId()));
                    add(new TableGroupOrderTableCreateRequest(orderTable2.getId()));
                }}
            );

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있지 않은 테이블입니다.");
        }

        @Test
        @DisplayName("테이블의 tableGroupId가 존재하는 경우 예외가 발생한다.")
        void existTableGroupId() {
            // given
            long savedGroupId = createAndSaveTableGroup().getId();
            OrderTable orderTable1 = createAndSaveOrderTable(true, savedGroupId);
            OrderTable orderTable2 = createAndSaveOrderTable(true, savedGroupId);
            TableGroupCreateRequest request = new TableGroupCreateRequest(
                new ArrayList<TableGroupOrderTableCreateRequest>() {{
                    add(new TableGroupOrderTableCreateRequest(orderTable1.getId()));
                    add(new TableGroupOrderTableCreateRequest(orderTable2.getId()));
                }}
            );

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 다른 그룹에 존재하는 테이블입니다.");
        }

    }

    @Nested
    @DisplayName("ungroup()")
    class UngroupMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 그룹이 해제된다.")
        void ungroup() {
            // given
            long savedGroupId = createAndSaveTableGroup().getId();
            createAndSaveOrderTable(false, savedGroupId);
            createAndSaveOrderTable(false, savedGroupId);

            // when
            tableGroupService.ungroup(savedGroupId);

            // then
            assertThat(orderTableDao.findAllByTableGroupId(savedGroupId)).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("COOKING 혹은 MEAL 상태의 테이블인 경우 예외가 발생한다.")
        void wrongTableState(String status) {
            // given
            long savedGroupId = createAndSaveTableGroup().getId();
            OrderTable orderTable = createAndSaveOrderTable(false, savedGroupId);
            createAndSaveOrderTable(false, savedGroupId);

            Order order = new Order(orderTable.getId());
            order.changeStatus(status);
            orderDao.save(order);

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(savedGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 시작되어 그룹을 해제할 수 없습니다.");
        }
    }

    private TableGroup createAndSaveTableGroup() {
        TableGroup tableGroup = new TableGroup();
        return tableGroupDao.save(tableGroup);
    }

    private OrderTable createAndSaveOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable(10, empty);

        return orderTableDao.save(orderTable);
    }

    private OrderTable createAndSaveOrderTable(boolean empty, long tableGroupId) {
        OrderTable orderTable = new OrderTable(10, empty);
        orderTable.setTableGroupId(tableGroupId);

        return orderTableDao.save(orderTable);
    }

}
