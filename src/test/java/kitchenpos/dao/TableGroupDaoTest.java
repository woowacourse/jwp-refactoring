package kitchenpos.dao;

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
public class TableGroupDaoTest {
    @Autowired
    private DataSource dataSource;
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @Test
    @DisplayName("테이블 그룹을 저장한다.")
    public void save() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        //when
        TableGroup returnedTableGroup = tableGroupDao.save(tableGroup);

        //then
        assertThat(returnedTableGroup.getId()).isNotNull();
        assertThat(tableGroup.getCreatedDate()).isEqualTo(returnedTableGroup.getCreatedDate());
    }

    @Test
    @DisplayName("테이블 그룹을 조회한다.")
    public void findById() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        Long savedTableGroupId = tableGroupDao.save(tableGroup).getId();

        //when
        Optional<TableGroup> returnedTableGroup = tableGroupDao.findById(savedTableGroupId);

        //then
        assertThat(returnedTableGroup).isPresent();
        assertThat(tableGroup.getCreatedDate()).isEqualTo(returnedTableGroup.get().getCreatedDate());
    }

    @Test
    @DisplayName("모든 테이블 그룹을 조회한다.")
    public void findAll() {
        //given
        final int originalSize = tableGroupDao.findAll().size();
        TableGroup tableGroup1 = new TableGroup();
        tableGroup1.setCreatedDate(LocalDateTime.now());
        TableGroup tableGroup2 = new TableGroup();
        tableGroup2.setCreatedDate(LocalDateTime.now());
        final TableGroup savedGroup1 = tableGroupDao.save(tableGroup1);
        final TableGroup savedGroup2 = tableGroupDao.save(tableGroup2);

        //when
        List<TableGroup> returnedTableGroups = tableGroupDao.findAll();

        //then
        assertThat(returnedTableGroups).hasSize(2 + originalSize);
        assertThat(returnedTableGroups).contains(savedGroup1, savedGroup2);
    }
}
