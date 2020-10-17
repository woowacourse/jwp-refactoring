package kitchenpos.tablegroup;

import static java.util.Arrays.*;
import static kitchenpos.ui.TableGroupRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

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
     * When 단체 지정 해제 요청.
     * Then 단체 지정이 해제 된다.
     */
    @DisplayName("단체 지정 관리")
    @TestFactory
    Stream<DynamicTest> manageTableGroup() throws Exception {
        // 단체 지정 생성
        Long tableGroupId = createTableGroup();
        assertThat(tableGroupId).isNotNull();

        return Stream.of(
                dynamicTest("단체 지정 해제", () -> {
                    delete(API_TABLE_GROUPS, tableGroupId);
                })
        );
    }

    private Long createTableGroup() throws Exception {
        OrderTable orderTable1 = orderTableFactory.create(1L);
        OrderTable orderTable2 = orderTableFactory.create(2L);
        TableGroup tableGroup = tableGroupFactory.create(asList(orderTable1, orderTable2));

        String request = objectMapper.writeValueAsString(tableGroup);
        return post(request, API_TABLE_GROUPS);
    }
}
