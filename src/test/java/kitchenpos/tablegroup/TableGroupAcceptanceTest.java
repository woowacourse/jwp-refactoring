package kitchenpos.tablegroup;

import static kitchenpos.ui.TableGroupRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupAcceptanceTest extends AcceptanceTest {
    /**
     * 단체 지정 기능을 관리한다.
     * <p>
     * When 단체 지정 생성 요청.
     * Then 단체 지정이 생성 된다.
     * <p>
     * Given 단체 지정이 생성 되어 있다.
     * When 단체 지정 삭제 요청.
     * Then 단체 지정이 삭제 된다.
     */
    @DisplayName("단체 지정 관리")
    @TestFactory
    Stream<DynamicTest> manageTableGroup() throws Exception {
        // 단체 지정 생성
        Long tableGroupId = createTableGroup();
        assertThat(tableGroupId).isNotNull();

        return Stream.of(
                dynamicTest("단체 지정 삭제", () -> {
                    delete(API_TABLE_GROUPS, tableGroupId);
                })
        );
    }

    private Long createTableGroup() throws Exception {
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        String request = objectMapper.writeValueAsString(tableGroup);
        return post(request, API_TABLE_GROUPS);
    }
}
