package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.support.DataSupport;
import kitchenpos.support.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
        final MenuRequest request = RequestBuilder.ofMenu(savedMenuGroup, savedProducts, PRICE);
        final MenuResponse response = menuService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("메뉴를 등록할 때 상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifProductNotFound() {
        // given
        final Product unsavedProduct = new Product(0L, "없는 메뉴", new BigDecimal(0));
        final List<Product> unsavedProducts = Arrays.asList(unsavedProduct);

        // when, then
        final MenuRequest request = RequestBuilder.ofMenu(savedMenuGroup, unsavedProducts, PRICE);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴를 등록할 때 가격을 입력하지 않으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifNoPrice() {
        final MenuRequest request = RequestBuilder.ofMenu(savedMenuGroup, savedProducts, null);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @ValueSource(ints = {-1, -500, -1000})
    @ParameterizedTest(name = "메뉴를 등록할 때 가격이 0보다 작은 {0}이면 예외가 발생한다.")
    void create_throwsException_ifPriceUnder0(final int price) {
        final MenuRequest request = RequestBuilder.ofMenu(savedMenuGroup, savedProducts, price);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @ValueSource(ints = {1, 500, 1000})
    @ParameterizedTest(name = "메뉴를 등록할 때 가격이 상품 가격의 총 합보다 크면 예외가 발생한다({0}만큼 초과).")
    void create_throwsException_ifPriceOverProduct(final int addedAmount) {
        // given
        final int priceOverProduct = PRICE + addedAmount;

        // when, then
        final MenuRequest request = RequestBuilder.ofMenu(savedMenuGroup, savedProducts, priceOverProduct);
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
        final List<MenuResponse> expected = MenuResponse.from(Arrays.asList(savedMenu1, savedMenu2));

        // when
        final List<MenuResponse> responses = menuService.list();

        // then
        assertThat(responses)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }
}
