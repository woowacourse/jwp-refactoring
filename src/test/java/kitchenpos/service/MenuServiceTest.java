package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.InvalidNumberException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.util.ObjectCreator;
import kitchenpos.util.ServiceTest;
import kitchenpos.value.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@DisplayName("메뉴 테스트")
@Import(MenuService.class)
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    private static Stream<Arguments> menuParameterProvider() {
        return Stream.of(
                Arguments.of("음수의 가격을 가진", 1L, BigDecimal.valueOf(-1), List.of(1L, 2L),
                        InvalidNumberException.class, "가격은 음수가 될 수 없습니다."),
                Arguments.of("메뉴 그룹에 속하지 않은", -1L, BigDecimal.valueOf(100), List.of(1L, 2L),
                        NoSuchDataException.class, "해당하는 id의 메뉴 그룹이 없습니다."),
                Arguments.of("없는 상품을 가진", 1L, BigDecimal.valueOf(100), List.of(-1L),
                        NoSuchDataException.class, "해당하는 id의 상품이 없습니다."),
                Arguments.of("상품 가격의 총합보다 더 비싼", 1L, BigDecimal.valueOf(100000000), List.of(1L),
                        InvalidNumberException.class, "상품 가격의 총합보다 메뉴가 더 비쌀 수 없습니다.")
        );
    }

    @DisplayName("단일 메뉴를 생성한다")
    @Test
    void create()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // given
        final int newMenuId = menuService.list().size() + 1;
        final List<MenuProductDto> dto = List.of(
                MenuProductDto.from(
                        new MenuProduct(1L, 1L, 1L, new Quantity(1L))),
                MenuProductDto.from(
                        new MenuProduct(2L, 2L, 1L, new Quantity(2L)))
        );
        final CreateMenuRequest request = ObjectCreator.getObject(CreateMenuRequest.class, "test",
                BigDecimal.valueOf(29), 1L, dto);
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
            final String errorMessage
    ) {
        // when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest(products, price, menuGroupId)))
                .isInstanceOf(exception)
                .hasMessage(errorMessage);
    }

    private CreateMenuRequest createMenuRequest(
            final List<Long> products,
            final BigDecimal price,
            final Long menuGroupId
    ) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final List<MenuProductDto> dto = new ArrayList<>();
        for (Long productId : products) {
            dto.add(MenuProductDto.from(
                    new MenuProduct(
                            productId,
                            productId,
                            productId,
                            new Quantity(1L))
            ));
        }
        return ObjectCreator.getObject(CreateMenuRequest.class, "test", price, menuGroupId, dto);
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
