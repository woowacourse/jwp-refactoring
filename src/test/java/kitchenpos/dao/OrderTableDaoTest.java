package kitchenpos.dao;

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

@DaoTest
public class OrderTableDaoTest {
    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("주문 테이블을 저장할 수 있다.")
    @Test
    void save() {
        OrderTable orderTable = createOrderTable(null, true, 0, null);

        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isNotNull(),
            () -> assertThat(savedOrderTable).isEqualToIgnoringGivenFields(orderTable, "id")
        );
    }

    @DisplayName("주문 테이블 아이디로 주문 테이블을 조회할 수 있다.")
    @Test
    void findById() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, true, 0, null));

        Optional<OrderTable> foundOrderTable = orderTableDao.findById(orderTable.getId());

        assertThat(foundOrderTable.get()).isEqualToComparingFieldByField(orderTable);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<OrderTable> savedOrderTables = Arrays.asList(
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null))
        );

        List<OrderTable> allOrderTables = orderTableDao.findAll();

        assertThat(allOrderTables).usingFieldByFieldElementComparator()
            .containsAll(savedOrderTables);
    }

    @DisplayName("주문 테이블 아이디 목록으로 주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAllByIdIn() {
        List<OrderTable> savedOrderTables = Arrays.asList(
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null))
        );
        List<OrderTable> selectedTables = savedOrderTables.stream().limit(2)
            .collect(Collectors.toList());

        List<OrderTable> foundOrderTables = orderTableDao
            .findAllByIdIn(
                selectedTables.stream().map(OrderTable::getId).collect(Collectors.toList()));

        assertThat(foundOrderTables).usingFieldByFieldElementComparator().isEqualTo(selectedTables);
    }

    @DisplayName("단체 지정 아이디로 주문 테이블 목록을 조회할 수 있다.")
    @Test
    void findAllByTableGroupId() {
        TableGroup tableGroup1 = tableGroupDao
            .save(createTableGroup(null, LocalDateTime.now(), null));
        TableGroup tableGroup2 = tableGroupDao
            .save(createTableGroup(null, LocalDateTime.now(), null));
        List<OrderTable> savedOrderTables = Arrays.asList(
            orderTableDao.save(createOrderTable(null, true, 0, tableGroup1.getId())),
            orderTableDao.save(createOrderTable(null, true, 0, tableGroup1.getId()))
        );
        OrderTable otherOrderTable = orderTableDao
            .save(createOrderTable(null, true, 0, tableGroup2.getId()));

        List<OrderTable> foundOrderTables = orderTableDao
            .findAllByTableGroupId(tableGroup1.getId());

        assertAll(
            () -> assertThat(foundOrderTables).usingFieldByFieldElementComparator()
                .doesNotContain(otherOrderTable),
            () -> assertThat(foundOrderTables).usingFieldByFieldElementComparator()
                .containsAll(savedOrderTables)
        );
    }
}
