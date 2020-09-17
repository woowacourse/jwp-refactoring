package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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
    private OrderTable table;

    @BeforeEach
    void setUp() {
        table = OrderTable.builder()
            .numberOfGuests(0)
            .empty(true)
            .build();
    }

    @DisplayName("테이블 추가")
    @Test
    void create() {
        OrderTable create = tableService.create(table);

        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        tableService.create(table);
        tableService.create(table);

        List<OrderTable> list = tableService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 등록 불가 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable create = tableService.create(table);
        OrderTable target = OrderTable.builder()
            .empty(!table.isEmpty())
            .build();

        OrderTable changeEmpty = tableService.changeEmpty(create.getId(), target);

        assertThat(changeEmpty.isEmpty()).isEqualTo(target.isEmpty());
    }
}