package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.entity.MenuProduct;
import kitchenpos.domain.value.Quantity;
import kitchenpos.dto.request.menu.CreateMenuRequest;
import kitchenpos.dto.request.menu.MenuProductDto;
import kitchenpos.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("단일 메뉴를 생성한다")
    @Test
    void create()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final int newMenuId = menuService.list().size() + 1;
        final List<MenuProductDto> dto = List.of(
                MenuProductDto.from(new MenuProduct(1L,1L,1L,new Quantity(1L))),
                MenuProductDto.from(new MenuProduct(2L,2L,2L,new Quantity(2L)))
        );
        final CreateMenuRequest request = getRequest(CreateMenuRequest.class, "test", BigDecimal.valueOf(29), 1L, dto);
        // when
        final MenuResponse actual = menuService.create(request);

        // then
        assertThat(actual.getId()).isEqualTo(newMenuId);
    }

    @DisplayName("메뉴 생성에 실패한다")
    @ParameterizedTest(name = "{0} 메뉴 생성 시 실패한다")
    @MethodSource("menuParameterProvider")
    void create_Fail(
            final String testName,
            final Long menuGroupId,
            final BigDecimal price,
            final List<Long> products,
            final Class exception,
            final String message
    ) {
        // given
        // when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest(products,price,menuGroupId)))
                .isInstanceOf(exception)
                .hasMessage(message);
    }

    private CreateMenuRequest createMenuRequest(
            final List<Long> products,
            final BigDecimal price,
            final Long menuGroupId
    ) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final List<MenuProductDto> dto = new ArrayList<>();
        for (Long productId : products) {
            dto.add(MenuProductDto.from(new MenuProduct(productId, productId, productId, new Quantity(productId))));
        }
        return getRequest(CreateMenuRequest.class, "test", price, menuGroupId, dto);
    }

    private static Stream<Arguments> menuParameterProvider() {
        return Stream.of(
                Arguments.of("음수의 가격을 가진", 1L, BigDecimal.valueOf(-1), List.of(1L, 2L),
                        IllegalArgumentException.class, null),
                Arguments.of("메뉴 그룹에 속하지 않은", -1L, BigDecimal.valueOf(100), List.of(1L, 2L),
                        IllegalArgumentException.class, null),
                Arguments.of("없는 상품을 가진", 1L, BigDecimal.valueOf(100), List.of(-1L),
                        IllegalArgumentException.class, null),
                Arguments.of("없는 상품을 가진", 1L, BigDecimal.valueOf(100000000), List.of(1L),
                        IllegalArgumentException.class, null)
        );
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void list() {
        // given & when
        final List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).hasSize(2);
    }
}
