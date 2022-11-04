package kitchenpos.domain.table;

import static kitchenpos.domain.DomainTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.DomainTestFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("테이블을 그룹으로 묶는다.")
    void of() {
        final LocalDateTime startTime = LocalDateTime.now();
        final OrderTable testOrderTable1 = getTestOrderTable1();
        final OrderTable testOrderTable2 = getTestOrderTable2();
        final TableGroup tableGroup = TableGroup.of(List.of(testOrderTable1, testOrderTable2));

        assertAll(
                () -> assertThat(tableGroup.getCreatedDate()).isBefore(LocalDateTime.now()),
                () -> assertThat(tableGroup.getCreatedDate()).isAfter(startTime),
                () -> assertThat(tableGroup.getOrderTables()).extracting("id")
                        .contains(testOrderTable1.getId(), testOrderTable2.getId())
        );
    }
}