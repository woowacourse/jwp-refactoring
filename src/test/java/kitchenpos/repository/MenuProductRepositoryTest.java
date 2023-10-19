package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.MenuProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Test
    void findAllByMenuId() {
        // given
        final MenuGroup japanese = createMenuGroup("일식");

        final Menu expected = createMenu("감자튀김", BigDecimal.valueOf(5000), japanese);
        final Menu other = createMenu("우동", BigDecimal.valueOf(6000), japanese);

        final Product potato = createProduct("감자", BigDecimal.valueOf(3000));
        final Product noodle = createProduct("면", BigDecimal.valueOf(3000));

        final MenuProduct frenchFries = createMenuProduct(expected, potato, 5000);
        final MenuProduct wooDong = createMenuProduct(other, noodle, 6000);

        expected.addMenuProducts(List.of(frenchFries));
        other.addMenuProducts(List.of(wooDong));

        em.flush();
        em.close();

        // when
        final List<MenuProduct> actual = menuProductRepository.findAllByMenuId(expected.getId());

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(frenchFries);
    }
}
