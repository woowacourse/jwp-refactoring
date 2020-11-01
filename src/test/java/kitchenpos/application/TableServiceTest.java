package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Test
    void create() {
        Long tableId = tableService.create(ORDER_TABLE_REQUEST);

        assertThat(tableId).isNotNull();
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