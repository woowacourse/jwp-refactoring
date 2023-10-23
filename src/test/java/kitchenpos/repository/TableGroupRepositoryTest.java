package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    void 테이블_그룹_엔티티를_저장한다() {
        TableGroup tableGroupEntity = createTableGroupEntity();

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroupEntity);

        assertThat(savedTableGroup.getId()).isPositive();
    }

    @Test
    void 테이블_그룹_엔티티를_조회한다() {
        TableGroup tableGroupEntity = createTableGroupEntity();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroupEntity);

        assertThat(tableGroupRepository.findById(savedTableGroup.getId())).isPresent();
    }

    @Test
    void 모든_테이블_그룹_엔티티를_조회한다() {
        TableGroup tableGroupEntityA = createTableGroupEntity();
        TableGroup tableGroupEntityB = createTableGroupEntity();
        TableGroup savedTableGroupA = tableGroupRepository.save(tableGroupEntityA);
        TableGroup savedTableGroupB = tableGroupRepository.save(tableGroupEntityB);

        List<TableGroup> tableGroups = tableGroupRepository.findAll();

        assertThat(tableGroups).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(savedTableGroupA, savedTableGroupB);
    }

    private TableGroup createTableGroupEntity() {
        return TableGroup.builder()
                .orderTables(Collections.emptyList())
                .build();
    }
}
