package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class OrderLineItemRepositoryTest {

    @Autowired
    OrderLineItemRepository orderLineItemRepository;

    @Nested
    class findAllByOrderId {

        @Test
        void 주문_식별자로_모든_엔티티를_조회() {
            // given
            Long orderId = 4885L;
            for (int i = 0; i < 3; i++) {
                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setMenuId(1L);
                orderLineItem.setQuantity(1);
                orderLineItem.setOrderId(orderId);
                orderLineItemRepository.save(orderLineItem);
            }

            // when
            List<OrderLineItem> actual = orderLineItemRepository.findAllByOrderId(orderId);

            // then
            assertThat(actual).hasSize(3);
        }
    }
}
