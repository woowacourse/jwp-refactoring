package kitchenpos.domain;

import static kitchenpos.exception.ExceptionType.INVALID_TABLES_COUNT_OF_TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroup.Builder;
import kitchenpos.exception.ExceptionType;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

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
