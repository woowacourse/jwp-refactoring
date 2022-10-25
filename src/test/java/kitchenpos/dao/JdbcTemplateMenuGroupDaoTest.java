package kitchenpos.dao;

import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JdbcTemplateMenuGroupDaoTest extends JdbcTemplateTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("메뉴 그룹을 저장한다.")
        void success() {
            MenuGroup menuGroup = KOREAN.getMenuGroup();

            MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menuGroup);

            Long actual = savedMenuGroup.getId();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private MenuGroup menuGroup;

        @BeforeEach
        void setUp() {
            menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
        }

        @Test
        @DisplayName("아이디로 메뉴 그룹을 단일 조회한다.")
        void success() {
            Long id = menuGroup.getId();

            MenuGroup actual = jdbcTemplateMenuGroupDao.findById(id)
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(menuGroup);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @BeforeEach
        void setUp() {
            jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
        }

        @Test
        @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
        void success() {
            List<MenuGroup> menuGroups = jdbcTemplateMenuGroupDao.findAll();

            assertThat(menuGroups).hasSize(2);
        }
    }

    @Nested
    @DisplayName("existsById 메서드는")
    class ExistsById {

        private MenuGroup menuGroup;

        @BeforeEach
        void setUp() {
            menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
        }

        @Test
        @DisplayName("메뉴 그룹 아이디가 존재하면 true를 반환한다.")
        void success_true() {
            Long id = menuGroup.getId();

            Boolean actual = jdbcTemplateMenuGroupDao.existsById(id);

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("메뉴 그룹 아이디가 존재하지 않으면 false를 반환한다.")
        void success_false() {
            Long id = Long.MAX_VALUE;

            Boolean actual = jdbcTemplateMenuGroupDao.existsById(id);

            assertThat(actual).isFalse();
        }
    }
}
