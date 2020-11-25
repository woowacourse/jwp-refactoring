package kitchenpos.repository;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuProductFixture.createMenuProduct;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MenuProductRepositoryTest {
    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup menuGroup;
    private Product product;
    private Menu menu;

    @BeforeEach
    void setup() {
        menuGroup = menuGroupRepository.save(createMenuGroup(null, "메뉴그룹"));
        product = productRepository.save(createProduct(null, "상품", 1000L));
        menu = menuRepository.save(createMenu(null, "메뉴", 3000L, menuGroup.getId()));
    }

    @DisplayName("메뉴 상품을 저장할 수 있다.")
    @Test
    void save() {
        MenuProduct menuProduct = createMenuProduct(null, menu.getId(), product.getId(), 3L);

        MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);

        assertAll(
            () -> assertThat(savedMenuProduct.getSeq()).isNotNull(),
            () -> assertThat(savedMenuProduct).isEqualToIgnoringGivenFields(menuProduct, "seq")
        );
    }

    @DisplayName("메뉴 상품 아이디로 메뉴 상품을 조회할 수 있다.")
    @Test
    void findById() {
        MenuProduct menuProduct = menuProductRepository
            .save(createMenuProduct(null, menu.getId(), product.getId(), 3L));

        Optional<MenuProduct> foundMenuProduct = menuProductRepository.findById(menuProduct.getSeq());

        assertThat(foundMenuProduct.get()).isEqualToComparingFieldByField(menuProduct);
    }

    @DisplayName("메뉴 상품 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<MenuProduct> savedMenuProducts = Arrays.asList(
            menuProductRepository.save(createMenuProduct(null, menu.getId(), product.getId(), 3L)),
            menuProductRepository.save(createMenuProduct(null, menu.getId(), product.getId(), 3L)),
            menuProductRepository.save(createMenuProduct(null, menu.getId(), product.getId(), 3L))
        );

        List<MenuProduct> allMenuProducts = menuProductRepository.findAll();

        assertThat(allMenuProducts).usingFieldByFieldElementComparator()
            .containsAll(savedMenuProducts);
    }

    @DisplayName("메뉴에 속하는 메뉴 상품 목록을 조회할 수 있다.")
    @Test
    void findAllByMenuId() {
        Menu secondMenu = menuRepository
            .save(createMenu(null, "메뉴", 3000L, menuGroup.getId()));
        MenuProduct menuProduct = menuProductRepository
            .save(createMenuProduct(null, secondMenu.getId(), product.getId(), 3L));
        menuProductRepository.save(createMenuProduct(null, menu.getId(), product.getId(), 3L));
        menuProductRepository.save(createMenuProduct(null, menu.getId(), product.getId(), 3L));

        List<MenuProduct> menuProductsByMenuId = menuProductRepository.findAllByMenuId(secondMenu.getId());

        assertAll(
            () -> assertThat(menuProductsByMenuId).hasSize(1),
            () -> assertThat(menuProductsByMenuId).usingFieldByFieldElementComparator()
                .contains(menuProduct)
        );
    }
}
