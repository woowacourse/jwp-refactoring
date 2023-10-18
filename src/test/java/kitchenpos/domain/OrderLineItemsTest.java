package kitchenpos.domain;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderLineItemsTest {

    @Test
    void 생성에_성공한다() {
        // when && then
        assertThatNoException()
            .isThrownBy(() -> new OrderLineItems(List.of(new OrderLineItem(1L, 1L, 2))));
    }

    @Test
    void 빈_주문항목을_생성할_수_없다() {
        // when && then
        assertThatThrownBy(() -> new OrderLineItems(emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("주문 항목이 존재하지 않습니다.");
    }
}
