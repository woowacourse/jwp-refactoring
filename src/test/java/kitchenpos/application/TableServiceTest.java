package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;
    
    @Test
    @DisplayName("create")
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        OrderTable result = tableService.create(orderTable);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("create - tableGroupId 같이 무의미한 정보가 담긴 경우")
    void create_WhenParameterHasMeaninglessInfo() {
        OrderTable orderTable = new OrderTable();

        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        orderTable.setId(10_000L);    // meaningless
        orderTable.setTableGroupId(10L);    // meaningless

        OrderTable result = tableService.create(orderTable);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isNotEqualTo(orderTable.getId());
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    @DisplayName("create - empty 를 false 로 하여 생성하기")
    void createNotEmptyTable() {
        OrderTable notEmptyTable = new OrderTable();
        notEmptyTable.setEmpty(false);
        notEmptyTable.setNumberOfGuests(0);

        OrderTable result = tableService.create(notEmptyTable);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isEqualTo(false);
    }

    @Test
    @DisplayName("create - 손님이 앉은 채로 테이블 생성하기")
    void createTableWithGuests() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);

        OrderTable result = tableService.create(orderTable);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    @DisplayName("create - 손님 수는 0보다 큰데 empty=true 인 경우")
    void createTableWithGhostGuests() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(5);

        OrderTable result = tableService.create(orderTable);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    @DisplayName("create - OrderTable 데이터 초기화 없이 생성")
    void createWithoutAnyInitializing() {
        OrderTable orderTable = new OrderTable();

        OrderTable result = tableService.create(orderTable);

        // Todo: 나중에 도메인 변경할것. 손님수 0명인데 안비어있는게 기본인게 좀 이상함
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void list() {
        List<OrderTable> tables = tableService.list();

        assertThat(tables).hasSize(0);

        // given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(false);
        tableService.create(orderTable1);

        OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(true);
        tableService.create(orderTable2);

        // when
        tables = tableService.list();

        // then
        assertThat(tables).hasSize(2);
    }

    @ParameterizedTest
    @CsvSource({"true,true", "true,false", "false,true", "false,false"})
    @DisplayName("change empty")
    void changeEmpty(boolean from, boolean to) {
        // given
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(from);

        table = tableService.create(table);

        OrderTable notEmptyTable = new OrderTable();
        notEmptyTable.setEmpty(to);

        // when
        OrderTable result = tableService.changeEmpty(table.getId(), notEmptyTable);

        // then
        assertThat(result.getId()).isEqualTo(table.getId());
        assertThat(result.isEmpty()).isEqualTo(to);
    }

    // Todo: 손님수가 0보다클때 empty 를 true 로 바꿀 수 있는데, empty 가 true 이면 손님수를 0으로 못바꿈
    @ParameterizedTest
    @CsvSource({"true,true", "true,false", "false,true", "false,false"})
    @DisplayName("change empty - 손님 수가 0보다 클 때")
    void changeEmpty_IfNumberOfGuestIsPositive(boolean from, boolean to) {
        // given
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(5);
        table.setEmpty(from);

        table = tableService.create(table);

        OrderTable notEmptyTable = new OrderTable();
        notEmptyTable.setEmpty(to);

        // when
        OrderTable result = tableService.changeEmpty(table.getId(), notEmptyTable);

        // then
        assertThat(result.getId()).isEqualTo(table.getId());
        assertThat(result.isEmpty()).isEqualTo(to);
    }

    @Test
    @DisplayName("테이블의 손님 수 변경하기")
    void changeNumberOfGuests() {
        // given
        OrderTable table = new OrderTable();
        table.setEmpty(false);
        table = tableService.create(table);

        // when
        OrderTable numberChangedTable = new OrderTable();
        numberChangedTable.setNumberOfGuests(5);
        OrderTable result = tableService.changeNumberOfGuests(table.getId(), numberChangedTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("테이블의 손님 수 변경하기 - empty = true 인 경우")
    void changeNumberOfGuests_IfTableIsEmpty() {
        // given
        OrderTable table = new OrderTable();
        table.setEmpty(true);

        Long tableId = tableService.create(table).getId();

        // when & then
        OrderTable numberChangedTable = new OrderTable();
        numberChangedTable.setNumberOfGuests(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, numberChangedTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수 변경하기 - 음수로 변경시 예외처리")
    void changeNumberOfGuests_IfNumberIsNegative_ThrowException() {
        // given
        OrderTable table = new OrderTable();
        table.setEmpty(false);

        Long tableId = tableService.create(table).getId();

        // when & then
        OrderTable numberChangedTable = new OrderTable();
        numberChangedTable.setNumberOfGuests(-5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, numberChangedTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
