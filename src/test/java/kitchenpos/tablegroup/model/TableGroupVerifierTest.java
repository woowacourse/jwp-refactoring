package kitchenpos.tablegroup.model;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.tablegroup.model.TableGroupVerifier;

class TableGroupVerifierTest {

    @DisplayName("그룹 지정은 테이블의 수가 2보다 커야 합니다.")
    @Test
    void validateMinimumTableCount_BelowTwoTables_ThrownException() {
        List<Long> orderTableIds = Collections.singletonList(1L);
        assertThatThrownBy(() -> TableGroupVerifier.validate(orderTableIds, 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("그룹 지정은 테이블의 수가 2보다 커야 합니다.");
    }

    @DisplayName("존재하지 않는 테이블은 그룹지정할 수 없습니다.")
    @Test
    void validate_TableExistence_ThrownException() {
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        assertThatThrownBy(() -> TableGroupVerifier.validate(orderTableIds, 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 테이블은 그룹지정할 수 없습니다.");
    }
}