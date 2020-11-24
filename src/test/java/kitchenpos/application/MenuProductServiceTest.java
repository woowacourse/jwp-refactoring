package kitchenpos.application;

import kitchenpos.application.menu.MenuProductService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.request.MenuProductRequest;
import kitchenpos.repository.menu.MenuProductRepository;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.createMenuWithPrice;
import static kitchenpos.fixture.MenuProductFixture.createMenuProductWithProduct;
import static kitchenpos.fixture.MenuProductFixture.createMenuProductWithProductAndQuantity;
import static kitchenpos.fixture.ProductFixture.createProductWithPrice;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class MenuProductServiceTest {

    @Autowired
    private MenuProductService menuProductService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @DisplayName("createMenuProduct 정상 동작")
    @Test
    void createMenuProduct() {
        BigDecimal price = BigDecimal.valueOf(10);
        Long quantity = 3L;
        Product savedProduct = productRepository.save(createProductWithPrice(price));
        Menu savedMenu = menuRepository.save(createMenuWithPrice(price));
        MenuProduct menuProduct = createMenuProductWithProductAndQuantity(savedProduct, quantity);
        MenuProductRequest menuProductRequest = MenuProductRequest.from(menuProduct);

        List<MenuProduct> actual = menuProductService.createMenuProduct(savedMenu, Arrays.asList(menuProductRequest));
        MenuProduct expected = new MenuProduct(null, savedMenu, savedProduct, quantity);

        assertAll(() -> {
            assertThat(actual).extracting(MenuProduct::getSeq).isNotNull();
            assertThat(actual.get(0))
                    .usingRecursiveComparison()
                    .ignoringFields("seq", "product.price")
                    .isEqualTo(expected);
            assertThat(actual).extracting(MenuProduct::getProduct)
                    .extracting(product -> product.getPrice().intValue())
                    .containsOnly(price.intValue());
        });
    }

    @DisplayName("createMenuProduct 메뉴에 속하는 MenuProducts의 총 금액이 메뉴의 가격보다 작은 경우 예외 테스트")
    @Test
    void createMenuProductsAmountLessThanMenuPrice() {
        Product savedProduct = productRepository.save(createProductWithPrice(new BigDecimal(9999)));
        Menu savedMenu = menuRepository.save(createMenuWithPrice(new BigDecimal(10000)));
        MenuProductRequest menuProductRequest = MenuProductRequest.from(createMenuProductWithProduct(savedProduct));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuProductService.createMenuProduct(savedMenu, Arrays.asList(menuProductRequest)))
                .withMessage("MenuProduct 전부를 합한 금액이 Menu 금액보다 작을 수 없습니다.");
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        productRepository.deleteAll();
    }
}
