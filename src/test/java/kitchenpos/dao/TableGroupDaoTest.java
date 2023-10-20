package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class TableGroupDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void 테이블_그룹_엔티티를_저장한다() {
        TableGroup tableGroupEntity = createTableGroupEntity();

        TableGroup savedTableGroup = tableGroupDao.save(tableGroupEntity);

        assertThat(savedTableGroup.getId()).isPositive();
    }

    @Test
    void 테이블_그룹_엔티티를_조회한다() {
        TableGroup tableGroupEntity = createTableGroupEntity();
        TableGroup savedTableGroup = tableGroupDao.save(tableGroupEntity);

        assertThat(tableGroupDao.findById(savedTableGroup.getId())).isPresent();
    }

    @Test
    void 모든_테이블_그룹_엔티티를_조회한다() {
        TableGroup tableGroupEntityA = createTableGroupEntity();
        TableGroup tableGroupEntityB = createTableGroupEntity();
        TableGroup savedTableGroupA = tableGroupDao.save(tableGroupEntityA);
        TableGroup savedTableGroupB = tableGroupDao.save(tableGroupEntityB);

        List<TableGroup> tableGroups = tableGroupDao.findAll();

        assertThat(tableGroups).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(savedTableGroupA, savedTableGroupB);
    }

    private TableGroup createTableGroupEntity() {
        return TableGroup.builder().build();
    }
}
