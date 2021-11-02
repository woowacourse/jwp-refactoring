package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("MenuGroup Dao 테스트")
class JdbcTemplateMenuGroupDaoTest extends JdbcTemplateDaoTest {

    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @DisplayName("MenuGroup을 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 MenuGroup은 저장에 성공한다.")
        @Test
        void success() {
            // given
            MenuGroup menuGroup = MenuGroup을_생성한다("인기 메뉴");

            // when
            MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menuGroup);

            // then
            assertThat(jdbcTemplateMenuGroupDao.findById(savedMenuGroup.getId())).isPresent();
            assertThat(savedMenuGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedMenuGroup);
        }

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            MenuGroup menuGroup = MenuGroup을_생성한다(null);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateMenuGroupDao.save(menuGroup))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @DisplayName("ID를 통해 MenuGroup을 조회할 때")
    @Nested
    class FindById {

        @DisplayName("ID가 존재한다면 MenuGroup 조회에 성공한다.")
        @Test
        void present() {
            // given
            MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("인기 메뉴"));

            // when
            Optional<MenuGroup> foundMenuGroup = jdbcTemplateMenuGroupDao.findById(savedMenuGroup.getId());

            // then
            assertThat(foundMenuGroup).isPresent();
            assertThat(foundMenuGroup.get()).usingRecursiveComparison()
                .isEqualTo(savedMenuGroup);
        }

        @DisplayName("ID가 존재하지 않는다면 MenuGroup 조회에 실패한다.")
        @Test
        void isNotPresent() {
            // when
            Optional<MenuGroup> foundMenuGroup = jdbcTemplateMenuGroupDao.findById(Long.MAX_VALUE);

            // then
            assertThat(foundMenuGroup).isNotPresent();
        }
    }

    @DisplayName("모든 MenuGroup을 조회한다.")
    @Test
    void findAll() {
        // given
        List<MenuGroup> beforeSavedMenuGroups = jdbcTemplateMenuGroupDao.findAll();

        beforeSavedMenuGroups.add(jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("인기 메뉴")));
        beforeSavedMenuGroups.add(jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("추천 메뉴")));
        beforeSavedMenuGroups.add(jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("주는대로 먹어")));

        // when
        List<MenuGroup> afterSavedMenuGroups = jdbcTemplateMenuGroupDao.findAll();

        // then
        assertThat(afterSavedMenuGroups).hasSize(beforeSavedMenuGroups.size());
        assertThat(afterSavedMenuGroups).usingRecursiveComparison()
            .isEqualTo(beforeSavedMenuGroups);
    }

    @DisplayName("ID가 일치하는 MenuGroup이 존재하는지 확인할 때")
    @Nested
    class CheckExistsById {

        @DisplayName("ID가 일치하는 MenuGroup이 존재하면 true가 반환된다.")
        @Test
        void returnTrue() {
            // given
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("인기 메뉴"));

            // when
            boolean exists = jdbcTemplateMenuGroupDao.existsById(menuGroup.getId());

            // then
            assertThat(exists).isTrue();
        }

        @DisplayName("ID가 일치하는 MenuGroup이 존재하지 않으면 false가 반환된다.")
        @Test
        void returnFalse() {
            // when
            boolean exists = jdbcTemplateMenuGroupDao.existsById(Long.MAX_VALUE);

            // then
            assertThat(exists).isFalse();
        }
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }
}
