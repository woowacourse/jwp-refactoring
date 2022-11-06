package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupTest {

    @Test
    void tableGroup을_생성할_수_있다() {
        // when & then
        assertThatCode(TableGroup::new)
                .doesNotThrowAnyException();
    }
}
