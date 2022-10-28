package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.ProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class MenuProductRepositoryTest {

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("메뉴 상품을 저장한다")
    void save() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final Product product = ProductFixtures.CHICKEN.create();
        final Product savedProduct = productRepository.save(product);

        final MenuProduct menuProduct = new MenuProduct(null, savedMenu, savedProduct.getId(), 2);

        // when
        final MenuProduct saved = menuProductRepository.save(menuProduct);

        // then
        assertAll(
                () -> assertThat(saved.getSeq()).isNotNull(),
                () -> assertThat(saved.getMenu()).isEqualTo(savedMenu),
                () -> assertThat(saved.getProductId()).isEqualTo(savedProduct.getId()),
                () -> assertThat(saved.getQuantity()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("존재하지 않는 메뉴로 메뉴 상품을 저장하려면 예외가 발생한다")
    void saveExceptionNotExistMenu() {
        // given
        final Product product = ProductFixtures.CHICKEN.create();
        final Product savedProduct = productRepository.save(product);

        final MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2);

        // when, then
        assertThatThrownBy(() -> menuProductRepository.save(menuProduct))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 메뉴 상품을 저장하려면 예외가 발생한다")
    void saveExceptionNotExistProduct() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final MenuProduct menuProduct = new MenuProduct(null, savedMenu, -1L, 2);

        // when, then
        assertThatThrownBy(() -> menuProductRepository.save(menuProduct))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("id로 메뉴 상품을 조회한다")
    void findById() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final Product product = ProductFixtures.CHICKEN.create();
        final Product savedProduct = productRepository.save(product);

        final MenuProduct menuProduct = new MenuProduct(null, savedMenu, savedProduct.getId(), 2);
        final MenuProduct saved = menuProductRepository.save(menuProduct);

        // when
        final MenuProduct foundMenuProduct = menuProductRepository.findById(saved.getSeq())
                .get();

        // then
        assertThat(foundMenuProduct).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 메뉴 상품을 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<MenuProduct> menuProduct = menuProductRepository.findById(-1L);

        // then
        assertThat(menuProduct).isEmpty();
    }

    @Test
    @DisplayName("메뉴 id로 모든 메뉴 상품을 조회한다")
    void findAllByMenuId() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final Product product = ProductFixtures.CHICKEN.create();
        final Product savedProduct = productRepository.save(product);

        final MenuProduct menuProduct = new MenuProduct(null, savedMenu, savedProduct.getId(), 2);
        final MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);

        // when
        final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(savedMenu.getId());

        // then
        assertAll(
                () -> assertThat(menuProducts).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(menuProducts).extracting("seq")
                        .contains(savedMenuProduct.getSeq()),
                () -> assertThat(menuProducts).extracting("menu")
                        .containsOnly(savedMenu)
        );
    }
}
