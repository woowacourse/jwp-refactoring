package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableGroupDaoTest extends DaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void save() throws Exception {
        TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        TableGroup foundTableGroup = tableGroupDao.findById(savedTableGroup.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedTableGroup.getId()).isEqualTo(foundTableGroup.getId());
        assertThat(savedTableGroup.getCreatedDate()).isEqualTo(foundTableGroup.getCreatedDate());
    }

    @Test
    void findById() throws Exception {
        TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        TableGroup foundTableGroup = tableGroupDao.findById(savedTableGroup.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedTableGroup.getId()).isEqualTo(foundTableGroup.getId());
        assertThat(savedTableGroup.getCreatedDate()).isEqualTo(foundTableGroup.getCreatedDate());
    }

    @Test
    void findAll() {
        tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        assertThat(tableGroupDao.findAll()).hasSize(2);
    }
}
