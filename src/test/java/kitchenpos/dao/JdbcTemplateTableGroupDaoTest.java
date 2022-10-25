package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.fixture.domain.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JdbcTemplateTableGroupDaoTest extends JdbcTemplateTest{

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("테이블 그룹을 저장한다.")
        void success() {
            TableGroup tableGroup = TableGroupFixture.getTableGroup();

            TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(tableGroup);

            Long actual = savedTableGroup.getId();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private TableGroup tableGroup;

        @BeforeEach
        void setUp() {
            tableGroup = jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
        }

        @Test
        @DisplayName("아이디로 테이블 그룹을 단일 조회한다.")
        void success() {
            Long id = tableGroup.getId();

            TableGroup actual = jdbcTemplateTableGroupDao.findById(id)
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(tableGroup);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @BeforeEach
        void setUp() {
            jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
            jdbcTemplateTableGroupDao.save(TableGroupFixture.getTableGroup());
        }

        @Test
        @DisplayName("테이블 그룹 전체 목록을 조회한다.")
        void success() {
            List<TableGroup> tableGroups = jdbcTemplateTableGroupDao.findAll();

            assertThat(tableGroups).hasSize(2);
        }
    }
}
