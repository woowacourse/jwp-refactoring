package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
class OrderValidatorTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("주어진 주문 테이블 아이디에 해당하는 주문 테이블이 존재하고 비어있지 않다면 예외가 발생하지 않는다.")
    void validate() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(3, false));
        final Order order = new Order(orderTable.getId());

        em.flush();
        em.clear();

        // when & then
        assertThatCode(() -> orderValidator.validate(order))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("주어진 주문 테이블 아이디에 해당하는 주문테이블을 찾을 수 없으면 예외가 발생한다")
    void validate_invalidOrderTable() {
        // given
        final long invalidOrderTableId = -999L;
        final Order invalidOrder = new Order(invalidOrderTableId);

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주어진 주문 테이블 아이디에 해당하는 주문 테이블의 상태가 비어있으면 예외가 발생한다")
    void validate_emptyTable() {
        // given
        final OrderTable 비어있는_테이블 = orderTableRepository.save(new OrderTable(3, true));
        final Order invalidOrder = new Order(비어있는_테이블.getId());

        em.flush();
        em.clear();

        // when & then
        assertThatThrownBy(() -> orderValidator.validate(invalidOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 생성할 수 없습니다.");
    }
}
