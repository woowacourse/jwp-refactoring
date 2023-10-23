package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupName;
import kitchenpos.domain.menu.MenuName;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductQuantity;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductName;
import kitchenpos.domain.product.ProductPrice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

class MenuRepositoryTest extends RepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴를_가져올_때_메뉴_상품을_같이_조회한다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(new MenuGroupName("one plus one")));
        Product product = productRepository.save(new Product(new ProductName("pizza"), new ProductPrice(BigDecimal.valueOf(14000L))));
        Menu menu = new Menu(new MenuName("pizza one plus one"), new MenuPrice(BigDecimal.valueOf(27000L)), menuGroup);
        MenuProduct menuProduct = new MenuProduct(product, new MenuProductQuantity(2L));
        menu.addMenuProducts(List.of(menuProduct));
        menuRepository.save(menu);
        em.flush();
        em.clear();

        // when
        List<Menu> menus = menuRepository.findAllWithMenuProducts();
        for (Menu findMenu : menus) {
            em.detach(findMenu);
        }

        // then
        for (Menu findMenu : menus) {
            Assertions.assertThatNoException()
                    .isThrownBy(() -> findMenu.getMenuProducts().get(0).getQuantity());
        }
    }
}
