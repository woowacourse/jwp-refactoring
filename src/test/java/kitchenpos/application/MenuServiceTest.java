package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.exception.InvalidMenuProductsPriceException;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuServiceTest extends ServiceBaseTest {

    @Autowired
    protected MenuService menuService;

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void create() {
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product product = productRepository.save(new Product("메뉴 1", new Price(new BigDecimal(10000))));
        final MenuRequest request = new MenuRequest("메뉴 1", new BigDecimal(9999), menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));

        //when
        final MenuResponse menuResponse = menuService.create(request);

        //then
        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo(request.getName())
        );
    }

    @Test
    @DisplayName("메뉴 가격은 0원 미만이면 예외가 발생한다.")
    void menuPriceOverZero() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product product = productRepository.save(new Product("메뉴 1", new Price(new BigDecimal(10000))));
        final MenuRequest request = new MenuRequest("메뉴 1", new BigDecimal(-1), menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));

        //when&then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("잘못된 가격입니다.");
    }

    @Test
    @DisplayName("메뉴는 존재하는 메뉴 그룹에 속해있어야 한다.")
    void menuBelongToValidMenuGroup() {
        //given
        final Product product = productRepository.save(new Product("메뉴 1", new Price(new BigDecimal(10000))));
        final MenuRequest request = new MenuRequest("메뉴 1", new BigDecimal(100), 999L,
                List.of(new MenuProductRequest(product.getId(), 1L)));

        //when&then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(MenuGroupNotFoundException.class)
                .hasMessage("MenuGroup을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("메뉴는 존재하는 상품만 가져야 한다.")
    void menuHasValidMenu() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final MenuRequest request = new MenuRequest("메뉴 1", new BigDecimal(999), menuGroup.getId(),
                List.of(new MenuProductRequest(999L, 1L)));

        //when&then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("메뉴의 가격은 상품의 합계 이하이다.")
    void menuPriceSameOrOverThanProductsPrices() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product product = productRepository.save(new Product("메뉴 1", new Price(new BigDecimal(10000))));
        final MenuRequest request = new MenuRequest("메뉴 1", new BigDecimal(999999), menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));

        //when&then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(InvalidMenuProductsPriceException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품 가격 합 이하여야 합니다.");
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void list() {
        //given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product product = productRepository.save(new Product("메뉴 1", new Price(new BigDecimal(10000))));
        final MenuRequest request = new MenuRequest("메뉴 1", new BigDecimal(9999), menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));
        menuService.create(request);

        //when
        final List<MenuResponse> menuResponses = menuService.list();

        //then
        assertAll(
                () -> assertThat(menuResponses).hasSize(1),
                () -> assertThat(menuResponses.get(0).getName()).isEqualTo(request.getName())
        );
    }
}
