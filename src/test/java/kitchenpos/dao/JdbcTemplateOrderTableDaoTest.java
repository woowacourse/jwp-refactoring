package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class JdbcTemplateOrderTableDaoTest {
    private static final String DELETE_ORDER_TABLE = "delete from order_table where id in (:ids)";
    private static final String INSERT_TABLE_GROUP = "insert into table_group (id, created_date) " +
            "values (:id, :created_date)";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    private List<Long> orderTableIds;

    @BeforeEach
    void setUp() {
        orderTableIds = new ArrayList<>();
    }

    @DisplayName("신규 주문 테이블 저장")
    @Test
    void saveTest() {
        OrderTable orderTable = createOrderTable(0, true);

        OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);
        orderTableIds.add(savedOrderTable.getId());

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(savedOrderTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("기존 주문 테이블 저장 시 정보 업데이트")
    @Test
    void saveExistOrderTableTest() {
        OrderTable orderTable = createOrderTable(0, true);
        OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);
        savedOrderTable.setNumberOfGuests(10);
        savedOrderTable.setEmpty(false);

        OrderTable updateOrderTable = jdbcTemplateOrderTableDao.save(savedOrderTable);
        orderTableIds.add(updateOrderTable.getId());

        assertAll(
                () -> assertThat(updateOrderTable.getId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(updateOrderTable.getNumberOfGuests()).isEqualTo(10),
                () -> assertThat(updateOrderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("아이디에 맞는 주문 테이블 반환")
    @Test
    void findByIdTest() {
        OrderTable orderTable = createOrderTable(0, true);
        OrderTable savedOrderTable = jdbcTemplateOrderTableDao.save(orderTable);

        OrderTable findOrderTable = jdbcTemplateOrderTableDao.findById(savedOrderTable.getId()).get();
        orderTableIds.add(findOrderTable.getId());

        assertAll(
                () -> assertThat(findOrderTable.getId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(findOrderTable.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests()),
                () -> assertThat(findOrderTable.isEmpty()).isEqualTo(savedOrderTable.isEmpty())
        );
    }

    @DisplayName("잘못된 아이디 입력 시 빈 객체 반환")
    @Test
    void findByIdWithInvalidIdTest() {
        Optional<OrderTable> findOrderTable = jdbcTemplateOrderTableDao.findById(0L);

        assertThat(findOrderTable).isEqualTo(Optional.empty());
    }

    @DisplayName("모든 주문 테이블 반환")
    @Test
    void findAllTest() {
        OrderTable firstOrderTable = createOrderTable(0, true);
        OrderTable secondOrderTable = createOrderTable(5, false);
        jdbcTemplateOrderTableDao.save(firstOrderTable);
        jdbcTemplateOrderTableDao.save(secondOrderTable);

        List<OrderTable> allOrderTables = jdbcTemplateOrderTableDao.findAll();
        allOrderTables.forEach(orderTable -> orderTableIds.add(orderTable.getId()));

        assertThat(allOrderTables).hasSize(2);
    }

    @DisplayName("입력한 아이디에 해당하는 주문 테이블만 반환")
    @Test
    void findAllByIdInTest() {
        OrderTable firstOrderTable = createOrderTable(0, true);
        OrderTable secondOrderTable = createOrderTable(5, false);
        OrderTable thirdOrderTable = createOrderTable(10, false);
        OrderTable savedFirstOrderTable = jdbcTemplateOrderTableDao.save(firstOrderTable);
        OrderTable savedSecondOrderTable = jdbcTemplateOrderTableDao.save(secondOrderTable);
        OrderTable savedThirdOrderTable = jdbcTemplateOrderTableDao.save(thirdOrderTable);

        List<Long> ids = Arrays.asList(savedFirstOrderTable.getId(), savedSecondOrderTable.getId());
        List<OrderTable> selectedOrderTables = jdbcTemplateOrderTableDao.findAllByIdIn(ids);
        selectedOrderTables.forEach(orderTable -> orderTableIds.add(orderTable.getId()));
        orderTableIds.add(savedThirdOrderTable.getId());

        assertThat(selectedOrderTables).hasSize(2);
    }

    @DisplayName("단체로 지정된 테이블만 반환")
    @Test
    void findAllByTableGroupIdTest() {
        createTableGroup();
        OrderTable firstOrderTable = createOrderTable(1L, 0, true);
        OrderTable secondOrderTable = createOrderTable(1L, 5, false);
        OrderTable thirdOrderTable = createOrderTable(10, false);
        jdbcTemplateOrderTableDao.save(firstOrderTable);
        jdbcTemplateOrderTableDao.save(secondOrderTable);
        OrderTable savedThirdOrderTable = jdbcTemplateOrderTableDao.save(thirdOrderTable);

        List<OrderTable> orderTablesInTableGroup = jdbcTemplateOrderTableDao.findAllByTableGroupId(1L);
        orderTablesInTableGroup.forEach(orderTable -> orderTableIds.add(orderTable.getId()));
        orderTableIds.add(savedThirdOrderTable.getId());

        assertThat(orderTablesInTableGroup).hasSize(2);
    }

    private void createTableGroup() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("created_date", LocalDateTime.now());
        namedParameterJdbcTemplate.update(INSERT_TABLE_GROUP, params);
    }

    private OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        return createOrderTable(null, numberOfGuests, empty);
    }

    private OrderTable createOrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    @AfterEach
    void tearDown() {
        Map<String, Object> params = Collections.singletonMap("ids", orderTableIds);
        namedParameterJdbcTemplate.update(DELETE_ORDER_TABLE, params);
    }
}