package kitchenpos.dao;

import static kitchenpos.constants.Constants.TEST_MENU_NAME;
import static kitchenpos.constants.Constants.TEST_MENU_PRICE;
import static kitchenpos.constants.Constants.TEST_WRONG_MENU_ID;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuDaoTest extends KitchenPosDaoTest {

    @DisplayName("MenuDao 저장 - 성공")
    @Test
    void save_Success() {
        Long menuGroupId = getCreatedMenuGroupId();

        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(TEST_MENU_PRICE);
        menu.setMenuGroupId(menuGroupId);

        Menu savedMenu = menuDao.save(menu);

        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo(TEST_MENU_NAME);
        assertThat(savedMenu.getPrice()).isEqualByComparingTo(TEST_MENU_PRICE);
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroupId);
        assertThat(savedMenu.getMenuProducts()).isNull();
    }

    @DisplayName("MenuID로 Menu 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnMenu() {
        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(TEST_MENU_PRICE);
        menu.setMenuGroupId(getCreatedMenuGroupId());
        Menu savedMenu = menuDao.save(menu);

        Menu foundMenu = menuDao.findById(savedMenu.getId())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 Menu가 없습니다."));

        assertThat(foundMenu.getId()).isEqualTo(savedMenu.getId());
        assertThat(foundMenu.getName()).isEqualTo(savedMenu.getName());
        assertThat(foundMenu.getPrice()).isEqualByComparingTo(savedMenu.getPrice());
        assertThat(foundMenu.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId());
        assertThat(foundMenu.getMenuProducts()).isEqualTo(savedMenu.getMenuProducts());
    }

    @DisplayName("MenuID로 Menu 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(TEST_MENU_PRICE);
        menu.setMenuGroupId(getCreatedMenuGroupId());
        Menu savedMenu = menuDao.save(menu);

        Optional<Menu> foundMenu = menuDao.findById(savedMenu.getId() + 1);

        assertThat(foundMenu.isPresent()).isFalse();
    }

    @DisplayName("전체 Menu 조회 - 성공")
    @Test
    void findAll_Success() {
        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(TEST_MENU_PRICE);
        menu.setMenuGroupId(getCreatedMenuGroupId());
        Menu savedMenu = menuDao.save(menu);

        List<Menu> menus = menuDao.findAll();

        assertThat(menus).isNotNull();
        assertThat(menus).isNotEmpty();

        List<Long> menuIds = menus.stream()
            .map(Menu::getId)
            .collect(Collectors.toList());

        assertThat(menuIds).contains(savedMenu.getId());
    }

    @DisplayName("ID에 해당하는 Menu 수 조회 - 성공")
    @Test
    void countByIdIn_Success() {
        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(TEST_MENU_PRICE);
        menu.setMenuGroupId(getCreatedMenuGroupId());
        Menu savedMenu = menuDao.save(menu);

        List<Long> ids = Arrays.asList(savedMenu.getId(), TEST_WRONG_MENU_ID);

        long menuCount = menuDao.countByIdIn(ids);

        assertThat(menuCount).isEqualTo(1);
    }
}
