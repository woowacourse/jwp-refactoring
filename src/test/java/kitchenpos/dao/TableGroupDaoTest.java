package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class TableGroupDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void 테이블_그룹_엔티티를_저장한다() {
        TableGroup tableGroupEntity = createTableGroupEntity();

        TableGroup saveTableGroup = tableGroupDao.save(tableGroupEntity);

        assertThat(saveTableGroup.getId()).isPositive();
    }

    @Test
    void 테이블_그룹_엔티티를_조회한다() {
        TableGroup tableGroupEntity = createTableGroupEntity();
        TableGroup saveTableGroup = tableGroupDao.save(tableGroupEntity);

        assertThat(tableGroupDao.findById(saveTableGroup.getId())).isPresent();
    }

    @Test
    void 모든_테이블_그룹_엔티티를_조회한다() {
        TableGroup tableGroupEntityA = createTableGroupEntity();
        TableGroup tableGroupEntityB = createTableGroupEntity();
        TableGroup saveTableGroupA = tableGroupDao.save(tableGroupEntityA);
        TableGroup saveTableGroupB = tableGroupDao.save(tableGroupEntityB);

        List<TableGroup> tableGroups = tableGroupDao.findAll();

        assertThat(tableGroups).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(saveTableGroupA, saveTableGroupB);
    }

    private TableGroup createTableGroupEntity() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(null);
        return tableGroup;
    }
}
