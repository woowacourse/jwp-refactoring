package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {
    @DisplayName("단체 지정을 생성할 수 있다.")
    @Test
    void constructor() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.MIN);

        assertAll(
            () -> assertThat(tableGroup.getId()).isEqualTo(1L),
            () -> assertThat(tableGroup.getCreatedDate()).isEqualTo(LocalDateTime.MIN)
        );
    }
}
