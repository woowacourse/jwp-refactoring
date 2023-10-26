package kitchenpos.menu.domain;

import kitchenpos.RepositoryTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("one plus one"));
        Product product = productRepository.save(new Product("pizza", BigDecimal.valueOf(14000L)));
        Menu menu = new Menu("pizza one plus one", BigDecimal.valueOf(27000L), menuGroup.getId());
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
