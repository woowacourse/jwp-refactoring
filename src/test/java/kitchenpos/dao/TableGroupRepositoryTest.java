package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("테이블 단체를 저장한다")
    void save() {
        // given
        final TableGroup tableGroup = new TableGroup(null, LocalDateTime.now());

        // when
        final TableGroup saved = tableGroupRepository.save(tableGroup);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now())
        );
    }

    @Test
    @DisplayName("id로 테이블 단체를 조회한다")
    void findById() {
        // given
        final TableGroup tableGroup = new TableGroup(null, LocalDateTime.now());
        final TableGroup saved = tableGroupRepository.save(tableGroup);

        // when
        final TableGroup foundTableGroup = tableGroupRepository.findById(saved.getId())
                .get();

        // then
        assertThat(foundTableGroup).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 테이블 단체를 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<TableGroup> tableGroup = tableGroupRepository.findById(-1L);

        // then
        assertThat(tableGroup).isEmpty();
    }

    @Test
    @DisplayName("모든 테이블 단체를 조회한다")
    void findAll() {
        // given
        final TableGroup tableGroup = new TableGroup(null, LocalDateTime.now());
        final TableGroup saved = tableGroupRepository.save(tableGroup);

        // when
        final List<TableGroup> tableGroups = tableGroupRepository.findAll();

        // then
        assertAll(
                () -> assertThat(tableGroups).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(tableGroups).extracting("id")
                        .contains(saved.getId())
        );
    }
}
