package kitchenpos.application;

import static kitchenpos.application.KitchenposFixture.메뉴그룹만들기;
import static kitchenpos.application.KitchenposFixture.메뉴상품만들기;
import static kitchenpos.application.KitchenposFixture.상품만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.response.MenuResponse;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.product.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import({MenuService.class, MenuGroupService.class, ProductService.class})
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("이름과 가격, 메뉴 그룹의 식별자 그리고 메뉴에서 제공하는 상품들의 이름과 가격을 제공하여 메뉴를 저장할 수 있다.")
    void givenToMakeMenu(
            @Autowired MenuGroupService menuGroupService,
            @Autowired ProductService productService
    ) {
        // given : 메뉴 그룹
        final Long menuGroupId = 메뉴그룹만들기(menuGroupService).getId();

        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final List<MenuProductRequest> menuProductRequests = List.of(
                new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity()),
                new MenuProductRequest(menuProduct2.getProductId(), menuProduct2.getQuantity())
        );

        final MenuResponse savedMenu = menuService.create("메뉴!", new Price(new BigDecimal("4000")), menuGroupId, menuProductRequests);

        assertThat(savedMenu).isNotNull();
    }

    @Test
    @DisplayName("메뉴그룹의 식별자는 존재하는 식별자여야 한다.")
    void invalidMenuGroupId(@Autowired ProductService productService) {
        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final List<MenuProductRequest> menuProductRequests = List.of(
                new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity()),
                new MenuProductRequest(menuProduct2.getProductId(), menuProduct2.getQuantity())
        );

        assertThatThrownBy(() -> menuService.create("메뉴!", new Price(new BigDecimal("4000")), 0L, menuProductRequests))
                .as("존재하지 않는 메뉴 그룹에 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에서 제공하는 상품의 가격 총 합계가 메뉴 가격보다 작으면 안된다. (메뉴 가격 =< 각 상품의 가격 합계)")
    void invalidMenuPrice(
            @Autowired MenuGroupService menuGroupService,
            @Autowired ProductService productService
    ) {
        // given : 메뉴 그룹
        final Long menuGroupId = 메뉴그룹만들기(menuGroupService).getId();

        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final List<MenuProductRequest> menuProductRequests = List.of(
                new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity()),
                new MenuProductRequest(menuProduct2.getProductId(), menuProduct2.getQuantity())
        );

        assertThatThrownBy(() -> menuService.create("메뉴!", new Price(new BigDecimal("20001")), menuGroupId, menuProductRequests))
                .as("메뉴에 있는 각 상품의 금액 총합보다 메뉴 금액이 크다면 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조건에 맞다면 메뉴 정보를 저장한 후, 메뉴의 각 상품 정보를 저장한다.")
    void successfullySaved(
            @Autowired MenuGroupService menuGroupService,
            @Autowired ProductService productService
    ) {
        // given : 메뉴 그룹
        final Long menuGroupId = 메뉴그룹만들기(menuGroupService).getId();

        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final List<MenuProductRequest> menuProductRequests = List.of(
                new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity()),
                new MenuProductRequest(menuProduct2.getProductId(), menuProduct2.getQuantity())
        );

        final MenuResponse savedManu = menuService.create("메뉴!", new Price(new BigDecimal("4000")), menuGroupId, menuProductRequests);

        assertThat(savedManu).extracting("menuProducts").asList()
                .as("메뉴를 저장하면 메뉴 상품도 따라 저장된다.")
                .hasSize(2);
    }
}
