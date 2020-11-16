package kitchenpos.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import kitchenpos.config.JpaAuditingConfig;
import kitchenpos.domain.TableGroup;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class TableGroupRepositoryTest {

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Test
    void save() {
        TableGroup tableGroup = new TableGroup();
        TableGroup saved = tableGroupRepository.save(tableGroup);

        assertThat(saved).extracting(TableGroup::getId)
            .isEqualTo(1L);
    }
}
