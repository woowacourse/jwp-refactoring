package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
        TableGroup tableGroupEntity = new TableGroup();
        tableGroupEntity.setCreatedDate(LocalDateTime.now());
        tableGroupEntity.setOrderTables(null);
        tableGroup = tableGroupDao.save(tableGroupEntity);
    }

    @Test
    void 주문_테이블_엔티티를_저장한다() {
        OrderTable orderTableEntity = createOrderTableEntity();

        OrderTable saveOrderTable = orderTableDao.save(orderTableEntity);

        assertThat(saveOrderTable.getId()).isPositive();
    }

    @Test
    void 주문_테이블_엔티티를_조회한다() {
        OrderTable orderTableEntity = createOrderTableEntity();
        OrderTable saveOrderTable = orderTableDao.save(orderTableEntity);

        assertThat(orderTableDao.findById(saveOrderTable.getId())).isPresent();
    }

    @Test
    void 모든_주문_테이블_엔티티를_조회한다() {
        OrderTable orderTableEntityA = createOrderTableEntity();
        OrderTable orderTableEntityB = createOrderTableEntity();
        OrderTable saveOrderTableA = orderTableDao.save(orderTableEntityA);
        OrderTable saveOrderTableB = orderTableDao.save(orderTableEntityB);

        List<OrderTable> orderTables = orderTableDao.findAll();

        assertThat(orderTables).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(saveOrderTableA, saveOrderTableB);
    }

    @Test
    void ID에_해당하는_주문_테이블_엔티티를_조회한다() {
        OrderTable orderTableEntityA = createOrderTableEntity();
        OrderTable orderTableEntityB = createOrderTableEntity();
        OrderTable saveOrderTableA = orderTableDao.save(orderTableEntityA);
        OrderTable saveOrderTableB = orderTableDao.save(orderTableEntityB);

        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(saveOrderTableA.getId()));

        assertThat(orderTables).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(saveOrderTableA)
                .doesNotContain(saveOrderTableB);
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
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(tableGroup.getId());
        orderTable.setNumberOfGuests(10);
        return orderTable;
    }
}
