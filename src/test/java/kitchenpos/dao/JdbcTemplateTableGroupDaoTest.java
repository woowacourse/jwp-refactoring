package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체지정 테이블그룹Dao 테스트")
class JdbcTemplateTableGroupDaoTest extends DomainDaoTest {

    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @DisplayName("단체로 지정된 테이블 그룹을 저장한다.")
    @Test
    void save() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @DisplayName("id로 단체로 지정된 테이블 그룹을 조회한다.")
    @Test
    void findById() {
        // given
        long tableGroupId = SAVE_TABLE_GROUP_RETURN_ID();

        // when
        Optional<TableGroup> findTableGroup = tableGroupDao.findById(tableGroupId);

        // then
        assertThat(findTableGroup).isPresent();
        TableGroup tableGroup = findTableGroup.get();
        assertThat(tableGroup.getId()).isEqualTo(tableGroupId);
    }

    @DisplayName("단체로 지정된 모든 테이블그룹을 조회한다.")
    @Test
    void findAll() {
        SAVE_TABLE_GROUP_RETURN_ID();

        // when
        List<TableGroup> tableGroups = tableGroupDao.findAll();

        // then
        assertThat(tableGroups).hasSize(1);
    }
}
