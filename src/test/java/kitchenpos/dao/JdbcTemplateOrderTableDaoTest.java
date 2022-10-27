package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateOrderTableDaoTest extends JdbcTemplateTest{

    private TableGroupDao tableGroupDao;
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
    }

    @Test
    @DisplayName("데이터 베이스에 저장할 경우 id 값을 가진 엔티티로 반환한다.")
    void save() {
        final TableGroup tableGroup = tableGroupDao.save(getTableGroup());
        final OrderTable orderTable = orderTableDao.save(getOrderTable(tableGroup.getId()));
        assertThat(orderTable.getId()).isNotNull();
    }

    @Test
    @DisplayName("목록을 조회한다.")
    void list() {
        final List<OrderTable> actual = orderTableDao.findAll();
        assertThat(actual.size()).isEqualTo(8);
    }
}
