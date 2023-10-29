package kitchenpos.domain.tablegroup;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupTest {

    @Test
    void 생성자는_호출하면_tableGroup를_초기화한다() {
        // when & then
        assertThatCode(TableGroup::new).doesNotThrowAnyException();
    }
}
