package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.DomainFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MenuProductRepositoryTest {
    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("메뉴 아이디에 해당하는 메뉴 상품을 조회")
    @Test
    void findAllByMenuIdsTest() {
        final Product product = productRepository.save(createProduct("test", BigDecimal.valueOf(1_000)));
        final MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("test menu group"));
        final Menu firstMenu = menuRepository.save(
                createMenu(menuGroup.getId(), "first menu", BigDecimal.valueOf(1_000))
        );
        final Menu secondMenu = menuRepository.save(
                createMenu(menuGroup.getId(), "first menu", BigDecimal.valueOf(1_000))
        );
        final Menu thirdMenu = menuRepository.save(
                createMenu(menuGroup.getId(), "first menu", BigDecimal.valueOf(1_000))
        );
        menuProductRepository.save(createMenuProduct(firstMenu.getId(), product.getId(), 1));
        menuProductRepository.save(createMenuProduct(secondMenu.getId(), product.getId(), 2));
        menuProductRepository.save(createMenuProduct(thirdMenu.getId(), product.getId(), 3));

        final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuIds(
                Arrays.asList(firstMenu.getId(), thirdMenu.getId())
        );

        assertThat(menuProducts).hasSize(2);
    }
}
