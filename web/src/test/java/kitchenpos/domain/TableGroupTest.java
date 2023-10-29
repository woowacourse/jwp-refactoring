package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.ordertable.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("주문 테이블 그룹을 생성할 수 있다.")
    void create() {
        // given
        TableGroup tableGroup = TableGroupFixture.TABLE_GROUP_AVAILABLE.toEntity();

        // when
        tableGroup.group();

        // then
        assertAll(
            () -> assertThat(tableGroup.getOrderTables()).hasSize(2),
            () -> assertThat(tableGroup.getOrderTables().get(0).getTableGroupId()).isEqualTo(
                tableGroup.getId()),
            () -> assertThat(tableGroup.getOrderTables().get(1).getTableGroupId()).isEqualTo(
                tableGroup.getId())
        );
    }
}
