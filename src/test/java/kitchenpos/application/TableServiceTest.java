package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.TableChangeRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    void create() {
        TableCreateRequest table = new TableCreateRequest(true, 4);

        TableResponse persistTable = tableService.create(table);

        assertAll(
            () -> assertThat(persistTable.getId()).isNotNull(),
            () -> assertThat(persistTable.getTableGroupId()).isNull(),
            () -> assertThat(persistTable).isEqualToIgnoringGivenFields(table, "id", "tableGroupId")
        );
    }

    @Test
    void list() {
        tableService.create(new TableCreateRequest(true, 4));
        tableService.create(new TableCreateRequest(true, 5));

        List<TableResponse> tables = tableService.list();
        List<Integer> numberOfGuestsInOrderTables = tables.stream()
            .map(TableResponse::getNumberOfGuests)
            .collect(Collectors.toList());

        assertThat(numberOfGuestsInOrderTables)
            .contains(ORDER_TABLE_FIXTURE_1.getNumberOfGuests(), ORDER_TABLE_FIXTURE_2.getNumberOfGuests());
    }

    @Test
    void changeEmpty() {
        TableResponse persistTable = tableService.create(new TableCreateRequest(true, 4));
        TableChangeRequest requestTable = new TableChangeRequest(true);

        TableResponse changedTable = tableService.changeEmpty(persistTable.getId(), requestTable);

        assertThat(changedTable.isEmpty()).isTrue();
    }

    @Test
    void changeNumberOfGuests() {
        TableCreateRequest table = new TableCreateRequest(false, 4);

        TableResponse persistTable = tableService.create(table);
        TableChangeRequest requestTable = new TableChangeRequest(100);

        TableResponse changedTable = tableService.changeNumberOfGuests(persistTable.getId(), requestTable);

        assertThat(changedTable.getNumberOfGuests()).isEqualTo(100);
    }
}