package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class MenuDaoTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroupEntity = MenuGroup.builder()
                .name("반려 식물")
                .build();
        menuGroup = menuGroupDao.save(menuGroupEntity);
    }

    @Test
    void 메뉴_엔티티를_저장한다() {
        Menu menuEntity = createMenu();

        Menu savedMenu = menuDao.save(menuEntity);

        assertThat(savedMenu.getId()).isPositive();
    }

    @Test
    void 메뉴_엔티티를_조회한다() {
        Menu menuEntity = createMenu();
        Menu savedMenu = menuDao.save(menuEntity);

        assertThat(menuDao.findById(savedMenu.getId())).isPresent();
    }

    @Test
    void 모든_메뉴_엔티티를_조회한다() {
        Menu menuEntityA = createMenu();
        Menu menuEntityB = createMenu();
        Menu savedMenuA = menuDao.save(menuEntityA);
        Menu savedMenuB = menuDao.save(menuEntityB);

        List<Menu> menus = menuDao.findAll();

        assertThat(menus).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(savedMenuA, savedMenuB);
    }

    @Test
    void 존재하는_메뉴의_개수를_조회한다() {
        Menu menuEntityA = createMenu();
        Menu menuEntityB = createMenu();
        Menu saveMenuA = menuDao.save(menuEntityA);
        Menu saveMenuB = menuDao.save(menuEntityB);

        long count = menuDao.countByIdIn(List.of(saveMenuA.getId(), saveMenuB.getId()));

        assertThat(count).isEqualTo(2);
    }

    private Menu createMenu() {
        return Menu.builder()
                .name("스투키")
                .price(10_000)
                .menuGroup(menuGroup)
                .build();
    }
}
