package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuServiceTest extends ServiceTest {

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        // given
        final Product product = saveAndGetProduct();
        final MenuProductCreateRequest menuProductRequest =
            createMenuProductCreateRequest(product.getId(), 1);
        final List<MenuProductCreateRequest> menuProducts = List.of(menuProductRequest);

        final MenuCreateRequest request = createMenuCreateRequest(
            "후라이드", product.getPrice(), saveAndGetMenuGroup().getId(), menuProducts);

        // when
        final MenuResponse actual = menuService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo("후라이드")
        );
    }

    @DisplayName("create 메서드는 메뉴 가격이 null이면 예외를 발생시킨다.")
    @Test
    void create_null_price_throwException() {
        // given
        final BigDecimal price = null;
        final MenuCreateRequest menu = createMenuCreateRequest(price);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create 메서드는 메뉴 가격이 음수면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -10_000L})
    void create_invalid_price_throwException(final Long input) {
        // given
        final BigDecimal price = BigDecimal.valueOf(input);
        final MenuCreateRequest menu = createMenuCreateRequest(price);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create 메서드는 메뉴 가격이 상품의 전체 금액보다 비싸면 예외를 발생시킨다.")
    @Test
    void create_invalid_price_throwException() {
        // given
        final int quantity = 10;
        final Product product = saveAndGetProduct();

        final MenuProductCreateRequest menuProduct = createMenuProductCreateRequest(
            product.getId(), quantity);
        final List<MenuProductCreateRequest> menuProducts = List.of(menuProduct);

        final BigDecimal totalPrice = product.getPrice()
            .multiply(BigDecimal.valueOf(quantity));
        final BigDecimal requestPrice = totalPrice.add(BigDecimal.TEN);

        final MenuCreateRequest menu = createMenuCreateRequest("후라이드", requestPrice,
            saveAndGetMenuGroup().getId(), menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        final MenuGroup menuGroup = saveAndGetMenuGroup();
        saveAndGetMenu(menuGroup.getId());

        // when
        final List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
