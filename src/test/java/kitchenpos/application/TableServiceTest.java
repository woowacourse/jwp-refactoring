package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
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
        assertThat(result.getNumberOfGuests()).isEqualTo(notEmptyTable.getNumberOfGuests());
        assertThat(result.isEmpty()).isEqualTo(notEmptyTable.isEmpty());
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
    void list() {
    }

    @Test
    void changeEmpty() {
    }

    @Test
    void changeNumberOfGuests() {
    }
}
