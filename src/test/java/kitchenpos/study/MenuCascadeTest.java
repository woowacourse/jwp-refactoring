package kitchenpos.study;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuCascadeTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @Test
    void cascade() {
        //given
        Product productA = new Product(null, "nameA", BigDecimal.valueOf(1000L));
        Product productB = new Product(null, "nameB", BigDecimal.valueOf(2000L));
        productRepository.saveAll(Arrays.asList(productA, productB));

        MenuProduct menuProductA = new MenuProduct(null, null, productA.getId(), 10L);
        MenuProduct menuProductB = new MenuProduct(null, null, productB.getId(), 20L);

        List<MenuProduct> menuProducts = Arrays.asList(menuProductA, menuProductB);
        //when
        Menu menu = new Menu(null, "name", BigDecimal.valueOf(1000L), 1L, menuProducts);
//        menu.setMenuProducts(menuProducts);
        Menu result = menuRepository.save(menu);

        //then
        MenuProducts actual = result.getMenuProducts();
        Assertions.assertThat(actual.getValues()).containsAll(menuProducts);
    }
}
