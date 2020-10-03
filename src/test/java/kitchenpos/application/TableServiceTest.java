package kitchenpos.application;

import kitchenpos.TestObjectFactory;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("/delete_all.sql")
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @DisplayName("테이블 생성 메서드 테스트")
    @Test
    void create() {
        OrderTable orderTable = TestObjectFactory.creatOrderTable();

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("테이블 목록 조회 기능 테스트")
    @Test
    void list() {
        tableService.create(TestObjectFactory.creatOrderTable());
        tableService.create(TestObjectFactory.creatOrderTable());

        List<OrderTable> tables = tableService.list();

        assertThat(tables).hasSize(2);
    }
}
