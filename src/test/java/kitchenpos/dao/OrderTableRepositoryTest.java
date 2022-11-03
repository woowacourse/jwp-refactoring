package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.fixtures.TableGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("주문 테이블을 저장한다")
    void save() {
        // given
        final OrderTable orderTable = new OrderTable(null, null, 2, false);

        // when
        final OrderTable saved = orderTableRepository.save(orderTable);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(saved.isEmpty()).isFalse(),
                () -> assertThat(saved.getTableGroup()).isNull()
        );
    }

    @Test
    @DisplayName("id로 주문 테이블을 조회한다")
    void findById() {
        // given
        final OrderTable orderTable = new OrderTable(null, null, 2, false);
        final OrderTable saved = orderTableRepository.save(orderTable);

        // when
        final OrderTable foundOrderTable = orderTableRepository.findById(saved.getId())
                .get();

        // then
        assertThat(foundOrderTable).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 주문 테이블을 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<OrderTable> orderTable = orderTableRepository.findById(-1L);

        // then
        assertThat(orderTable).isEmpty();
    }

    @Test
    @DisplayName("모든 주문 테이블을 조회한다")
    void findAll() {
        // given
        final OrderTable orderTable = new OrderTable(null, null, 2, false);
        final OrderTable saved = orderTableRepository.save(orderTable);

        // when
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        // then
        assertAll(
                () -> assertThat(orderTables).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orderTables).extracting("id")
                        .contains(saved.getId())
        );
    }

    @Test
    @DisplayName("id에 해당 하는 모든 주문 테이블을 조회한다")
    void findAllByIdIn() {
        // given
        final OrderTable orderTable = new OrderTable(null, null, 2, false);
        final OrderTable saved = orderTableRepository.save(orderTable);

        // when
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
                Collections.singletonList(saved.getId()));

        // then
        assertAll(
                () -> assertThat(orderTables).hasSize(1),
                () -> assertThat(orderTables).usingRecursiveFieldByFieldElementComparator()
                        .hasSize(1)
                        .extracting("id")
                        .containsExactly(saved.getId())
        );
    }

    @Test
    @DisplayName("테이블 단체 id에 해당 하는 모든 주문 테이블을 조회한다")
    void findAllByTableGroupId() {
        // given
        final TableGroup tableGroup = TableGroupFixtures.create();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        final OrderTable orderTable = new OrderTable(null, savedTableGroup, 2, false);
        final OrderTable saved = orderTableRepository.save(orderTable);

        // when
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(savedTableGroup.getId());

        // then
        assertAll(
                () -> assertThat(orderTables).hasSize(1),
                () -> assertThat(orderTables).usingRecursiveFieldByFieldElementComparator()
                        .hasSize(1)
                        .extracting("id")
                        .containsExactly(saved.getId())
        );
    }
}
