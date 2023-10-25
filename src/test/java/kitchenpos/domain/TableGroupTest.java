package kitchenpos.domain;

import kitchenpos.tableGroup.domain.TableGroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupTest {

    @Test
    void create() {
        //when&then
        assertDoesNotThrow(() -> new TableGroup());
    }
}
