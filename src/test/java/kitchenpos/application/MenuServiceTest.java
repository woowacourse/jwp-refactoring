package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.support.DataSupport;
import kitchenpos.support.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    private static final int PRICE = 3500;

    @Autowired
    private MenuService menuService;
    @Autowired
    private DataSupport dataSupport;

    private MenuGroup savedMenuGroup;
    private List<Product> savedProducts;

    @BeforeEach
    void saveData() {
        savedMenuGroup = dataSupport.saveMenuGroup("추천 메뉴");

        final Product savedProduct = dataSupport.saveProduct("치킨마요", PRICE);
        savedProducts = Arrays.asList(savedProduct);
    }

    @DisplayName("새로운 메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given, when
        final Menu request = RequestBuilder.ofMenu(savedMenuGroup, savedProducts, PRICE);
        final Menu savedMenu = menuService.create(request);

        // then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @DisplayName("메뉴를 등록할 때 상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifProductNotFound() {
        // given
        final Product unsavedProduct = new Product();
        unsavedProduct.setId(0L);
        final List<Product> unsavedProducts = Arrays.asList(unsavedProduct);

        // when, then
        final Menu request = RequestBuilder.ofMenu(savedMenuGroup, unsavedProducts, PRICE);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴를 등록할 때 가격을 입력하지 않으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifNoPrice() {
        final Menu request = RequestBuilder.ofMenu(savedMenuGroup, savedProducts, 0);
        request.setPrice(null);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴를 등록할 때 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifPriceUnder0() {
        final Menu request = RequestBuilder.ofMenu(savedMenuGroup, savedProducts, -1);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴를 등록할 때 가격이 상품 가격의 총 합보다 크면 예외가 발생한다.")
    @Test
    void create_throwsException_ifPriceOverProduct() {
        // given
        final int priceOverProduct = PRICE + 1;

        // when, then
        final Menu request = RequestBuilder.ofMenu(savedMenuGroup, savedProducts, priceOverProduct);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Product savedProduct = savedProducts.get(0);
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        final int discountedPrice = PRICE - 500;

        final Menu savedMenu1 = dataSupport.saveMenu(
                "치킨마요", PRICE, savedMenuGroup.getId(), menuProduct);
        final Menu savedMenu2 = dataSupport.saveMenu(
                "할인된 치킨마요", discountedPrice, savedMenuGroup.getId(), menuProduct);

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertThat(menus)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(Arrays.asList(savedMenu1, savedMenu2));
    }
}
