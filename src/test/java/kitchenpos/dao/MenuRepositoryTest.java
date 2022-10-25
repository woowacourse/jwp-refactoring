package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kitchenpos.MenuFixtures;
import kitchenpos.domain.Menu;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuRepositoryTest {

    private MenuRepository menuRepository;

    @Autowired
    public MenuRepositoryTest(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Test
    void save() {
        // given
        Menu menu = MenuFixtures.createMenu();
        // when
        Menu savedMenu = menuRepository.save(menu);
        // then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        Menu menu = MenuFixtures.createMenu();
        Menu savedMenu = menuRepository.save(menu);
        // when
        Optional<Menu> foundMenu = menuRepository.findById(savedMenu.getId());

        // then
        assertThat(foundMenu).isPresent();
    }

    @Test
    void findAll() {
        // given
        Menu menu = MenuFixtures.createMenu();
        menuRepository.save(menu);
        // when
        List<Menu> menus = menuRepository.findAll();

        // then
        int defaultSize = 6;
        assertThat(menus).hasSize(1 + defaultSize);
    }

    @Test
    void countByIdIn() {
        // given
        Menu menu = MenuFixtures.createMenu();
        Menu savedMenu = menuRepository.save(menu);

        // when
        long count = menuRepository.countByIdIn(List.of(savedMenu.getId()));

        // then
        assertThat(count).isEqualTo(1);
    }
}
