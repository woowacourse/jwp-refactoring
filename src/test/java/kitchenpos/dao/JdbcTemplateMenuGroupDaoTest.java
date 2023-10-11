package kitchenpos.dao;

import static kitchenpos.common.fixture.MenuGroupFixture.새_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(value = "classpath:test_truncate_table.sql", executionPhase = BEFORE_TEST_METHOD)
@JdbcTest
class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    void 메뉴_그룹을_저장한다() {
        // given
        MenuGroup menuGroup = 새_메뉴_그룹();

        // when
        MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menuGroup);

        // then
        assertThat(savedMenuGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(새_메뉴_그룹());
    }

    @Test
    void ID로_메뉴_그룹을_조회한다() {
        // given
        Long menuGroupId = jdbcTemplateMenuGroupDao.save(새_메뉴_그룹()).getId();

        // when
        MenuGroup foundMenuGroupId = jdbcTemplateMenuGroupDao.findById(menuGroupId).get();

        // then
        assertThat(foundMenuGroupId).usingRecursiveComparison()
                .isEqualTo(새_메뉴_그룹(menuGroupId));
    }

    @Test
    void 전체_메뉴_그룹을_조회한다() {
        // given
        Long menuGroupId1 = jdbcTemplateMenuGroupDao.save(새_메뉴_그룹()).getId();
        Long menuGroupId2 = jdbcTemplateMenuGroupDao.save(새_메뉴_그룹()).getId();

        // when
        List<MenuGroup> menuGroups = jdbcTemplateMenuGroupDao.findAll();

        // then
        assertThat(menuGroups).usingRecursiveComparison()
                .isEqualTo(List.of(새_메뉴_그룹(menuGroupId1), 새_메뉴_그룹(menuGroupId2)));
    }

    @Test
    void 존재하는_메뉴_그룹_ID로_메뉴_그룹이_존재하는지_확인하면_true를_반환한다() {
        // given
        Long menuGroupId = jdbcTemplateMenuGroupDao.save(새_메뉴_그룹()).getId();

        // when
        boolean actual = jdbcTemplateMenuGroupDao.existsById(menuGroupId);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 존재하지_않는_메뉴_그룹_ID로_메뉴_그룹이_존재하는지_확인하면_false를_반환한다() {
        // given
        Long invalidMenuGroupId = Long.MIN_VALUE;

        // when
        boolean actual = jdbcTemplateMenuGroupDao.existsById(invalidMenuGroupId);

        // then
        assertThat(actual).isFalse();
    }
}
