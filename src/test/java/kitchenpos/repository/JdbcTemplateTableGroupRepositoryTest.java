package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateTableGroupRepositoryTest {

    private final TableGroupRepository tableGroupRepository;

    @Autowired
    public JdbcTemplateTableGroupRepositoryTest(final TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Test
    void 저장한다() {
        LocalDateTime now = LocalDateTime.now();
        TableGroup tableGroup = table_group을_생성한다(now);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        Assertions.assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isEqualTo(now)
        );
    }

    @Test
    void ID로_table_group을_조회할_수_있다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        TableGroup tableGroup = table_group을_생성한다(now);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        // when
        Optional<TableGroup> foundTableGroup = tableGroupRepository.findById(savedTableGroup.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(foundTableGroup).isPresent(),
                () -> assertThat(foundTableGroup.get().getCreatedDate()).isEqualTo(now)
        );
    }

    @Test
    void 일치하는_ID가_없으면_empty를_반환한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        TableGroup tableGroup = table_group을_생성한다(now);
        tableGroupRepository.save(tableGroup);

        // when
        Optional<TableGroup> foundTableGroup = tableGroupRepository.findById(101L);

        // then
        assertThat(foundTableGroup).isEmpty();
    }

    @Test
    void table_group_목록을_조회한다() {
        // given
        LocalDateTime now = LocalDateTime.now();
        table_group을_생성한다(now);
        LocalDateTime now2 = LocalDateTime.now();
        table_group을_생성한다(now2);

        // when
        List<TableGroup> tableGroups = tableGroupRepository.findAll();

        // then
        assertThat(tableGroups).hasSize(2);
    }

    private TableGroup table_group을_생성한다(final LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }
}
