package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.repository.MenuProductRepository;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

        final MenuProducts expectedMenuProducts = new MenuProducts();
        expectedMenuProducts.addAll(List.of(frenchFries));
        expected.addMenuProducts(expectedMenuProducts);

        final MenuProducts otherMenuProducts = new MenuProducts();
        otherMenuProducts.addAll(List.of(wooDong));
        other.addMenuProducts(otherMenuProducts);

        em.flush();
        em.close();

        // when
        final List<MenuProduct> actual = menuProductRepository.findAllByMenuId(expected.getId());

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(frenchFries);
    }

    @Test
    void fetchAllById() {
        // given
        final MenuGroup japanese = createMenuGroup("일식");

        final Menu expected = createMenu("감자튀김", BigDecimal.valueOf(5000), japanese);
        final Menu other = createMenu("우동", BigDecimal.valueOf(6000), japanese);

        final Product potato = createProduct("감자", BigDecimal.valueOf(3000));
        final Product noodle = createProduct("면", BigDecimal.valueOf(3000));

        final MenuProduct frenchFries = createMenuProduct(expected, potato, 5000);
        final MenuProduct wooDong = createMenuProduct(other, noodle, 6000);

        final MenuProducts expectedMenuProducts = new MenuProducts();
        expectedMenuProducts.addAll(List.of(frenchFries));
        expected.addMenuProducts(expectedMenuProducts);

        final MenuProducts otherMenuProducts = new MenuProducts();
        otherMenuProducts.addAll(List.of(wooDong));
        other.addMenuProducts(otherMenuProducts);

        em.flush();
        em.close();

        // when
        final List<MenuProduct> actual = menuProductRepository.fetchAllById(List.of(frenchFries.getSeq(), wooDong.getSeq()));

        // then
        assertAll(
                () -> assertThat(actual).containsExactly(frenchFries, wooDong),
                () -> assertThat(actual.stream()
                        .map(MenuProduct::getMenu)
                        .collect(Collectors.toList())
                ).containsExactly(expected, other),
                () -> assertThat(actual.stream()
                        .map(MenuProduct::getProduct)
                        .collect(Collectors.toList())
                ).containsExactly(potato, noodle)
        );
    }
}
