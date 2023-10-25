package kitchenpos.domain.order;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.OrderException.NoOrderLineItemsException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderLineItemsTest {

    @Test
    void 주문_상품들은_비어있다면_예외가_발생한다() {
        // expected
        assertThatThrownBy(() -> new OrderLineItems(emptyList()))
                .isInstanceOf(NoOrderLineItemsException.class);
    }
}
