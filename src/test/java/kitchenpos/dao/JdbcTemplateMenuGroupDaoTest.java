package kitchenpos.dao;

import static kitchenpos.common.fixture.MenuGroupFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.common.DaoTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
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
        MenuGroup menuGroup = 메뉴_그룹();

        // when
        MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menuGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedMenuGroup.getId()).isNotNull();
            softly.assertThat(savedMenuGroup).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(메뉴_그룹());
        });
    }

    @Test
    void ID로_메뉴_그룹을_조회한다() {
        // given
        Long menuGroupId = jdbcTemplateMenuGroupDao.save(메뉴_그룹()).getId();

        // when
        MenuGroup menuGroup = jdbcTemplateMenuGroupDao.findById(menuGroupId).get();

        // then
        assertThat(menuGroup).usingRecursiveComparison()
                .isEqualTo(메뉴_그룹(menuGroupId));
    }

    @Test
    void 전체_메뉴_그룹을_조회한다() {
        // given
        Long menuGroupId_A = jdbcTemplateMenuGroupDao.save(메뉴_그룹()).getId();
        Long menuGroupId_B = jdbcTemplateMenuGroupDao.save(메뉴_그룹()).getId();

        // when
        List<MenuGroup> menuGroups = jdbcTemplateMenuGroupDao.findAll();

        // then
        assertThat(menuGroups).usingRecursiveComparison()
                .isEqualTo(List.of(메뉴_그룹(menuGroupId_A), 메뉴_그룹(menuGroupId_B)));
    }

    @Test
    void 존재하는_메뉴_그룹_ID로_메뉴_그룹이_존재하는지_확인하면_true를_반환한다() {
        // given
        Long menuGroupId = jdbcTemplateMenuGroupDao.save(메뉴_그룹()).getId();

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
