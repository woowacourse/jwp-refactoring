package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.repository.support.RepositoryTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderLineItemRepositoryTest extends RepositoryTest {

    @Autowired
    OrderLineItemRepository orderLineItemRepository;

    @Test
    void 주문의_주문_상품_개수_검색() {
        // given
        Order order = defaultOrder();

        // when
        List<OrderLineItem> actual = orderLineItemRepository.findAllByOrderId(order.getId());

        // then
        assertThat(actual).hasSize(2);
    }

}
