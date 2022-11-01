package kitchenpos.repository.entity;

import static org.assertj.core.api.Assertions.*;

import kitchenpos.TableGroupFixtures;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.repository.entity.TableGroupEntityRepository;
import kitchenpos.repository.entity.TableGroupEntityRepositoryImpl;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class TableGroupEntityRepositoryTest {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupEntityRepository tableGroupEntityRepository;

    @Autowired
    public TableGroupEntityRepositoryTest(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupEntityRepository = new TableGroupEntityRepositoryImpl(tableGroupRepository);
    }

    @Test
    void getById() {
        // given
        TableGroup tableGroup = tableGroupRepository.save(TableGroupFixtures.createTableGroup());
        // when
        TableGroup foundTableGroup = tableGroupEntityRepository.getById(tableGroup.getId());
        // then
        assertThat(tableGroup).isSameAs(foundTableGroup);
    }

    @Test
    void getByIdWithInvalidId() {
        // given
        Long invalidId = 999L;
        // when & then
        assertThatThrownBy(() -> tableGroupEntityRepository.getById(invalidId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
