package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("메뉴를 저장한다.")
    void save() {
        final MenuProduct testMenuProduct1 = new MenuProduct(product1.getId(), 1);
        final MenuProduct testMenuProduct2 = new MenuProduct(product2.getId(), 1);
        final Menu newMenu = Menu.create(
                "메뉴생성",
                BigDecimal.valueOf(3000L),
                menuGroup.getId(),
                List.of(testMenuProduct1, testMenuProduct2)
        );

        final Menu savedMenu = menuRepository.save(newMenu);

        final List<MenuProduct> savedMenuProducts = savedMenu.getMenuProducts();
        assertAll(
                () -> assertThat(savedMenu.getPrice().longValue()).isEqualTo(newMenu.getPrice().longValue()),
                () -> assertThat(savedMenu.getName()).isEqualTo(newMenu.getName()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(newMenu.getMenuGroupId()),
                () -> assertThat(savedMenuProducts).hasSize(2),
                () -> assertThat(savedMenuProducts).extracting("productId")
                        .containsExactly(product1.getId(), product2.getId()),
                () -> assertThat(savedMenuProducts).extracting("menuId")
                        .containsOnly(savedMenu.getId())
        );
    }

    @Test
    @DisplayName("id로 메뉴를 불러온다.")
    void findById() {
        final Menu findMenu = menuRepository.findById(menu.getId())
                .orElseThrow();

        assertAll(
                () -> assertThat(findMenu.getId()).isEqualTo(menu.getId()),
                () -> assertThat(findMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(findMenu.getPrice()).isEqualTo(menu.getPrice()),
                () -> assertThat(findMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                () -> assertThat(findMenu.getMenuProducts()).hasSize(menu.getMenuProducts().size())
        );
    }

    @Test
    @DisplayName("모든 메뉴를 찾는다.")
    void findAll() {
        final MenuProduct testMenuProduct1 = new MenuProduct(product1.getId(), 1);
        final MenuProduct testMenuProduct2 = new MenuProduct(product2.getId(), 1);
        final Menu newMenu = Menu.create(
                "메뉴생성",
                BigDecimal.valueOf(3000L),
                menuGroup.getId(),
                List.of(testMenuProduct1, testMenuProduct2)
        );

        final Menu savedMenu = menuRepository.save(newMenu);

        final List<Menu> menus = menuRepository.findAll();

        assertThat(menus).extracting("id").contains(menu.getId(), savedMenu.getId());
    }

    @Test
    @DisplayName("메뉴아이디 목록으로 카운트를 구한다.")
    void countByIdIn() {
        final MenuProduct testMenuProduct1 = new MenuProduct(product1.getId(), 1);
        final MenuProduct testMenuProduct2 = new MenuProduct(product2.getId(), 1);
        final Menu newMenu = Menu.create(
                "메뉴생성",
                BigDecimal.valueOf(3000L),
                menuGroup.getId(),
                List.of(testMenuProduct1, testMenuProduct2)
        );

        final Menu savedMenu = menuRepository.save(newMenu);

        final long count = menuRepository.countByIdIn(List.of(menu.getId(), savedMenu.getId()));

        assertThat(count).isEqualTo(2);
    }
}