package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class OrderTableDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        TableGroup tableGroupEntity = TableGroup.builder().build();
        tableGroup = tableGroupDao.save(tableGroupEntity);
    }

    @Test
    void 주문_테이블_엔티티를_저장한다() {
        OrderTable orderTableEntity = createOrderTableEntity();

        OrderTable savedOrderTable = orderTableDao.save(orderTableEntity);

        assertThat(savedOrderTable.getId()).isPositive();
    }

    @Test
    void 주문_테이블_엔티티를_조회한다() {
        OrderTable orderTableEntity = createOrderTableEntity();
        OrderTable savedOrderTable = orderTableDao.save(orderTableEntity);

        assertThat(orderTableDao.findById(savedOrderTable.getId())).isPresent();
    }

    @Test
    void 모든_주문_테이블_엔티티를_조회한다() {
        OrderTable orderTableEntityA = createOrderTableEntity();
        OrderTable orderTableEntityB = createOrderTableEntity();
        OrderTable savedOrderTableA = orderTableDao.save(orderTableEntityA);
        OrderTable savedOrderTableB = orderTableDao.save(orderTableEntityB);

        List<OrderTable> orderTables = orderTableDao.findAll();

        assertThat(orderTables).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(savedOrderTableA, savedOrderTableB);
    }

    @Test
    void ID에_해당하는_주문_테이블_엔티티를_조회한다() {
        OrderTable orderTableEntityA = createOrderTableEntity();
        OrderTable orderTableEntityB = createOrderTableEntity();
        OrderTable savedOrderTableA = orderTableDao.save(orderTableEntityA);
        OrderTable savedOrderTableB = orderTableDao.save(orderTableEntityB);

        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(savedOrderTableA.getId()));

        assertThat(orderTables).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(savedOrderTableA)
                .doesNotContain(savedOrderTableB);
    }

    @Test
    void 테이블_그룹_ID에_해당하는_주문_테이블_엔티티를_조회한다() {
        OrderTable orderTableEntityA = createOrderTableEntity();
        OrderTable orderTableEntityB = createOrderTableEntity();
        OrderTable saveOrderTableA = orderTableDao.save(orderTableEntityA);
        OrderTable saveOrderTableB = orderTableDao.save(orderTableEntityB);

        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());

        assertThat(orderTables).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(saveOrderTableA, saveOrderTableB);
    }

    private OrderTable createOrderTableEntity() {
        return OrderTable.builder()
                .empty(false)
                .tableGroup(tableGroup)
                .numberOfGuests(10)
                .build();
    }
}
