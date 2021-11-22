package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Price;
import kitchenpos.domain.menu.MenuProduct.MenuProductBuilder;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.Product.ProductBuilder;
import kitchenpos.exception.InvalidMenuProductsPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @DisplayName("메뉴 가격이 단일 가격의 합보다 작을 경우 - 성공")
    @Test
    void validatePrice() {
        //given
        Product product1 = new ProductBuilder()
                .setPrice(3000)
                .setName("떡볶이")
                .build();
        Product product2 = new ProductBuilder()
                .setPrice(3000)
                .setName("순대")
                .build();
        Product product3 = new ProductBuilder()
                .setPrice(3000)
                .setName("튀김")
                .build();

        List<MenuProduct> menuProducts = List.of(product1, product2, product3).stream()
                .map(product -> new MenuProductBuilder().setProduct(product).setQuantity(1).build())
                .collect(Collectors.toList());

        //when
        MenuProducts actual = MenuProducts.create(menuProducts, Price.create(8999));
        //then
        assertThat(actual).isNotNull();
    }

    @DisplayName("메뉴 가격이 단일 가격의 합보다 클 경우 - 실패")
    @Test
    void validatePriceFail() {
        //given
        Product product1 = new ProductBuilder()
                .setPrice(3000)
                .setName("떡볶이")
                .build();
        Product product2 = new ProductBuilder()
                .setPrice(3000)
                .setName("순대")
                .build();
        Product product3 = new ProductBuilder()
                .setPrice(3000)
                .setName("튀김")
                .build();

        List<MenuProduct> menuProducts = List.of(product1, product2, product3).stream()
                .map(product -> new MenuProductBuilder().setProduct(product).setQuantity(1).build())
                .collect(Collectors.toList());

        //when
        assertThatThrownBy(() -> MenuProducts.create(menuProducts, Price.create(9001)))
                .isInstanceOf(InvalidMenuProductsPriceException.class);
        //then
    }
}