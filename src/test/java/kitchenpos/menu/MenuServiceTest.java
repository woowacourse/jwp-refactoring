package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.menu.dto.menu.CreateMenuRequest;
import kitchenpos.menu.dto.menu.MenuProductRequest;
import kitchenpos.common.vo.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_저장할_수_있다() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("피자"));
        final Product product = productRepository.save(new Product("치즈피자", new BigDecimal(3_000)));
        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 3);
        final CreateMenuRequest createMenuRequest = new CreateMenuRequest("치즈피자", 5_000, menuGroup.getId(), List.of(menuProductRequest));

        // when
        final Menu actual = menuService.create(createMenuRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("치즈피자"),
                () -> assertThat(actual.getPrice()).isEqualTo(Money.valueOf(5_000)),
                () -> assertThat(actual.getMenuGroup()).usingRecursiveComparison().isEqualTo(menuGroup),
                () -> assertThat(actual.getMenuProducts()).hasSize(1)
        );
    }

    @Nested
    class 메뉴_생성_실패 {

        @Test
        void 금액이_0보다_작다면_예외가_발생한다() {
            // given
            final Integer price = -999999;
            final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("피자"));
            final Product product = productRepository.save(new Product("치즈피자", new BigDecimal(3_000)));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 3);
            final CreateMenuRequest createMenuRequest = new CreateMenuRequest("치즈피자", price, menuGroup.getId(), List.of(menuProductRequest));

            // expected
            assertThatThrownBy(() -> menuService.create(createMenuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않는다면_예외가_발생한다() {
            // given
            final Long menuGroupId = 999999L;
            final Product product = productRepository.save(new Product("치즈피자", new BigDecimal(3_000)));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 3);
            final CreateMenuRequest createMenuRequest = new CreateMenuRequest("치즈피자", 5_000, menuGroupId, List.of(menuProductRequest));

            // expect
            assertThatThrownBy(() -> menuService.create(createMenuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴에_상품이_지정되지_않았다면_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("피자"));
            final CreateMenuRequest createMenuRequest = new CreateMenuRequest("피자", 5_000, menuGroup.getId(), List.of());

            // expect
            assertThatThrownBy(() -> menuService.create(createMenuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격의_총합이_상품_가격보다_크다면_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("피자"));
            final Product product = productRepository.save(new Product("치즈피자", new BigDecimal(3_000)));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 3);
            final CreateMenuRequest createMenuRequest = new CreateMenuRequest("치즈피자", 500_000, menuGroup.getId(), List.of(menuProductRequest));

            // expect
            assertThatThrownBy(() -> menuService.create(createMenuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 메뉴_목록을_조회할_수_있다() {
        // when
        final List<Menu> actual = menuService.list();

        // then
        assertThat(actual).hasSize(6);
    }

}
