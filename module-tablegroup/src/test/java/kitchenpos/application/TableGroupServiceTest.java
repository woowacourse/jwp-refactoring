package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 테이블_그룹_생성() {
        // given
        List<Long> tableIds = List.of(1L, 2L);

        // when
        TableGroup result = tableGroupService.create(tableIds);

        // then
        assertThat(result.id()).isNotNull();
    }
}
