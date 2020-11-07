package kitchenpos.acceptance;

import static kitchenpos.adapter.presentation.web.TableGroupRestController.*;
import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

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
        String request = objectMapper.writeValueAsString(TABLE_GROUP_CREATE_REQUEST);

        return post(request, API_TABLE_GROUPS);
    }
}
