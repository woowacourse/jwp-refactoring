package kitchenpos.application;

import static kitchenpos.fixture.MenuTestFixture.떡볶이;
import static kitchenpos.fixture.ProductFixture.불맛_떡볶이;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Test
    void 메뉴_정상_생성() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(불맛_떡볶이.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록_생성(product);

        Menu menu = 떡볶이.toEntity(menuGroup.getId(), menuProducts);

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        Optional<Menu> actual = menuDao.findById(savedMenu.getId());
        assertThat(actual).isNotEmpty();
    }

    private List<MenuProduct> 메뉴_상품_목록_생성(final Product... products) {
        return Arrays.stream(products)
                .map(this::메뉴_상품_생성)
                .collect(Collectors.toList());
    }

    private MenuProduct 메뉴_상품_생성(final Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(null);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(3);
        return menuProduct;
    }
}
