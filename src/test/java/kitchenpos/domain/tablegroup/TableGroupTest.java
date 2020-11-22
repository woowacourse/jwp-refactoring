package kitchenpos.domain.tablegroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    private static final long TABLE_GROUP_ID = 1L;
    private static final LocalDateTime NOW = LocalDateTime.now();

    public Table emptyTable1;
    public Table emptyTable2;
    public Table emptyTable3;
    public Table notEmptyTable1;

    @BeforeEach
    void setUp() {
        emptyTable1 = Table.of(11L, 11, true);
        emptyTable2 = Table.of(12L, 12, true);
        emptyTable3 = Table.of(13L, 13, true);
        notEmptyTable1 = Table.of(21L, 21, false);
    }

    @DisplayName("정적 팩터리 메서드 of - 예외 발생, Tables가 Null인 경우")
    @Test
    void of_NullTables_ThrownIllegalArgumentException() {
        assertThatThrownBy(() -> TableGroup.of(TABLE_GROUP_ID, NOW, null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정적 팩터리 메서드 of - 예외 발생, Tables가 Empty인 경우")
    @Test
    void of_EmptyTables_ThrownIllegalArgumentException() {
        assertThatThrownBy(() -> TableGroup.of(TABLE_GROUP_ID, NOW, new ArrayList<>()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정적 팩터리 메서드 of - 예외 발생, Tables Size가 1인 경우")
    @Test
    void of_SizeOneTables_ThrownIllegalArgumentException() {
        assertThatThrownBy(
            () -> TableGroup.of(TABLE_GROUP_ID, NOW, Collections.singletonList(emptyTable1)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정적 팩터리 메서드 of - 예외 발생, 비어있지 않은 테이블")
    @Test
    void of_NotEmptyTable_ThrownIllegalArgumentException() {
        List<Table> tables = Arrays.asList(emptyTable1, notEmptyTable1);

        assertThatThrownBy(() -> TableGroup.of(TABLE_GROUP_ID, NOW, tables))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정적 팩터리 메서드 of - 생성")
    @Test
    void of_Success() {
        List<Table> tables = Arrays.asList(emptyTable1, emptyTable2);
        TableGroup tableGroup = TableGroup.of(TABLE_GROUP_ID, NOW, tables);

        assertThat(tableGroup.getId()).isEqualTo(TABLE_GROUP_ID);
        assertThat(tableGroup.getCreatedDate()).isEqualTo(NOW);
        assertThat(tableGroup.getTables()).isEqualTo(tables);
    }

    @DisplayName("정적 팩터리 메서드 entityOf - 생성")
    @Test
    void entityOf_Success() {
        List<Table> tables = Arrays.asList(emptyTable1, emptyTable2);
        TableGroup tableGroup = TableGroup.entityOf(tables);

        assertThat(tableGroup.getId()).isNull();
        assertThat(tableGroup.getCreatedDate()).isNotNull();
        assertThat(tableGroup.getTables()).isEqualTo(tables);

    }

    @DisplayName("Table 추가 - 성공, 비어있지 않은 테이블")
    @Test
    void addTable_NotEmptyTable_Success() {
        assertThat(notEmptyTable1.isEmpty()).isFalse();

        List<Table> tables = Arrays.asList(emptyTable1, emptyTable2);
        TableGroup tableGroup = TableGroup.of(TABLE_GROUP_ID, NOW, tables);

        assertThat(tableGroup.getTables()).isEqualTo(tables);

        tableGroup.addTable(notEmptyTable1);
        assertThat(notEmptyTable1.isEmpty()).isFalse();

        assertThat(tableGroup.getTables()).hasSize(3);
        assertThat(tableGroup.getTables()).containsAll(tables);
        assertThat(tableGroup.getTables()).contains(notEmptyTable1);
    }

    @DisplayName("Table 추가 - 예외 발생, 비어있는 테이블")
    @Test
    void addTable_EmptyTable_ThrownIllegalArgumentException() {
        assertThat(emptyTable3.isEmpty()).isTrue();

        List<Table> tables = Arrays.asList(emptyTable1, emptyTable2);
        TableGroup tableGroup = TableGroup.of(TABLE_GROUP_ID, NOW, tables);

        assertThat(tableGroup.getTables()).isEqualTo(tables);

        assertThatThrownBy(() -> tableGroup.addTable(emptyTable3))
            .isInstanceOf(IllegalArgumentException.class);
        assertThat(emptyTable3.getTableGroup()).isNull();

        assertThat(tableGroup.getTables()).hasSize(2);
        assertThat(tableGroup.getTables()).containsAll(tables);
    }

    @Test
    void removeTable() {
    }

    @Test
    void unGroup() {
        List<Table> tables = Arrays.asList(emptyTable1, emptyTable2);
        TableGroup tableGroup = TableGroup.of(TABLE_GROUP_ID, NOW, tables);

        tableGroup.unGroup();
    }
}
