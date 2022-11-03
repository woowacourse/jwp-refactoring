package kitchenpos;

import java.util.List;
import kitchenpos.dao.menu.MenuGroupRepository;
import kitchenpos.dao.menu.MenuProductRepository;
import kitchenpos.dao.menu.MenuRepository;
import kitchenpos.dao.menu.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AllRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;


    @DisplayName("asdf")
    @Test
    void test1() {
        // given

        // when
        final Product product = productRepository.findById(1L).get();

        System.out.println("Product Test");
        System.out.println(product.getId());
        System.out.println(product.getPrice());
        System.out.println(product.getName());
        // then

        System.out.println("MenuGroup Test");
        final MenuGroup menuGroup = menuGroupRepository.findById(1L).get();
        System.out.println(menuGroup.getName());

        System.out.println("Menu Test");
        final Menu menu = menuRepository.findById(1L).get();
        System.out.println(menu.getPrice());
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        for (MenuProduct menuProduct : menuProducts) {
            System.out.println(menuProduct.getProductId());
        }

        System.out.println("MenuProduct Test");
        final MenuProduct menuProduct = menuProductRepository.findById(1L).get();
        System.out.println(menuProduct.getQuantity());

    }

}
