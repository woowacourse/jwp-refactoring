package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<Menu> menus = menuDao.findAll();

        assertAll(
            () -> assertThat(menus).hasSize(2),
            () -> assertThat(menus.get(0).getId()).isEqualTo(MENU_1.getId()),
            () -> assertThat(menus.get(0).getName()).isEqualTo(MENU_1.getName()),
            () -> assertThat(menus.get(1).getId()).isEqualTo(MENU_2.getId()),
            () -> assertThat(menus.get(1).getName()).isEqualTo(MENU_2.getName())
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 메뉴가 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<Menu> menu = menuDao.findById(-1L);

        assertThat(menu).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        Menu menu = menuDao.findById(MENU_ID_1).get();

        assertAll(
            () -> assertThat(menu.getId()).isEqualTo(MENU_ID_1),
            () -> assertThat(menu.getName()).isEqualTo(MENU_NAME_1)
        );
    }

    @DisplayName("id목록으로 count 테스트")
    @Test
    void countByIdInTest() {
        long count = menuDao.countByIdIn(Arrays.asList(MENU_ID_1, MENU_ID_2));

        assertThat(count).isEqualTo(2);
    }
}