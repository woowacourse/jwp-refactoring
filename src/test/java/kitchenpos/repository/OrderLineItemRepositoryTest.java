package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.OrderLineItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderLineItemRepositoryTest {

    private final OrderLineItemRepository orderLineItemRepository;

    @Autowired
    public OrderLineItemRepositoryTest(final OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @BeforeEach
    void setup() {
    }

    @Test
    void ID로_조회한다() {
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemRepository.findById(1L);

        // then
        Assertions.assertAll(
                () -> assertThat(foundOrderLineItem).isPresent(),
                () -> assertThat(foundOrderLineItem.get())
                        .usingRecursiveComparison()
                        .ignoringFields("seq", "orderId", "menuPrice")
                        .isEqualTo(new OrderLineItem(1L, "pasta", BigDecimal.valueOf(13000), 3L))
        );
    }

    @Test
    void 일치하는_ID가_없을_경우_empty를_반환한다() {
        // given
        Long notExistId = -1L;

        // when
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemRepository.findById(notExistId);

        // then
        assertThat(foundOrderLineItem).isEmpty();
    }

    @Test
    void 목록을_조회한다() {
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAll();
        assertThat(orderLineItems).hasSize(1)
                .extracting("menuId", "menuName", "quantity")
                .contains(tuple(1L, "pasta", 3L));
    }
}
