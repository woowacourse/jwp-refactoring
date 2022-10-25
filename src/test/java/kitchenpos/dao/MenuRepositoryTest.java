package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    void findAllFetch() {
        Product product = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));

        Menu menu = new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId());
        menuRepository.save(menu);
        menu.addMenuProducts(List.of(new MenuProduct(null, product.getId(), 1)));

        List<Menu> menus = menuRepository.findAll();
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus.get(0).getMenuProducts().size()).isEqualTo(1)
        );
    }
}
