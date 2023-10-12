package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MenuDaoTest extends DaoTest {

    @Autowired
    MenuDao menuDao;

    @DisplayName("MenuDao 저장 - 성공")
    @Test
    void save_Success() {
        // given && when
        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);

        // then
        assertThat(saveMenu.getId()).isNotNull();
    }

    @DisplayName("MenuID로 Menu 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnMenu() {
        // given
        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);

        // when
        Menu foundMenu = menuDao.findById(saveMenu.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 Menu가 없습니다."));

        assertThat(foundMenu.getId()).isEqualTo(saveMenu.getId());
    }

    @DisplayName("MenuID로 Menu 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        // given
        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);

        // when
        Optional<Menu> foundMenu = menuDao.findById(saveMenu.getId()+1);

        // then
        assertThat(foundMenu.isPresent()).isFalse();
    }

    @DisplayName("전체 Menu 조회 - 성공")
    @Test
    void findAll_Success() {
        // given
        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu1 = saveMenu("메뉴1", 10, saveMenuGroup, null);
        final Menu saveMenu2 = saveMenu("메뉴2", 10, saveMenuGroup, null);
        final Menu saveMenu3 = saveMenu("메뉴3", 10, saveMenuGroup, null);

        final List<Menu> menus = menuDao.findAll();

        assertThat(menus).hasSize(3);
    }

    @DisplayName("ID에 해당하는 Menu 수 조회 - 성공")
    @Test
    void countByIdIn_Success() {
        // given
        final MenuGroup saveMenuGroup = saveMenuGroup("메뉴 그룹");
        final Menu saveMenu = saveMenu("메뉴", 10, saveMenuGroup, null);
        final List<Long> ids = Arrays.asList(saveMenu.getId(), saveMenu.getId() + 1);

        // when
        final long menuCount = menuDao.countByIdIn(ids);

        //then
        assertThat(menuCount).isEqualTo(1);
    }
}
