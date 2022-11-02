package kitchenpos.domain;

import static kitchenpos.DomainFixture.createOrderTable;
import static kitchenpos.DomainFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.dto.TableGroupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("두 개 미만의 테이블인 경우 예외가 발생한다")
    void tableGroup_underTwoTables_throwException() {
        // given
        final OrderTable table = createOrderTable(3, true);

        // when & then
        assertThatThrownBy(() -> new TableGroupDto(createTableGroup(table)).toEntity())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
