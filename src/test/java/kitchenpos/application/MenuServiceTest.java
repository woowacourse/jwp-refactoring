package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

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
        final Menu 신메뉴 = 메뉴(List.of(상품1), 메뉴그룹);
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

            final List<MenuProduct> 메뉴에_속하는_수량이_있는_상품 = List.of(
                    new MenuProduct(상품1.getId(), 3),
                    new MenuProduct(상품2.getId(), 5)
            );

            BigDecimal 실제_상품_가격과_갯수를_곱한_총합 = BigDecimal.ZERO;
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(상품1.getPrice().multiply(BigDecimal.valueOf(3)));
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(상품2.getPrice().multiply(BigDecimal.valueOf(5)));

            final Menu 신메뉴 = new Menu();
            신메뉴.setName("신메뉴");
            신메뉴.setPrice(실제_상품_가격과_갯수를_곱한_총합);
            신메뉴.setMenuGroupId(메뉴그룹.getId());
            신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);

            // when
            final Menu 저장된_메뉴 = menuService.create(신메뉴);

            // then
            assertSoftly(soft -> {
                soft.assertThat(저장된_메뉴.getId()).isNotNull();
                soft.assertThat(저장된_메뉴.getName()).isEqualTo(신메뉴.getName());
                soft.assertThat(저장된_메뉴.getMenuGroupId()).isEqualByComparingTo(메뉴그룹.getId());
                soft.assertThat(저장된_메뉴.getMenuProducts()).hasSize(2);
                soft.assertThat(저장된_메뉴.getMenuProducts().get(0).getMenuId()).isEqualTo(저장된_메뉴.getId());
                soft.assertThat(저장된_메뉴.getMenuProducts().get(0).getSeq()).isNotNull();
                soft.assertThat(저장된_메뉴.getMenuProducts().get(1).getSeq()).isNotNull();
                soft.assertThat(저장된_메뉴.getMenuProducts().get(0).getQuantity()).isEqualTo(3);
                soft.assertThat(저장된_메뉴.getMenuProducts().get(1).getQuantity()).isEqualTo(5);
            });
        }

        @Test
        void 가격이_비어있다면_예외가_발생한다() {
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Product 상품1 = productService.create(상품);
            final Product 상품2 = productService.create(상품);

            final List<MenuProduct> 메뉴에_속하는_수량이_있는_상품 = List.of(
                    new MenuProduct(상품1.getId(), 3),
                    new MenuProduct(상품2.getId(), 5)
            );

            final Menu 신메뉴 = new Menu();
            신메뉴.setName("신메뉴");
            신메뉴.setMenuGroupId(메뉴그룹.getId());
            신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);

            // when
            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 비어있거나 음수일 수 없습니다");
        }

        @Test
        void 가격이_0이라면_예외가_발생한다() {
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Product 상품1 = productService.create(상품);
            final Product 상품2 = productService.create(상품);

            final List<MenuProduct> 메뉴에_속하는_수량이_있는_상품 = List.of(
                    new MenuProduct(상품1.getId(), 3),
                    new MenuProduct(상품2.getId(), 5)
            );

            final Menu 신메뉴 = new Menu();
            신메뉴.setName("신메뉴");
            신메뉴.setMenuGroupId(메뉴그룹.getId());
            신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);

            // 가격에 음수로 설정한다
            신메뉴.setPrice(new BigDecimal(-1));

            // when
            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 비어있거나 음수일 수 없습니다");
        }

        @Test
        void 가격이_실제_상품과_수량을_곱한것의_총합보다_크다면_예외가_발생한다() {
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Product 상품1 = productService.create(상품);

            final List<MenuProduct> 메뉴에_속하는_수량이_있는_상품 = List.of(new MenuProduct(상품1.getId(), 5));

            BigDecimal 실제_상품_가격과_갯수를_곱한_총합 = BigDecimal.ZERO;
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(상품1.getPrice().multiply(BigDecimal.valueOf(5)));

            final Menu 신메뉴 = new Menu();
            신메뉴.setName("신메뉴");
            신메뉴.setMenuGroupId(메뉴그룹.getId());
            신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);

            // 실제 가격 보다 큰 가격을 설정한다.
            신메뉴.setPrice(실제_상품_가격과_갯수를_곱한_총합.add(BigDecimal.ONE));

            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록하려는 메뉴의 가격이 실제 상품의 가격을 초과할 수 없습니다");
        }

        @Test
        void 등록하려는_메뉴의_분류가_존재하지_않으면_예외가_발생한다() {
            final Product 상품1 = productService.create(상품);

            final List<MenuProduct> 메뉴에_속하는_수량이_있는_상품 = List.of(new MenuProduct(상품1.getId(), 5));

            BigDecimal 실제_상품_가격과_갯수를_곱한_총합 = BigDecimal.ZERO;
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(상품1.getPrice().multiply(BigDecimal.valueOf(5)));

            final Menu 신메뉴 = new Menu();
            신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);
            신메뉴.setPrice(실제_상품_가격과_갯수를_곱한_총합);
            신메뉴.setName("신메뉴");

            // 존재하지 않은 메뉴 분류 아이디를 입력한다
            신메뉴.setMenuGroupId(-1L);

            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴 분류 입니다");
        }

        @Test
        void 등록하려는_상품이_존재하지_않으면_예외가_발생한다() {
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);

            final Menu 신메뉴 = new Menu();
            신메뉴.setMenuGroupId(메뉴그룹.getId());
            신메뉴.setPrice(new BigDecimal(1));
            신메뉴.setName("신메뉴");

            // 존재하지 않은 상품을 등록한다
            신메뉴.setMenuProducts(List.of(new MenuProduct(-1L, 10)));

            Assertions.assertThatThrownBy(() -> menuService.create(신메뉴))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 상품입니다");
        }
    }
}
