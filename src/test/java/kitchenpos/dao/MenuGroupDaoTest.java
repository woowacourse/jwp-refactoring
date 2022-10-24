package kitchenpos.dao;

import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class MenuGroupDaoTest extends JdbcDaoTest {

    @Test
    void 메뉴그룹을_저장할_수_있다() {
        // given, when
        final MenuGroup savedMenuGroup = menuGroupDao.save(MENU_GROUP_1.생성());

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    void 메뉴그룹을_아이디로_조회할_수_있다() {
        // given
        final MenuGroup savedMenuGroup = 메뉴그룹을_저장한다(MENU_GROUP_1.생성());

        // when
        final MenuGroup foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(foundMenuGroup.getName()).isEqualTo("메뉴그룹1");
    }

    @Test
    void 모든_메뉴그룹을_조회할_수_있다() {
        // given
        final int alreadyExistCount = menuGroupDao.findAll()
                .size();
        final MenuGroup savedMenuGroup = 메뉴그룹을_저장한다(MENU_GROUP_1.생성());

        // when
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        assertThat(menuGroups).usingFieldByFieldElementComparator()
                .hasSize(alreadyExistCount + 1)
                .contains(savedMenuGroup);
    }

    @Test
    void 메뉴그룹이_존재하는지_확인한다() {
        // given
        final MenuGroup savedMenuGroup = 메뉴그룹을_저장한다(MENU_GROUP_1.생성());

        // when
        final boolean exist = menuGroupDao.existsById(savedMenuGroup.getId());

        // then
        assertThat(exist).isTrue();
    }
}
