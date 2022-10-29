package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProductDto;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuDto;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.LowerThanZeroPriceException;
import kitchenpos.exception.MenuGroupNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("Menu를 등록할 수 있다.")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));
        Product product = productRepository.save(Product.of("상품1", new BigDecimal(5000)));
        MenuProductDto menuProductDto = new MenuProductDto(product.getId(), 2L);
        MenuDto menu = new MenuDto("메뉴1", new BigDecimal(10000), menuGroup.getId(), List.of(menuProductDto));

        menuService.create(menu);

        List<MenuDto> menus = menuService.list();
        List<String> menuNames = menus.stream()
                .map(MenuDto::getName)
                .collect(Collectors.toUnmodifiableList());
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menuNames).contains("메뉴1")
        );
    }

    @DisplayName("존재하지 않는 Menu Group으로 Menu를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotFoundMenuGroup() {
        Long notFoundMenuGroupId = 100L;
        Product product = productRepository.save(Product.of("상품1", new BigDecimal(5000)));
        MenuProductDto menuProductDto = new MenuProductDto(product.getId(), 2L);
        MenuDto menu = new MenuDto("메뉴1", new BigDecimal(10000), notFoundMenuGroupId, List.of(menuProductDto));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(MenuGroupNotFoundException.class)
                .hasMessage("존재하지 않는 메뉴 그룹입니다.");
    }

    @DisplayName("Menu의 price가 0보다 작거나, Menu를 구성하는 Product들의 price 합보다 크면 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("provideInvalidMenuPriceAndExpectedExceptionType")
    void create_Exception_InvalidPrice(int price, Class<?> expectedExceptionType) {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));
        Product product1 = productRepository.save(Product.of("상품1", new BigDecimal(5000)));
        Product product2 = productRepository.save(Product.of("상품2", new BigDecimal(6000)));
        MenuProductDto menuProductDto1 = new MenuProductDto(product1.getId(), 2L);
        MenuProductDto menuProductDto2 = new MenuProductDto(product2.getId(), 1L);
        MenuDto menu = new MenuDto("메뉴1", new BigDecimal(price), menuGroup.getId(),
                List.of(menuProductDto1, menuProductDto2));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(expectedExceptionType);
    }

    private static Stream<Arguments> provideInvalidMenuPriceAndExpectedExceptionType() {
        return Stream.of(
                Arguments.of(-1, LowerThanZeroPriceException.class),
                Arguments.of(16001, InvalidMenuPriceException.class)
        );
    }
}
