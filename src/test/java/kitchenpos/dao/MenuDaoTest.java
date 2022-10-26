package kitchenpos.dao;

import static kitchenpos.support.MenuFixture.MENU_PRICE_10000;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuDaoTest extends JdbcDaoTest {

    @Test
    void 메뉴를_저장할_수_있다() {
        // given
        final long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu menu = MENU_PRICE_10000.생성(menuGroupId);

        // when
        final Menu savedMenu = menuDao.save(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    void 메뉴를_아이디로_조회할_수_있다() {
        // given
        final long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu savedMenu = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId));

        // when
        final Menu foundMenu = menuDao.findById(savedMenu.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
                () -> assertThat(foundMenu.getName()).isEqualTo("메뉴1"),
                () -> assertThat(foundMenu.getPrice().intValue()).isEqualTo(10000),
                () -> assertThat(foundMenu.getMenuGroupId()).isEqualTo(menuGroupId)
        );
    }

    @Test
    void 모든_메뉴를_조회할_수_있다() {
        // given
        final int alreadyExistCount = menuDao.findAll()
                .size();
        final long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu savedMenu = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId));

        // when
        final List<Menu> menus = menuDao.findAll();

        // then
        assertThat(menus).usingFieldByFieldElementComparator()
                .hasSize(alreadyExistCount + 1)
                .contains(savedMenu);
    }

    @Test
    void 아이디_목록으로_존재하는_메뉴의_개수를_반환한다() {
        // given
        final long menuGroupId = 메뉴그룹을_저장한다(MENU_GROUP_1.생성()).getId();
        final Menu savedMenu = 메뉴를_저장한다(MENU_PRICE_10000.생성(menuGroupId));
        final long notExistId = Long.MAX_VALUE;

        // when
        final long count = menuDao.countByIdIn(List.of(savedMenu.getId(), notExistId));

        // then
        assertThat(count).isOne();
    }
}
