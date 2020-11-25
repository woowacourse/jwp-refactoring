package kitchenpos.domain.model.tablegroup;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("단체 지정 생성")
    @Test
    void create() {
        TableGroup tableGroup = new TableGroup(null, null, null);

        assertThat(tableGroup.create().getCreatedDate()).isNotNull();
    }
}
