package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.config.DataSourceConfig;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateTableDao;
import kitchenpos.dto.table.TableChangeRequest;
import kitchenpos.dto.table.TableCreateRequest;
import kitchenpos.dto.table.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@Import(DataSourceConfig.class)
@SpringBootTest(classes = {
    TableService.class,
    JdbcTemplateTableDao.class,
    JdbcTemplateOrderDao.class
})
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("create")
    void create() {
        TableCreateRequest table = new TableCreateRequest(true, 0);

        TableResponse result = tableService.create(table);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("create - empty 를 false 로 하여 생성하기")
    void createNotEmptyTable() {
        TableCreateRequest notEmptyTable = new TableCreateRequest(false, 0);

        TableResponse result = tableService.create(notEmptyTable);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isEqualTo(false);
    }

    @Test
    @DisplayName("create - 손님이 앉은 채로 테이블 생성하기")
    void createTableWithGuests() {
        TableCreateRequest table = new TableCreateRequest(false, 5);

        TableResponse result = tableService.create(table);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(table.getNumberOfGuests());
        assertThat(result.isEmpty()).isEqualTo(table.isEmpty());
    }

    @Test
    @DisplayName("create - 손님 수는 0보다 큰데 empty=true 인 경우")
    void createTableWithGhostGuests() {
        TableCreateRequest table = new TableCreateRequest(true, 5);

        TableResponse result = tableService.create(table);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(table.getNumberOfGuests());
        assertThat(result.isEmpty()).isEqualTo(table.isEmpty());
    }

    @Test
    @DisplayName("create - Table 데이터 초기화 없이 생성")
    void createWithoutAnyInitializing() {
        TableCreateRequest table = new TableCreateRequest();

        TableResponse result = tableService.create(table);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(0);
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void list() {
        List<TableResponse> tables = tableService.list();

        assertThat(tables).hasSize(0);

        // given
        TableCreateRequest table1 = new TableCreateRequest(false);
        tableService.create(table1);

        TableCreateRequest table2 = new TableCreateRequest(true);
        tableService.create(table2);

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
        TableCreateRequest request = new TableCreateRequest(from, 0);

        TableResponse table = tableService.create(request);

        TableChangeRequest changeRequest = new TableChangeRequest(to);

        // when
        TableResponse result = tableService.changeEmpty(table.getId(), changeRequest);

        // then
        assertThat(result.getId()).isEqualTo(table.getId());
        assertThat(result.isEmpty()).isEqualTo(to);
    }

    @ParameterizedTest
    @CsvSource({"true,true", "true,false", "false,true", "false,false"})
    @DisplayName("change empty - 손님 수가 0보다 클 때")
    void changeEmpty_IfNumberOfGuestIsPositive(boolean from, boolean to) {
        // given
        TableCreateRequest request = new TableCreateRequest(from, 5);

        TableResponse table = tableService.create(request);

        TableChangeRequest changeRequest = new TableChangeRequest(to);

        // when
        TableResponse result = tableService.changeEmpty(table.getId(), changeRequest);

        // then
        assertThat(result.getId()).isEqualTo(table.getId());
        assertThat(result.isEmpty()).isEqualTo(to);
    }

    @Test
    @DisplayName("테이블의 손님 수 변경하기")
    void changeNumberOfGuests() {
        // given
        TableCreateRequest request = new TableCreateRequest(false);
        TableResponse table = tableService.create(request);

        // when
        TableChangeRequest numberChangedTable = new TableChangeRequest(5);
        TableResponse result = tableService.changeNumberOfGuests(table.getId(), numberChangedTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("테이블의 손님 수 변경하기 - empty = true 인 경우")
    void changeNumberOfGuests_IfTableIsEmpty() {
        // given
        TableCreateRequest table = new TableCreateRequest(true);

        Long tableId = tableService.create(table).getId();

        // when & then
        TableChangeRequest numberChangedTable = new TableChangeRequest(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, numberChangedTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수 변경하기 - 음수로 변경시 예외처리")
    void changeNumberOfGuests_IfNumberIsNegative_ThrowException() {
        // given
        TableCreateRequest table = new TableCreateRequest(false);

        Long tableId = tableService.create(table).getId();

        // when & then
        TableChangeRequest numberChangedTable = new TableChangeRequest(-5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, numberChangedTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
