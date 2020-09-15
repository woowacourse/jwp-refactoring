package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.TableGroup;

@SpringBootTest
class JdbcTemplateTableGroupDaoTest {
    @Autowired
    private JdbcTemplateTableGroupDao tableGroupDao;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void save() {
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
    }

    @Test
    void findById() {
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        TableGroup foundTableGroup = tableGroupDao.findById(savedTableGroup.getId()).get();

        assertThat(savedTableGroup.getId()).isEqualTo(foundTableGroup.getId());
        assertThat(savedTableGroup.getCreatedDate()).isEqualTo(foundTableGroup.getCreatedDate());
    }

    @Test
    void findAll() {
        List<TableGroup> tableGroups = tableGroupDao.findAll();
        tableGroupDao.save(tableGroup);
        List<TableGroup> savedSableGroups = tableGroupDao.findAll();

        assertThat(savedSableGroups.size()).isEqualTo(tableGroups.size() + 1);
    }
}
