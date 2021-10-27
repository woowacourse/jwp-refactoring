package kitchenpos.dao;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("TableGroup Dao 테스트")
class JdbcTemplateTableGroupDaoTest extends JdbcTemplateDaoTest {

    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @DisplayName("TableGroup을 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 TableGroup은 저장에 성공한다.")
        @Test
        void success() {
            // given
            TableGroup tableGroup = TableGroup을_생성한다(LocalDateTime.now());

            // when
            TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(tableGroup);

            // then
            assertThat(savedTableGroup.getId()).isNotNull();
            assertThat(savedTableGroup.getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
        }

        @DisplayName("createdDate가 Null인 경우 예외가 발생한다.")
        @Test
        void createdDateNullException() {
            // given
            TableGroup tableGroup = TableGroup을_생성한다(null);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateTableGroupDao.save(tableGroup))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @DisplayName("ID를 통해 TableGroup을 조회할 때")
    @Nested
    class FindById {

        @DisplayName("ID가 존재한다면 TableGroup 조회에 성공한다.")
        @Test
        void present() {
            // given
            TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(TableGroup을_생성한다(LocalDateTime.now()));

            // when
            Optional<TableGroup> foundTableGroup = jdbcTemplateTableGroupDao.findById(savedTableGroup.getId());

            // then
            assertThat(foundTableGroup).isPresent();
            assertThat(foundTableGroup.get()).usingRecursiveComparison()
                .isEqualTo(savedTableGroup);
        }

        @DisplayName("ID가 존재하지 않는다면 TableGroup 조회에 실패한다.")
        @Test
        void isNotPresent() {
            // when
            Optional<TableGroup> foundTableGroup = jdbcTemplateTableGroupDao.findById(Long.MAX_VALUE);

            // then
            assertThat(foundTableGroup).isNotPresent();
        }
    }

    @DisplayName("모든 TableGroup을 조회한다.")
    @Test
    void findAll() {
        // given
        List<TableGroup> beforeSavedTableGroups = jdbcTemplateTableGroupDao.findAll();

        beforeSavedTableGroups.add(jdbcTemplateTableGroupDao.save(TableGroup을_생성한다(LocalDateTime.now())));
        beforeSavedTableGroups.add(jdbcTemplateTableGroupDao.save(TableGroup을_생성한다(LocalDateTime.now())));
        beforeSavedTableGroups.add(jdbcTemplateTableGroupDao.save(TableGroup을_생성한다(LocalDateTime.now())));

        // when
        List<TableGroup> afterSavedTableGroups = jdbcTemplateTableGroupDao.findAll();

        // then
        assertThat(afterSavedTableGroups).hasSize(beforeSavedTableGroups.size());
        assertThat(afterSavedTableGroups).usingRecursiveComparison()
            .isEqualTo(beforeSavedTableGroups);
    }

    private TableGroup TableGroup을_생성한다(LocalDateTime localDateTime) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(localDateTime);

        return tableGroup;
    }
}