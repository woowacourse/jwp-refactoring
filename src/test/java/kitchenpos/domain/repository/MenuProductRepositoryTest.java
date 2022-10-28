package kitchenpos.domain.repository;

import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴_상품을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static kitchenpos.support.TestFixtureFactory.상품을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class MenuProductRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;

    @Test
    void 메뉴_상품을_저장하면_seq가_채워진다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Product product = productRepository.save(상품을_생성한다("상품", BigDecimal.ZERO));
        MenuProduct menuProduct = 메뉴_상품을_생성한다(product.getId(), 1, product.getPrice());
        menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of(menuProduct)));

        assertThat(menuProduct.getSeq()).isNotNull();
    }

    @Test
    void id로_메뉴_상품을_조회할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Product product = productRepository.save(상품을_생성한다("상품", BigDecimal.ZERO));
        MenuProduct menuProduct = 메뉴_상품을_생성한다(product.getId(), 1, product.getPrice());
        menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of(menuProduct)));

        MenuProduct actual = menuProductRepository.findById(menuProduct.getSeq())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(menuProduct);
    }

    @Test
    void 없는_메뉴_상품_id로_조회하면_Optional_empty를_반환한다() {
        Optional<MenuProduct> actual = menuProductRepository.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_메뉴_상품을_조회할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Product product1 = productRepository.save(상품을_생성한다("상품1", BigDecimal.ZERO));
        Product product2 = productRepository.save(상품을_생성한다("상품2", BigDecimal.ZERO));
        MenuProduct menuProduct1 = 메뉴_상품을_생성한다(product1.getId(), 1, product1.getPrice());
        MenuProduct menuProduct2 = 메뉴_상품을_생성한다(product2.getId(), 2, product2.getPrice());
        menuRepository.save(메뉴를_생성한다("메뉴1", BigDecimal.ZERO, menuGroupId, List.of(menuProduct1)));
        menuRepository.save(메뉴를_생성한다("메뉴2", BigDecimal.ZERO, menuGroupId, List.of(menuProduct2)));

        List<MenuProduct> actual = menuProductRepository.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .containsExactly(menuProduct1, menuProduct2);
    }

    @Test
    void 메뉴_id에_해당하는_모든_메뉴_상품을_조회할_수_있다() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Product product1 = productRepository.save(상품을_생성한다("상품1", BigDecimal.ZERO));
        Product product2 = productRepository.save(상품을_생성한다("상품2", BigDecimal.ZERO));
        MenuProduct menuProduct1 = 메뉴_상품을_생성한다(product1.getId(), 1, product1.getPrice());
        MenuProduct menuProduct2 = 메뉴_상품을_생성한다(product2.getId(), 2, product2.getPrice());
        Menu menu1 = menuRepository.save(메뉴를_생성한다("메뉴1", BigDecimal.ZERO, menuGroupId, List.of(menuProduct1)));
        menuRepository.save(메뉴를_생성한다("메뉴2", BigDecimal.ZERO, menuGroupId, List.of(menuProduct2)));

        List<MenuProduct> actual = menuProductRepository.findAllByMenu(menu1);

        assertThat(actual).hasSize(1)
                .usingFieldByFieldElementComparator()
                .containsExactly(menuProduct1);
    }
}
