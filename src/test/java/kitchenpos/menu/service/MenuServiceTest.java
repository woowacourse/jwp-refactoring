package kitchenpos.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.LowerThanZeroPriceException;
import kitchenpos.common.service.ServiceTest;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    private static final Price PRICE = new Price(new BigDecimal(5000));
    @Autowired
    private MenuService menuService;

    @DisplayName("Menu를 등록할 수 있다.")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));
        Product product = productRepository.save(new Product("상품1", PRICE));
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(product.getId(), 2L);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴1", new BigDecimal(10000),
                menuGroup.getId(), List.of(menuProductCreateRequest));

        menuService.create(menuCreateRequest);

        List<MenuResponse> menus = menuService.list();
        List<String> menuNames = menus.stream()
                .map(MenuResponse::getName)
                .collect(Collectors.toUnmodifiableList());
        List<MenuProduct> menuProducts = menuProductRepository.findAll();
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menuNames).contains("메뉴1"),
                () -> assertThat(menuProducts).hasSize(1)
        );
    }

    @DisplayName("존재하지 않는 Menu Group으로 Menu를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotFoundMenuGroup() {
        Long notFoundMenuGroupId = 100L;
        Product product = productRepository.save(new Product("상품1", PRICE));
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(product.getId(), 2L);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴1", new BigDecimal(10000),
                notFoundMenuGroupId, List.of(menuProductCreateRequest));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(MenuGroupNotFoundException.class)
                .hasMessage("존재하지 않는 메뉴 그룹입니다.");
    }

    @DisplayName("Menu의 price가 0보다 작거나, Menu를 구성하는 Product들의 price 합보다 크면 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("provideInvalidMenuPriceAndExpectedExceptionType")
    void create_Exception_InvalidPrice(int price, Class<?> expectedExceptionType) {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));
        Product product1 = productRepository.save(new Product("상품1", PRICE));
        Product product2 = productRepository.save(new Product("상품2", PRICE));
        MenuProductCreateRequest menuProductCreateRequest1 = new MenuProductCreateRequest(product1.getId(), 2L);
        MenuProductCreateRequest menuProductCreateRequest2 = new MenuProductCreateRequest(product2.getId(), 1L);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴1", new BigDecimal(price),
                menuGroup.getId(), List.of(menuProductCreateRequest1, menuProductCreateRequest2));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(expectedExceptionType);
    }

    private static Stream<Arguments> provideInvalidMenuPriceAndExpectedExceptionType() {
        return Stream.of(
                Arguments.of(-1, LowerThanZeroPriceException.class),
                Arguments.of(15001, InvalidMenuPriceException.class)
        );
    }
}
