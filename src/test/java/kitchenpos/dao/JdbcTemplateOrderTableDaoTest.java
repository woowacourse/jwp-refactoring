package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문테이블Dao 테스트")
class JdbcTemplateOrderTableDaoTest extends DaoTest {

    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
    }

    @DisplayName("주문테이블을 저장한다.")
    @Test
    void save() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(false);

        // when
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(10);
        assertThat(savedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("id로 주문테이블을 조회한다.")
    @Test
    void findById() {
        // given
        long orderTableId = SAVE_ORDER_TABLE();

        // when
        Optional<OrderTable> findOrderTable = orderTableDao.findById(orderTableId);

        // then
        assertThat(findOrderTable).isPresent();
        OrderTable orderTable = findOrderTable.get();
        assertThat(orderTable.getId()).isEqualTo(orderTableId);
    }

    @DisplayName("모든 주문테이블을 조회한다.")
    @Test
    void findAll() {
        // given
        SAVE_ORDER_TABLE();

        // when
        List<OrderTable> orderTables = orderTableDao.findAll();

        // then
        // 초기화를 통해 등록된 메뉴 8개 + 새로 추가한 메뉴 1개
        assertThat(orderTables).hasSize(8 + 1);
    }

    @DisplayName("id에 속하는 주문테이블을 조회한다.")
    @Test
    void findAllByIdIn() {
        // given
        long orderTableId1 = SAVE_ORDER_TABLE();
        long orderTableId2 = SAVE_ORDER_TABLE();

        // when
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(Arrays.asList(orderTableId1, orderTableId2));

        // then
        assertThat(orderTables).hasSize(2);
        assertThat(orderTables).extracting("id")
                .containsExactly(orderTableId1, orderTableId2);
    }

    @DisplayName("tableGroupId에 해당하는 주문테이블을 조회한다.")
    @Test
    void findAllByTableGroupId() {
        // given
        long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(tableGroupId);

        orderTableDao.save(orderTable);

        // when
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        // then
        assertThat(orderTables).hasSize(1);
        assertThat(orderTables).extracting("tableGroupId")
                .containsExactly(tableGroupId);
    }

    private long SAVE_ORDER_TABLE() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(false);

        // when
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return savedOrderTable.getId();
    }
}
