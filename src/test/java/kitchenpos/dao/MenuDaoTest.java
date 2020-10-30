package kitchenpos.dao;

import static kitchenpos.fixture.MenuFixture.MENU_FIXTURE_1;
import static kitchenpos.fixture.MenuFixture.MENU_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuDaoTest {

    @Autowired
    private MenuDao menuDao;

    @Test
    void save() {
        Menu menu = MENU_FIXTURE_1;

        Menu persistMenu = menuDao.save(menu);

        assertThat(persistMenu.getId()).isNotNull();
    }

    @Test
    void findById() {
        Menu persistMenu = menuDao.save(MENU_FIXTURE_1);

        Menu findMenu = menuDao.findById(persistMenu.getId()).get();

        assertThat(findMenu.getId()).isEqualTo(persistMenu.getId());
    }

    @Test
    void findAll() {
        menuDao.save(MENU_FIXTURE_1);
        menuDao.save(MENU_FIXTURE_2);

        List<Menu> menus = menuDao.findAll();
        List<String> menuNames = menus.stream()
            .map(Menu::getName)
            .collect(Collectors.toList());

        assertThat(menuNames).contains("소세트", "소국세트");
    }

    @Test
    void countByIdIn() {
        Long menu1Id = menuDao.save(MENU_FIXTURE_1).getId();
        Long menu2Id = menuDao.save(MENU_FIXTURE_2).getId();

        long count = menuDao.countByIdIn(Arrays.asList(menu1Id, menu2Id));

        assertThat(count).isGreaterThanOrEqualTo(count);
    }
}
