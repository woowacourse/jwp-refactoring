package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderFixture.조리_상태_주문;
import static kitchenpos.table.domain.OrderTableFixture.단체_지정_없는_빈_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private Long orderTableId;

    @BeforeEach
    void setUp() {
        orderTableId = orderTableRepository.save(단체_지정_없는_빈_주문_테이블()).getId();
    }

    @Test
    void 주문_테이블_ID로_모든_주문을_조회한다() {
        // given
        Order order = orderRepository.save(조리_상태_주문(orderTableId));

        // when
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);

        // then
        assertThat(orders).usingRecursiveComparison()
                .isEqualTo(List.of(order));
    }

    @Test
    void 주문_테이블_ID_목록으로_모든_주문을_조회한다() {
        // given
        Order order = orderRepository.save(조리_상태_주문(orderTableId));

        // when
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(List.of(orderTableId));

        // then
        assertThat(orders).usingRecursiveComparison()
                .isEqualTo(List.of(order));
    }
}
