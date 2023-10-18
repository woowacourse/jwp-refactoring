package kitchenpos.repository;

import kitchenpos.BaseTest;
import kitchenpos.domain.menu.*;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductName;
import kitchenpos.domain.product.ProductPrice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
class MenuRepositoryTest extends BaseTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private EntityManager em;

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
