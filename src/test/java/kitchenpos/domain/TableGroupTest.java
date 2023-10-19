package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupTest {

    @Test
    void 테이블_그룹의_주문_테이블이_null이면_예외() {
        // when & then
        Assertions.assertThatThrownBy(() ->
                        TableGroup.builder()
                                .orderTables(null)
                                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹의_주문_테이블이_비어있으면_예외() {
        // when & then
        Assertions.assertThatThrownBy(() ->
                        TableGroup.builder()
                                .orderTables(Collections.emptyList())
                                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 테이블_그룹의_주문_테이블이_2개_미만이면_예외() {
        // when & then
        Assertions.assertThatThrownBy(() ->
                        TableGroup.builder()
                                .orderTables(List.of(
                                        ORDER_TABLE.비어있는_테이블()
                                ))
                                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
