package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableService tableService;

    @Test
    void Order_Table_생성() {
        // given
        OrderTable orderTable = OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false);

        // when
        Long orderTableId = tableService.create(orderTable)
                .getId();

        // then
        OrderTable savedOrderTableId = orderTableDao.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(savedOrderTableId).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTable);
    }

    @Test
    void Order_Table_목록을_반환한다() {
        // given
        List<OrderTable> orderTables = List.of(
                OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false),
                OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false),
                OrderTableFixture.테이블_그룹이_없는_주문_테이블_생성(1, false)
        );
        List<OrderTable> savedOrderTables = new ArrayList<>();
        for (OrderTable orderTable : orderTables) {
            savedOrderTables.add(tableService.create(orderTable));
        }

        // when
        List<OrderTable> orderTablesExcludeExistingData = tableService.list()
                .stream()
                .filter(orderTable ->
                        containsObjects(
                                savedOrderTables,
                                orderTableInSavedOrderTables -> orderTableInSavedOrderTables.getId()
                                        .equals(orderTable.getId())
                        )
                )
                .collect(Collectors.toList());

        // then
        assertThat(orderTablesExcludeExistingData).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTables);
    }

}
