package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;

@SpringBootTest
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("테이블을 등록한다.")
    void create() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1, false);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(orderTable);
    }

    @Test
    @DisplayName("전체 테이블을 조회한다.")
    void list() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result).contains(savedOrderTable);
    }
}
