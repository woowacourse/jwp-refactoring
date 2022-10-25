package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class TableGroupDaoTest {

    private final TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupDaoTest(final DataSource dataSource) {
        this.tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @Test
    @DisplayName("테이블 단체를 저장한다")
    void save() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when
        final TableGroup saved = tableGroupDao.save(tableGroup);

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
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup saved = tableGroupDao.save(tableGroup);

        // when
        final TableGroup foundTableGroup = tableGroupDao.findById(saved.getId())
                .get();

        // then
        assertThat(foundTableGroup).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 테이블 단체를 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<TableGroup> tableGroup = tableGroupDao.findById(-1L);

        // then
        assertThat(tableGroup).isEmpty();
    }

    @Test
    @DisplayName("모든 테이블 단체를 조회한다")
    void findAll() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup saved = tableGroupDao.save(tableGroup);

        // when
        final List<TableGroup> tableGroups = tableGroupDao.findAll();

        // then
        assertAll(
                () -> assertThat(tableGroups).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(tableGroups).extracting("id")
                        .contains(saved.getId())
        );
    }
}
