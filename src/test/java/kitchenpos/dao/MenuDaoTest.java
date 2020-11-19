package kitchenpos.dao;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
public class MenuDaoTest {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuGroup menuGroup;

    @BeforeEach
    void setup() {
        menuGroup = menuGroupDao.save(createMenuGroup(null, "2+1메뉴"));
    }

    @DisplayName("메뉴를 저장할 수 있다.")
    @Test
    void save() {
        Menu menu = createMenu(null, "메뉴", 0L, menuGroup.getId());

        Menu savedMenu = menuDao.save(menu);

        assertAll(
            () -> assertThat(savedMenu).isNotNull(),
            () -> assertThat(savedMenu.getId()).isNotNull(),
            () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
            () -> assertThat(savedMenu.getPrice().longValue())
                .isEqualTo(menu.getPrice().longValue()),
            () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId())
        );
    }

    @DisplayName("메뉴 아이디로 메뉴를 조회할 수 있다.")
    @Test
    void findById() {
        Menu menu = menuDao
            .save(createMenu(null, "메뉴", 0L, menuGroup.getId()));

        Optional<Menu> foundMenu = menuDao.findById(menu.getId());

        assertThat(foundMenu.get()).isEqualToComparingFieldByField(menu);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<Menu> savedMenus = Arrays.asList(
            menuDao.save(createMenu(null, "메뉴1", 0L, menuGroup.getId())),
            menuDao.save(createMenu(null, "메뉴2", 0L, menuGroup.getId())),
            menuDao.save(createMenu(null, "메뉴3", 0L, menuGroup.getId()))
        );

        List<Menu> allMenus = menuDao.findAll();

        assertThat(allMenus).usingFieldByFieldElementComparator().containsAll(savedMenus);
    }

    @DisplayName("메뉴 아이디 목록으로 개수를 셀 수 있다.")
    @Test
    void countByIdIn() {
        List<Menu> savedMenus = Arrays.asList(
            menuDao.save(createMenu(null, "메뉴1", 0L, menuGroup.getId())),
            menuDao.save(createMenu(null, "메뉴2", 0L, menuGroup.getId())),
            menuDao.save(createMenu(null, "메뉴3", 0L, menuGroup.getId()))
        );

        Long menuCount = menuDao
            .countByIdIn(savedMenus.stream().map(Menu::getId).collect(Collectors.toList()));

        assertThat(menuCount).isEqualTo(savedMenus.size());
    }
}
