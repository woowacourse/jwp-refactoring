package kitchenpos.menu.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.config.RepositoryTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MenuProductRepositoryTest {

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    private Menu menu;
    private Product product;


    @BeforeEach
    void setUp() {
        MenuGroup menuGroupEntity = MenuGroup.builder()
                .name("샐러드")
                .build();
        MenuGroup menuGroup = menuGroupRepository.save(menuGroupEntity);

        Menu menuEntity = Menu.builder()
                .name("닭가슴살")
                .price(1_000)
                .menuGroupId(menuGroup.getId())
                .build();
        menu = menuRepository.save(menuEntity);

        Product productEntity = Product.builder()
                .name("닭가슴살 볼")
                .price(3_000)
                .build();
        product = productRepository.save(productEntity);
    }

    @Test
    void 메뉴_제품_엔티티를_저장한다() {
        MenuProduct menuProductEntity = createMenuProduct();

        MenuProduct savedMenuProduct = menuProductRepository.save(menuProductEntity);

        assertThat(savedMenuProduct.getId()).isPositive();
    }

    @Test
    void 메뉴_제품_엔티티를_조회한다() {
        MenuProduct menuProductEntity = createMenuProduct();
        MenuProduct savedMenuProduct = menuProductRepository.save(menuProductEntity);

        assertThat(menuProductRepository.findById(savedMenuProduct.getId())).isPresent();
    }

    @Test
    void 모든_메뉴_제품_엔티티를_조회한다() {
        MenuProduct menuProductEntityA = createMenuProduct();
        MenuProduct menuProductEntityB = createMenuProduct();
        MenuProduct savedMenuProductA = menuProductRepository.save(menuProductEntityA);
        MenuProduct savedMenuProductB = menuProductRepository.save(menuProductEntityB);

        List<MenuProduct> menuProducts = menuProductRepository.findAll();

        assertThat(menuProducts).usingRecursiveFieldByFieldElementComparatorOnFields("seq")
                .contains(savedMenuProductA, savedMenuProductB);
    }

    @Test
    void 메뉴와_일치하는_모든_메뉴_제품_엔티티를_조회한다() {
        MenuProduct menuProductEntityA = createMenuProduct();
        MenuProduct menuProductEntityB = createMenuProduct();
        MenuProduct savedMenuProductA = menuProductRepository.save(menuProductEntityA);
        MenuProduct savedMenuProductB = menuProductRepository.save(menuProductEntityB);

        List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());

        assertThat(menuProducts).usingRecursiveFieldByFieldElementComparatorOnFields("seq")
                .contains(savedMenuProductA, savedMenuProductB);
    }

    private MenuProduct createMenuProduct() {
        return MenuProduct.builder()
                .menu(menu)
                .product(product)
                .quantity(10)
                .build();
    }
}
