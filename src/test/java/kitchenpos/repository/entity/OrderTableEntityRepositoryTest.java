package kitchenpos.repository.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.OrderTableFixtures;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderTableEntityRepositoryTest {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableEntityRepository orderTableEntityRepository;

    @Autowired
    public OrderTableEntityRepositoryTest(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableEntityRepository = new OrderTableEntityRepositoryImpl(orderTableRepository);
    }

    @Test
    void getById() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(OrderTableFixtures.createOrderTable());
        // when
        OrderTable foundOrderTable = orderTableEntityRepository.getById(savedOrderTable.getId());
        // then\
        assertThat(savedOrderTable).isSameAs(foundOrderTable);
    }

    @Test
    void getByIdWithInvalidId() {
        // given
        Long invalidId = 999L;
        // when & then
        assertThatThrownBy(() -> orderTableEntityRepository.getById(invalidId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAllByInIn() {
        // given
        OrderTable savedOrderTableA = orderTableRepository.save(OrderTableFixtures.createOrderTable());
        OrderTable savedOrderTableB = orderTableRepository.save(OrderTableFixtures.createOrderTable());
        // when
        List<OrderTable> orderTables = orderTableEntityRepository.getAllByIdIn(
                List.of(savedOrderTableA.getId(), savedOrderTableB.getId())
        );
        // then
        assertThat(orderTables).hasSize(2);
    }

    @Test
    void getAllByIdInWithInvalidId() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(OrderTableFixtures.createOrderTable());
        long invalidId = 999L;
        // when & then
        assertThatThrownBy(() -> orderTableEntityRepository.getAllByIdIn(List.of(savedOrderTable.getId(), invalidId)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
