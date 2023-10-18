package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class OrderTableDaoTest {
    @Autowired
    private DataSource dataSource;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        TableGroup entity = new TableGroup();
        entity.setCreatedDate(LocalDateTime.now());
        tableGroup = tableGroupDao.save(entity);

    }

    @Test
    @DisplayName("주문 테이블을 저장한다.")
    public void save() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroup.getId());
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);

        //when
        OrderTable returnedOrderTable = orderTableDao.save(orderTable);

        //then
        assertThat(returnedOrderTable.getId()).isNotNull();
        assertThat(orderTable.getTableGroupId()).isEqualTo(returnedOrderTable.getTableGroupId());
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(returnedOrderTable.getNumberOfGuests());
        assertThat(orderTable.isEmpty()).isEqualTo(returnedOrderTable.isEmpty());
    }

    @Test
    @DisplayName("주문 테이블을 아이디로 조회한다.")
    public void findById() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroup.getId());
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        //when
        Optional<OrderTable> returnedOrderTable = orderTableDao.findById(savedOrderTable.getId());

        //then
        assertThat(returnedOrderTable).isPresent();
        assertThat(savedOrderTable.getId()).isEqualTo(returnedOrderTable.get().getId());
        assertThat(savedOrderTable.getTableGroupId()).isEqualTo(returnedOrderTable.get().getTableGroupId());
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(returnedOrderTable.get().getNumberOfGuests());
        assertThat(savedOrderTable.isEmpty()).isEqualTo(returnedOrderTable.get().isEmpty());
    }

    @Test
    @DisplayName("주문 테이블을 전체 조회한다.")
    public void findAll() {
        //given
        final int originalSize = orderTableDao.findAll().size();
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable1.setNumberOfGuests(2);
        orderTable1.setEmpty(false);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setTableGroupId(tableGroup.getId());
        orderTable2.setNumberOfGuests(3);
        orderTable2.setEmpty(true);
        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);

        //when
        List<OrderTable> returnedOrderTables = orderTableDao.findAll();

        //then
        assertThat(returnedOrderTables).hasSize(2 + originalSize);
    }

    @Test
    @DisplayName("주문 테이블을 아이디 목록으로 조회한다.")
    public void findAllByIdIn() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable1.setNumberOfGuests(2);
        orderTable1.setEmpty(false);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setTableGroupId(tableGroup.getId());
        orderTable2.setNumberOfGuests(3);
        orderTable2.setEmpty(false);
        OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        //when
        List<OrderTable> returnedOrderTables = orderTableDao.findAllByIdIn(List.of(savedOrderTable1.getId(), savedOrderTable2.getId()));

        //then
        assertThat(returnedOrderTables).hasSize(2);
    }

    @Test
    @DisplayName("주문 테이블을 테이블 그룹 아이디로 조회한다.")
    public void findAllByTableGroupId() {
        //given
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable1.setNumberOfGuests(2);
        orderTable1.setEmpty(false);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setTableGroupId(tableGroup.getId());
        orderTable2.setNumberOfGuests(3);
        orderTable2.setEmpty(false);
        orderTableDao.save(orderTable1);
        orderTableDao.save(orderTable2);

        //when
        List<OrderTable> returnedOrderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());

        //then
        assertThat(returnedOrderTables).hasSize(2);
    }
}
