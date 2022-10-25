package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kitchenpos.MenuFixtures;
import kitchenpos.domain.MenuProduct;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuProductRepositoryTest {

    private MenuProductRepository menuProductRepository;

    @Autowired
    public MenuProductRepositoryTest(MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    @Test
    void save() {
        // given
        MenuProduct product = MenuFixtures.createMenuProduct();
        // when
        MenuProduct savedProduct = menuProductRepository.save(product);
        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    void findById() {
        // given
        MenuProduct product = MenuFixtures.createMenuProduct();
        MenuProduct savedProduct = menuProductRepository.save(product);

        // when
        Optional<MenuProduct> foundMenuProduct = menuProductRepository.findBySeq(savedProduct.getSeq());

        // then
        assertThat(foundMenuProduct).isPresent();
    }

    @Test
    void findAll() {
        // given
        MenuProduct product = MenuFixtures.createMenuProduct();
        menuProductRepository.save(product);

        // when
        List<MenuProduct> menuProducts = menuProductRepository.findAll();

        // then
        int defaultSize = 6;
        assertThat(menuProducts).hasSize(1 + defaultSize);
    }

//    @Test
//    void findAllByMenuId() {
//        // given
//        long menuId = 1L;
//        MenuProduct product = MenuFixtures.createMenuProduct(menuId);
//        menuProductRepository.save(product);
//
//        // when
//        List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menuId);
//
//        // then
//        int defaultSize = 1;
//        assertThat(menuProducts).hasSize(1 + defaultSize);
//    }
}
