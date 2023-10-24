package kitchenpos.repository;

import static kitchenpos.domain.table.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void OrderTableRepositoryTest() {
        // given
        OrderTable orderTable = new OrderTable(단체_지정(), 1, false);

        // when
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // then
        assertThat(savedOrderTable).isEqualTo(orderTable);
    }
}
