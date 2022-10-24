package kitchenpos.application;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE1;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.OrderTableFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @DisplayName("OrderTable을 생성한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = ORDER_TABLE1.createWithIdNull();

        // when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("OrderTable들을 조회한다.")
    @Test
    void list() {
        // given
        final List<OrderTable> expected = OrderTableFixtures.createAll();

        // when
        final List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
