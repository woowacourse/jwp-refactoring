package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableTest {

    @DisplayName("changeEmpty")
    @Test
    void changeEmpty() {
        Table emptyTable = Table.of(1L, 0, true);
        assertThat(emptyTable.isEmpty()).isTrue();

        emptyTable.changeEmpty(false);
        assertThat(emptyTable.isEmpty()).isFalse();

        emptyTable.changeEmpty(true);
        assertThat(emptyTable.isEmpty()).isTrue();
    }

    @DisplayName("changeEmpty")
    @Test
    void changeEmpty_HasTableGroup_ThrownIllegalArgumentException() {
        Table emptyTable1 = Table.of(1L, 0, true);
        Table emptyTable2 = Table.of(2L, 0, true);
        TableGroup.of(3L, LocalDateTime.now(), Arrays.asList(emptyTable1, emptyTable2));
        assertThat(emptyTable1.isEmpty()).isFalse();

        assertThatThrownBy(() -> emptyTable1.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 해제 - 성공")
    @Test
    void unGroup_Success() {
        Table emptyTable1 = Table.of(1L, 0, true);
        Table emptyTable2 = Table.of(2L, 0, true);
        TableGroup.of(3L, LocalDateTime.now(), Arrays.asList(emptyTable1, emptyTable2));

        emptyTable1.unGroup();
    }

    @Test
    void setTableGroup() {
    }

    @Test
    void changeNumberOfGuests() {
    }

    @Test
    void setEmpty() {
    }
}
