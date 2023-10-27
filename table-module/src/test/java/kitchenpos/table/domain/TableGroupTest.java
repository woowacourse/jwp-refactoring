package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 그룹(TableGroup) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class TableGroupTest {

    @Nested
    class 테이블_그룹_생성_시 {

        @Test
        void 테이블이_1개_이하로_포함된다면_예외() {
            // when & then
            assertThatThrownBy(() ->
                    new TableGroup(List.of())
            ).isInstanceOf(TableGroupException.class)
                    .hasMessage("테이블 그룹에는 최소 2개 이상의 테이블이 포함되어야 합니다.");
            assertThatThrownBy(() ->
                    new TableGroup(List.of(new OrderTable(1, true)))
            ).isInstanceOf(TableGroupException.class)
                    .hasMessage("테이블 그룹에는 최소 2개 이상의 테이블이 포함되어야 합니다.");
        }

        @Test
        void 테이블이_2개_이상이면_생성된다() {
            // when & then
            assertDoesNotThrow(() ->
                    new TableGroup(List.of(
                            new OrderTable(1, true),
                            new OrderTable(2, true)
                    ))
            );
        }
    }
}
