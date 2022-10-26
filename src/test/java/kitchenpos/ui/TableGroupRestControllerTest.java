package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("테이블 그룹 관련 기능에서")
class TableGroupRestControllerTest extends ControllerTest {

    @Autowired
    private TableGroupRestController tableGroupController;

    @Nested
    @DisplayName("테이블 그룹을 생성할 때")
    class CreateTableGroup {

        @Test
        @DisplayName("정상적으로 생성한다.")
        void create() {
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(createOrderTable(2, true), createOrderTable(2, true)));

            ResponseEntity<TableGroup> response = tableGroupController.create(tableGroup);

            assertThat(response.getBody().getId()).isNotNull();
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        @DisplayName("주문 테이블이 2개 미만일 경우 예외가 발생한다.")
        void createInvalidOrderTableSize() {
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(createOrderTable(2, true)));

            assertThatThrownBy(() -> tableGroupController.create(tableGroup))
                    .hasMessage("주문 테이블은 2개 이상이어야 합니다.");
        }

        @Test
        @DisplayName("빈 주문 테이블이 아닌경우 예외가 발생한다.")
        void createInvalidOrderTable() {
            TableGroup tableGroup = new TableGroup();

            tableGroup.setOrderTables(List.of(createOrderTable(2, false), createOrderTable(2, true)));

            assertThatThrownBy(() -> tableGroupController.create(tableGroup))
                    .hasMessage("빈 주문 테이블이어야 합니다.");
        }
    }

    @Nested
    @DisplayName("테이블 그룹을 해제할 때")
    class UnGroup {

        @Test
        @DisplayName("테이블 그룹을 해제한다.")
        void ungroup() {
            OrderTable orderTable1 = createOrderTable(2, true);
            OrderTable orderTable2 = createOrderTable(2, true);
            TableGroup tableGroup = createTableGroup(List.of(orderTable1, orderTable2));

            assertThatNoException().isThrownBy(() -> tableGroupController.ungroup(tableGroup.getId()));
        }
    }
}
