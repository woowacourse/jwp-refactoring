package kitchenpos.repository;

import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OrderTableRepositoryTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("주문 테이블을 저장할 수 있다.")
    @Test
    void save() {
        OrderTable orderTable = createOrderTable(null, true, 0, null);

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isNotNull(),
            () -> assertThat(savedOrderTable).isEqualToIgnoringGivenFields(orderTable, "id")
        );
    }

    @DisplayName("주문 테이블 아이디로 주문 테이블을 조회할 수 있다.")
    @Test
    void findById() {
        OrderTable orderTable = orderTableRepository.save(createOrderTable(null, true, 0, null));

        Optional<OrderTable> foundOrderTable = orderTableRepository.findById(orderTable.getId());

        assertThat(foundOrderTable.get()).isEqualToComparingFieldByField(orderTable);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<OrderTable> savedOrderTables = Arrays.asList(
            orderTableRepository.save(createOrderTable(null, true, 0, null)),
            orderTableRepository.save(createOrderTable(null, true, 0, null)),
            orderTableRepository.save(createOrderTable(null, true, 0, null))
        );

        List<OrderTable> allOrderTables = orderTableRepository.findAll();

        assertThat(allOrderTables).usingFieldByFieldElementComparator()
            .containsAll(savedOrderTables);
    }

    @DisplayName("주문 테이블 아이디 목록으로 주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAllByIdIn() {
        List<OrderTable> savedOrderTables = Arrays.asList(
            orderTableRepository.save(createOrderTable(null, true, 0, null)),
            orderTableRepository.save(createOrderTable(null, true, 0, null)),
            orderTableRepository.save(createOrderTable(null, true, 0, null))
        );
        List<OrderTable> selectedTables = savedOrderTables.stream().limit(2)
            .collect(Collectors.toList());

        List<OrderTable> foundOrderTables = orderTableRepository
            .findAllByIdIn(
                selectedTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        assertThat(foundOrderTables).usingFieldByFieldElementComparator().isEqualTo(selectedTables);
    }

    @DisplayName("단체 지정 아이디로 주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAllByTableGroupId() {
        TableGroup tableGroup1 = tableGroupRepository
            .save(createTableGroup(null, LocalDateTime.now()));
        TableGroup tableGroup2 = tableGroupRepository
            .save(createTableGroup(null, LocalDateTime.now()));
        List<OrderTable> savedOrderTables = Arrays.asList(
            orderTableRepository.save(createOrderTable(null, true, 0, tableGroup1.getId())),
            orderTableRepository.save(createOrderTable(null, true, 0, tableGroup1.getId()))
        );
        OrderTable otherOrderTable = orderTableRepository
            .save(createOrderTable(null, true, 0, tableGroup2.getId()));

        List<OrderTable> foundOrderTables = orderTableRepository
            .findAllByTableGroupId(tableGroup1.getId());

        assertAll(
            () -> assertThat(foundOrderTables).usingFieldByFieldElementComparator()
                .doesNotContain(otherOrderTable),
            () -> assertThat(foundOrderTables).usingFieldByFieldElementComparator()
                .containsAll(savedOrderTables)
        );
    }
}
