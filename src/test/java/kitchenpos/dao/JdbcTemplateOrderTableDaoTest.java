package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.fixture.TableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateOrderTableDaoTest extends JdbcTemplateTest{

    private JdbcTemplateTableGroupDao tableGroupDao;
    private JdbcTemplateOrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
    }

    @Test
    @DisplayName("데이터 베이스에 저장할 경우 id 값을 가진 엔티티로 반환한다.")
    void save() {
        tableGroupDao.save(TableFixture.getTableGroupRequest());
        final OrderTable orderTable = orderTableDao.save(TableFixture.getOrderTableRequest());
        assertThat(orderTable.getId()).isNotNull();
    }

    @Test
    @DisplayName("목록을 조회한다.")
    void list() {
        final List<OrderTable> actual = orderTableDao.findAll();
        assertThat(actual).hasSize(8);
    }
}
