package kitchenpos.dao;

import static kitchenpos.support.MenuGroupFixtures.MENU_GROUP1;
import static kitchenpos.support.MenuGroupFixtures.createAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(JdbcTemplateMenuGroupDao.class)
public class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @DisplayName("menuGroup을 저장한다.")
    @Test
    void save() {
        // given
        final MenuGroup menugroup = new MenuGroup("신메뉴");

        // when
        final MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menugroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("menuGroup 하나를 조회한다.")
    @Test
    void findById() {
        // given
        final MenuGroup expected = MENU_GROUP1.create();

        // when
        final Optional<MenuGroup> menuGroup = jdbcTemplateMenuGroupDao.findById(1L);

        // then
        assertAll(
                () -> assertThat(menuGroup.isPresent()).isTrue(),
                () -> assertThat(menuGroup.get()).usingRecursiveComparison().isEqualTo(expected)
        );
    }

    @DisplayName("menuGroup들을 조회한다.")
    @Test
    void findAll() {
        // given
        final List<MenuGroup> expected = createAll();

        // when
        final List<MenuGroup> menuGroups = jdbcTemplateMenuGroupDao.findAll();

        // then
        assertThat(menuGroups).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expected);
    }
}
