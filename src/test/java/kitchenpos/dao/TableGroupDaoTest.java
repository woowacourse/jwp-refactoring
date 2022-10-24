package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.BeanAssembler;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class TableGroupDaoTest {

    private TableGroupDao tableGroupDao;

    @Autowired
    public TableGroupDaoTest(DataSource dataSource) {
        tableGroupDao = BeanAssembler.createTableGroupDao(dataSource);
    }

    @Test
    void save() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(new OrderTable(1L, 3, true)));
        // when
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(new OrderTable(1L, 3, true)));
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // when
        Optional<TableGroup> foundTableGroup = tableGroupDao.findById(savedTableGroup.getId());

        // then
        assertThat(foundTableGroup).isPresent();
    }

    @Test
    void findAll() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(new OrderTable(1L, 3, true)));
        tableGroupDao.save(tableGroup);

        // when
        List<TableGroup> tableGroups = tableGroupDao.findAll();

        // then
        assertThat(tableGroups).hasSize(1);
    }
}
