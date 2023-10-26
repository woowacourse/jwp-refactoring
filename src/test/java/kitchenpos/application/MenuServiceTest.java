package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.KitchenposException;
import kitchenpos.support.ServiceTest;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.MenuRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.exception.ExceptionInformation.*;
import static kitchenpos.support.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("메뉴 서비스 테스트")
@ServiceTest
class MenuServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Test
    void 모든_메뉴를_조회한다() {
        // given
        final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
        final Product 상품1 = productService.create(상품);
        final MenuRequest 신메뉴 = 메뉴(List.of(상품1), 메뉴그룹);
        menuService.create(신메뉴);

        // when
        final List<Menu> 조회한_메뉴들 = menuService.list();

        // then
        assertThat(조회한_메뉴들).hasSize(1);
    }

    @DisplayName("메뉴 저장 테스트")
    @Nested
    class creatMenuTest {

        @Test
        void 정상적인_메뉴를_저장한다() {
            // given
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Product 상품1 = productService.create(상품);
            final Product 상품2 = productService.create(상품);

            final List<MenuProductRequest> 메뉴에_속하는_수량이_있는_상품 = List.of(
                    new MenuProductRequest(상품1.getId(), 3),
                    new MenuProductRequest(상품2.getId(), 5)
            );

            BigDecimal 실제_상품_가격과_갯수를_곱한_총합 = BigDecimal.ZERO;
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(상품1.getPrice().multiply(BigDecimal.valueOf(3)));
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(상품2.getPrice().multiply(BigDecimal.valueOf(5)));

            final MenuRequest 신메뉴 = new MenuRequest("신메뉴", 실제_상품_가격과_갯수를_곱한_총합, 메뉴그룹.getId(), 메뉴에_속하는_수량이_있는_상품);

            // when
            final Menu 저장된_메뉴 = menuService.create(신메뉴);

            // then
            assertSoftly(soft -> {
                soft.assertThat(저장된_메뉴.getId()).isNotNull();
                soft.assertThat(저장된_메뉴.getName()).isEqualTo(신메뉴.getName());
                soft.assertThat(저장된_메뉴.getMenuGroup().getId()).isEqualByComparingTo(메뉴그룹.getId());
                soft.assertThat(저장된_메뉴.getMenuProducts().getMenuProducts()).hasSize(2);
                soft.assertThat(저장된_메뉴.getMenuProducts().getMenuProducts().get(0).getMenu().getId()).isEqualTo(저장된_메뉴.getId());
                soft.assertThat(저장된_메뉴.getMenuProducts().getMenuProducts().get(0).getSeq()).isNotNull();
                soft.assertThat(저장된_메뉴.getMenuProducts().getMenuProducts().get(1).getSeq()).isNotNull();
                soft.assertThat(저장된_메뉴.getMenuProducts().getMenuProducts().get(0).getQuantity()).isEqualTo(3);
                soft.assertThat(저장된_메뉴.getMenuProducts().getMenuProducts().get(1).getQuantity()).isEqualTo(5);
            });
        }

        @Test
        void 가격이_비어있다면_예외가_발생한다() {
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Product 상품1 = productService.create(상품);
            final Product 상품2 = productService.create(상품);

            final List<MenuProductRequest> 메뉴에_속하는_수량이_있는_상품 = List.of(
                    new MenuProductRequest(상품1.getId(), 3),
                    new MenuProductRequest(상품2.getId(), 5)
            );

            final MenuRequest 신메뉴 = new MenuRequest();
            신메뉴.setName("신메뉴");
            신메뉴.setMenuGroupId(메뉴그룹.getId());
            신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);

            // when
            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(MENU_PRICE_IS_NULL.getMessage());
        }

        @Test
        void 가격이_0이라면_예외가_발생한다() {
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Product 상품1 = productService.create(상품);
            final Product 상품2 = productService.create(상품);

            final List<MenuProductRequest> 메뉴에_속하는_수량이_있는_상품 = List.of(
                    new MenuProductRequest(상품1.getId(), 3),
                    new MenuProductRequest(상품2.getId(), 5)
            );

            final MenuRequest 신메뉴 = new MenuRequest();
            신메뉴.setName("신메뉴");
            신메뉴.setMenuGroupId(메뉴그룹.getId());
            신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);

            // 가격에 음수로 설정한다
            신메뉴.setPrice(new BigDecimal(-1));

            // when
            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(MENU_PRICE_LENGTH_OUT_OF_BOUNCE.getMessage());
        }

        @Test
        void 가격이_실제_상품과_수량을_곱한것의_총합보다_크다면_예외가_발생한다() {
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Product 상품1 = productService.create(상품);

            final List<MenuProductRequest> 메뉴에_속하는_수량이_있는_상품 = List.of(new MenuProductRequest(상품1.getId(), 5));

            BigDecimal 실제_상품_가격과_갯수를_곱한_총합 = BigDecimal.ZERO;
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(상품1.getPrice().multiply(BigDecimal.valueOf(5)));

            final MenuRequest 신메뉴 = new MenuRequest();
            신메뉴.setName("신메뉴");
            신메뉴.setMenuGroupId(메뉴그룹.getId());
            신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);

            // 실제 가격 보다 큰 가격을 설정한다.
            신메뉴.setPrice(실제_상품_가격과_갯수를_곱한_총합.add(BigDecimal.ONE));

            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(MENU_PRICE_OVER_MENU_PRODUCT_PRICE.getMessage());
        }

        @Test
        void 등록하려는_메뉴의_분류가_존재하지_않으면_예외가_발생한다() {
            final Product 상품1 = productService.create(상품);

            final List<MenuProductRequest> 메뉴에_속하는_수량이_있는_상품 = List.of(new MenuProductRequest(상품1.getId(), 5));

            BigDecimal 실제_상품_가격과_갯수를_곱한_총합 = BigDecimal.ZERO;
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(상품1.getPrice().multiply(BigDecimal.valueOf(5)));

            final MenuRequest 신메뉴 = new MenuRequest();
            신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);
            신메뉴.setPrice(실제_상품_가격과_갯수를_곱한_총합);
            신메뉴.setName("신메뉴");

            // 존재하지 않은 메뉴 분류 아이디를 입력한다
            신메뉴.setMenuGroupId(-1L);

            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(MENU_GROUP_NOT_FOUND.getMessage());
        }

        @Test
        void 등록하려는_상품이_존재하지_않으면_예외가_발생한다() {
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);

            final MenuRequest 신메뉴 = new MenuRequest();
            신메뉴.setMenuGroupId(메뉴그룹.getId());
            신메뉴.setPrice(new BigDecimal(1));
            신메뉴.setName("신메뉴");

            // 존재하지 않은 상품을 등록한다
            신메뉴.setMenuProducts(List.of(new MenuProductRequest(-1L, 10)));

            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(PRODUCT_NOT_FOUND.getMessage());
        }
    }
}
