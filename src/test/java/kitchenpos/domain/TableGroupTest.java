package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.factory.TableGroupFactory;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    void updateCreatedDate() {
        // given
        TableGroup tableGroup = TableGroupFactory.builder()
            .id(1L)
            .orderTables(Collections.emptyList())
            .build();

        // when
        tableGroup.updateCreatedDate();

        // then
        assertThat(tableGroup.getCreatedDate()).isBefore(LocalDateTime.now());
    }
}
