package kitchenpos.table;

import static kitchenpos.ui.TableRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;

public class TableAcceptanceTest extends AcceptanceTest {
    /**
     * 테이블을 관리한다.
     * <p>
     * When 테이블을 생성 요청.
     * Then 테이블이 생성 된다.
     * <p>
     * When 테이블 전체 조회 요청.
     * Then 전체 테이블을 반환한다.
     */
    @DisplayName("테이블 관리")
    @TestFactory
    Stream<DynamicTest> manageTable() throws Exception {
        // 테이블 생성
        Long tableId = createTable();
        assertThat(tableId).isNotNull();

        return Stream.of(
                dynamicTest("테이블 전체 조회", () -> {
                    List<OrderTable> tables = showTables();
                    OrderTable lastTable = tables.get(tables.size() - 1);

                    assertThat(lastTable.getId()).isEqualTo(tableId);
                })
        );
    }

    private Long createTable() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        String request = objectMapper.writeValueAsString(orderTable);
        return create(request, API_TABLES);
    }

    private List<OrderTable> showTables() throws Exception {
        return showAll(OrderTable.class, API_TABLES);
    }
}
