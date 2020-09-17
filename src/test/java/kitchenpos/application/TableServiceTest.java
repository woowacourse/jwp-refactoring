package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @DisplayName("테이블 추가")
    @Test
    void create() {
        OrderTable table = OrderTable.builder()
            .numberOfGuests(0)
            .empty(true)
            .build();

        OrderTable create = tableService.create(table);

        assertThat(create.getId()).isNotNull();
    }
}