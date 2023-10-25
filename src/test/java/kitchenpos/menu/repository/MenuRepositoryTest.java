package kitchenpos.menu.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.config.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroupEntity = MenuGroup.builder()
                .name("반려 식물")
                .build();
        menuGroup = menuGroupRepository.save(menuGroupEntity);
    }

    @Test
    void 메뉴_엔티티를_저장한다() {
        Menu menuEntity = createMenu();

        Menu savedMenu = menuRepository.save(menuEntity);

        assertThat(savedMenu.getId()).isPositive();
    }

    @Test
    void 메뉴_엔티티를_조회한다() {
        Menu menuEntity = createMenu();
        Menu savedMenu = menuRepository.save(menuEntity);

        assertThat(menuRepository.findById(savedMenu.getId())).isPresent();
    }

    @Test
    void 모든_메뉴_엔티티를_조회한다() {
        Menu menuEntityA = createMenu();
        Menu menuEntityB = createMenu();
        Menu savedMenuA = menuRepository.save(menuEntityA);
        Menu savedMenuB = menuRepository.save(menuEntityB);

        List<Menu> menus = menuRepository.findAll();

        assertThat(menus).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(savedMenuA, savedMenuB);
    }

    @Test
    void 존재하는_메뉴의_개수를_조회한다() {
        Menu menuEntityA = createMenu();
        Menu menuEntityB = createMenu();
        Menu saveMenuA = menuRepository.save(menuEntityA);
        Menu saveMenuB = menuRepository.save(menuEntityB);

        long count = menuRepository.countByIdIn(List.of(saveMenuA.getId(), saveMenuB.getId()));

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
