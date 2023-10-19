package kitchenpos.dao;

import kitchenpos.domain.table.TableGroup;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateTableGroupDaoTest {

    @Autowired
    private JdbcTemplateTableGroupDao tableGroupDao;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = TableGroupFixture.테이블그룹_생성(LocalDateTime.now(), null);
    }

    @Test
    void save() {
        // when
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // then
        assertThat(savedTableGroup.getOrderTables()).isNull();
    }

    @Test
    void findById() {
        // given
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // when
        TableGroup foundTableGroup = tableGroupDao.findById(savedTableGroup.getId()).get();

        // then
        assertThat(foundTableGroup).usingRecursiveComparison()
                .ignoringFields("orderTables")
                .isEqualTo(savedTableGroup);
    }

    @Test
    void findAll() {
        // given
        int originSize = tableGroupDao.findAll().size();

        // when
        tableGroupDao.save(tableGroup);
        List<TableGroup> foundTableGroups = tableGroupDao.findAll();

        // then
        assertThat(foundTableGroups).hasSize(originSize + 1);
    }
}
