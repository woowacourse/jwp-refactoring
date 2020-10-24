package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Product;

class ProductServiceIntegrationTest extends ServiceIntegrationTest {
    @Autowired
    ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        Product spicyChicken = getProductWithoutId("스파이시치킨", 17000);

        Product persist = productService.create(spicyChicken);

        assertThat(persist).isEqualToIgnoringNullFields(spicyChicken);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void create_willThrowException_whenPriceIsNull() {
        Product spicyChicken = getProductWithNullPrice("스파이시치킨");

        assertThatThrownBy(() -> productService.create(spicyChicken))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 음수면 등록할 수 없다.")
    @Test
    void create_willThrowException_whenPriceIsNegativeNumber() {
        Product spicyChicken = getProductWithoutId("스파이시치킨", -16000);

        assertThatThrownBy(() -> productService.create(spicyChicken))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 전체를 조회한다.")
    @Test
    void list() {
        List<Product> products = productService.list();

        assertAll(
            () -> assertThat(products).hasSize(6),
            () -> assertThat(products.get(0)).isEqualToComparingFieldByField(getProductWithId(1L, "후라이드", 16000)),
            () -> assertThat(products.get(1)).isEqualToComparingFieldByField(getProductWithId(2L, "양념치킨", 16000)),
            () -> assertThat(products.get(2)).isEqualToComparingFieldByField(getProductWithId(3L, "반반치킨", 16000)),
            () -> assertThat(products.get(3)).isEqualToComparingFieldByField(getProductWithId(4L, "통구이", 16000)),
            () -> assertThat(products.get(4)).isEqualToComparingFieldByField(getProductWithId(5L, "간장치킨", 17000)),
            () -> assertThat(products.get(5)).isEqualToComparingFieldByField(getProductWithId(6L, "순살치킨", 17000))
        );
    }
}