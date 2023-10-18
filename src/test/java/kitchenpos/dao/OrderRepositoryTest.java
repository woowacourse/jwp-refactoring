package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.새로운_주문;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class OrderRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));
    }

    @Test
    void 주문을_등록하면_ID를_부여받는다() {
        Order 등록되지_않은_주문 = 새로운_주문(주문_테이블.getId(), COOKING.name(), LocalDateTime.now(), null);

        Order 등록된_주문 = orderRepository.save(등록되지_않은_주문);

        assertSoftly(softly -> {
            softly.assertThat(등록된_주문.getId()).isNotNull();
            softly.assertThat(등록된_주문).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(등록되지_않은_주문);
        });
    }

    @Test
    void 등록하는_주문에_ID가_존재하면_해당_ID의_주문으로_등록한다() {
        Order 원래_등록되어_있던_주문 = orderRepository.save(새로운_주문(주문_테이블.getId(), COOKING.name(), LocalDateTime.now(), null));
        Order 새_주문 = 새로운_주문(주문_테이블.getId(), MEAL.name(), LocalDateTime.now(), null);
        새_주문.setId(원래_등록되어_있던_주문.getId());

        Order 등록된_새_주문 = orderRepository.save(새_주문);

        assertThat(등록된_새_주문).usingRecursiveComparison()
                .isEqualTo(새_주문);
    }

    @Test
    void ID로_주문을_조회한다() {
        Order 주문 = orderRepository.save(새로운_주문(주문_테이블.getId(), COOKING.name(), LocalDateTime.now(), null));

        Order ID로_조회한_주문 = orderRepository.findById(주문.getId())
                .orElseGet(Assertions::fail);

        assertThat(ID로_조회한_주문).usingRecursiveComparison()
                .isEqualTo(주문);
    }

    @Test
    void 존재하지_않는_ID로_주문을_조회하면_Optional_empty를_반환한다() {
        Optional<Order> 존재하지_않는_ID로_조회한_주문 = orderRepository.findById(Long.MIN_VALUE);

        assertThat(존재하지_않는_ID로_조회한_주문).isEmpty();
    }

    @Test
    void 모든_주문을_조회한다() {
        Order 주문1 = orderRepository.save(새로운_주문(주문_테이블.getId(), COOKING.name(), LocalDateTime.now(), null));
        Order 주문2 = orderRepository.save(새로운_주문(주문_테이블.getId(), MEAL.name(), LocalDateTime.now(), null));

        List<Order> 모든_주문 = orderRepository.findAll();

        assertThat(모든_주문).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(주문1, 주문2);
    }

    @Test
    void 특정_테이블_ID와_목록에_있는_주문_상태를_갖는_주문의_존재여부를_반환한다() {
        orderRepository.save(새로운_주문(주문_테이블.getId(), COOKING.name(), LocalDateTime.now(), null));

        assertSoftly(softly -> {
            softly.assertThat(orderRepository.existsByOrderTableIdAndOrderStatusIn(주문_테이블.getId(), List.of(COOKING.name()))).isTrue();
            softly.assertThat(orderRepository.existsByOrderTableIdAndOrderStatusIn(Long.MIN_VALUE, List.of(COOKING.name()))).isFalse();
            softly.assertThat(orderRepository.existsByOrderTableIdAndOrderStatusIn(주문_테이블.getId(), List.of(MEAL.name()))).isFalse();
        });
    }
}
