package kitchenpos.dao;

import static kitchenpos.common.MenuGroupFixtures.MENU_GROUP1_REQUEST;
import static kitchenpos.common.MenuGroupFixtures.MENU_GROUP2_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private DataSource dataSource;

    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        this.menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("MenuGroup을 영속화한다.")
    void saveMenuGroup() {
        // given
        MenuGroup menuGroup = MENU_GROUP1_REQUEST();

        // when
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedMenuGroup.getId()).isNotNull();
            softly.assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
        });
    }

    @Nested
    @DisplayName("MenuGroupId에 해당하는 MenuGroup이 존재하는지 판단 시")
    class ExistsById {

        @Test
        @DisplayName("존재하면 true를 반환한다.")
        void exist() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(MENU_GROUP1_REQUEST());

            // when & then
            assertThat(menuGroupDao.existsById(savedMenuGroup.getId())).isTrue();
        }

        @Test
        @DisplayName("존재하면 true를 반환한다.")
        void notExist() {
            // given
            final Long notExistId = -1L;

            // when & then
            assertThat(menuGroupDao.existsById(notExistId)).isFalse();
        }
    }

    @Test
    @DisplayName("MenuGroup 목록을 조회한다.")
    void findAll() {
        // given
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("truncate table menu_group");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        MenuGroup menuGroup1 = MENU_GROUP1_REQUEST();
        MenuGroup menuGroup2 = MENU_GROUP2_REQUEST();
        MenuGroup savedMenuGroup1 = menuGroupDao.save(menuGroup1);
        MenuGroup savedMenuGroup2 = menuGroupDao.save(menuGroup2);
        List<MenuGroup> expected = List.of(savedMenuGroup1, savedMenuGroup2);

        // when
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        assertThat(menuGroups).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expected);
    }
}
