package kitchenpos.domain;

import static kitchenpos.fixture.MenuBuilder.aMenu;
import static kitchenpos.fixture.MenuGroupFactory.createMenuGroup;
import static kitchenpos.fixture.ProductBuilder.aProduct;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    MenuRepository sut;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("Menu를 저장하면 MenuProduct도 함께 저장한다")
    void save() {
        // given
        Product product = productRepository.save(aProduct().build());

        Menu menu = aMenu(savedMenuGroup().getId())
                .withMenuProducts(List.of(new MenuProduct(product.getId(), 1L, product.getPrice())))
                .build();

        // when
        Menu savedMenu = sut.save(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
        List<MenuProduct> savedMenuMenuProducts = savedMenu.getMenuProducts();
        assertThat(savedMenuMenuProducts).hasSize(1);
        assertThat(savedMenuMenuProducts.get(0).getSeq()).isNotNull();
    }

    private MenuGroup savedMenuGroup() {
        return menuGroupRepository.save(createMenuGroup());
    }
}
