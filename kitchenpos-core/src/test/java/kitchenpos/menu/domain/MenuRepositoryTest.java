package kitchenpos.menu.domain;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("모두 조회한다.")
    void findAllFetch() {
        Product product = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));

        List<MenuProduct> menuProducts = List.of(new MenuProduct(null, product.getId(), 1));
        Menu menu = new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId(), menuProducts);
        menuRepository.save(menu);

        List<Menu> menus = menuRepository.findAll();
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus.get(0).getMenuProducts()).hasSize(1)
        );
    }

    @Test
    @DisplayName("숫자를 센다.")
    void countByIdIn() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        Product product = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));

        List<MenuProduct> menuProducts = List.of(new MenuProduct(null, product.getId(), 1));
        Menu menu = menuRepository.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId(), menuProducts));
        assertThat(menuRepository.countByIdIn(List.of(menu.getId()))).isEqualTo(1);
    }
}
