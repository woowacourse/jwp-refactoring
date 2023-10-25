package kitchenpos.menu.application;

import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuServiceTest extends ApplicationTestConfig {

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(
                menuRepository,
                menuGroupRepository,
                productRepository
        );
    }

    @DisplayName("메뉴 가격")
    @Nested
    class MenuPriceNestedTest {

        @DisplayName("[SUCCESS] 등록할 신규 메뉴에 메뉴 상품이 없을 경우 메뉴의 가격은 0원이어야 등록할 수 있다.")
        @Test
        void success_create_menu_when_MenuProductsIsEmpty() {
            // given
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));

            final MenuCreateRequest request = new MenuCreateRequest(
                    "테스트용 메뉴명",
                    new BigDecimal("0"),
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            );

            // when
            final MenuResponse actual = menuService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getName()).isEqualTo(request.getName());
                softly.assertThat(actual.getPrice()).isEqualByComparingTo(request.getPrice());
                softly.assertThat(actual.getMenuGroup().getId()).isEqualTo(request.getMenuGroupId());
                softly.assertThat(actual.getMenuProducts())
                        .usingRecursiveComparison()
                        .isEqualTo(Collections.emptyList());
            });
        }

        @DisplayName("[EXCEPTION] 등록할 신규 메뉴에 메뉴 상품 목록들의 가격 합보다 메뉴의 가격이 높을 경우 예외가 발생한다.")
        @Test
        void throwException_when_create_Menu_IfMenuProductsPriceSum_IsGreaterThanMenuPrice() {
            // given
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));

            // when
            final MenuCreateRequest request = new MenuCreateRequest(
                    "테스트용 메뉴명",
                    new BigDecimal("10000"),
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            );

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 그룹")
    @Nested
    class MenuGroupNestedTest {

        @DisplayName("[EXCEPTION] 등록할 신규 메뉴에 메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwException_when_create_Menu_IfMenuGroupIsNotExists() {
            // given
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));

            // when
            final MenuCreateRequest request = new MenuCreateRequest(
                    "테스트용 메뉴명",
                    new BigDecimal("10000"),
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            );

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 상품")
    @Nested
    class MenuProductNestedTest {

        @DisplayName("[SUCCESS] 등록할 신규 메뉴에 메뉴 상품 목록을 같이 등록할 수 있다.")
        @Test
        void success_create_menu_with_MenuProducts() {
            // given
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));

            final List<MenuProductCreateRequest> menuProductCreateRequests = new ArrayList<>();
            for (int count = 1; count <= 10; count++) {
                final Product savedProduct = productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("10000")));
                menuProductCreateRequests.add(new MenuProductCreateRequest(savedProduct.getId(), 10));
            }

            final MenuCreateRequest request = new MenuCreateRequest(
                    "테스트용 메뉴명",
                    new BigDecimal("10000"),
                    savedMenuGroup.getId(),
                    menuProductCreateRequests
            );

            // when
            final MenuResponse actual = menuService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getName()).isEqualTo(request.getName());
                softly.assertThat(actual.getPrice()).isEqualByComparingTo(request.getPrice());
                softly.assertThat(actual.getMenuGroup().getId()).isEqualTo(request.getMenuGroupId());
            });
        }

        private List<MenuProductCreateRequest> convertToMenuProductCreateRequest(final Menu menu) {
            return menu.getMenuProducts()
                    .getMenuProductItems()
                    .stream()
                    .map(menuProduct -> new MenuProductCreateRequest(
                            menuProduct.getProduct().getId(),
                            menuProduct.getQuantity().getValue()
                    )).collect(Collectors.toList());
        }
    }
}
