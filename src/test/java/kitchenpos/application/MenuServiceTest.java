package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.menu.CreateMenuRequest;
import kitchenpos.ui.dto.menu.MenuProductDto;
import kitchenpos.domain.vo.Money;
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
        final MenuProductDto menuProductDto = new MenuProductDto(product.getId(), 3);
        final CreateMenuRequest createMenuRequest = new CreateMenuRequest("치즈피자", 5_000, menuGroup.getId(), List.of(menuProductDto));

        // when
        final Menu actual = menuService.create(createMenuRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("치즈피자"),
                () -> assertThat(actual.getPrice()).isEqualTo(Money.valueOf(5_000)),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(menuGroup.getId())
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
            final MenuProductDto menuProductDto = new MenuProductDto(product.getId(), 3);
            final CreateMenuRequest createMenuRequest = new CreateMenuRequest("치즈피자", price, menuGroup.getId(), List.of(menuProductDto));

            // expected
            assertThatThrownBy(() -> menuService.create(createMenuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_지정되지_않았다면_예외가_발생한다() {
            // given
            final Long menuGroupId = null;
            final Product product = productRepository.save(new Product("치즈피자", new BigDecimal(3_000)));
            final MenuProductDto menuProductDto = new MenuProductDto(product.getId(), 3);
            final CreateMenuRequest createMenuRequest = new CreateMenuRequest("치즈피자", 5_000, menuGroupId, List.of(menuProductDto));

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
            final MenuProductDto menuProductDto = new MenuProductDto(product.getId(), 3);
            final CreateMenuRequest createMenuRequest = new CreateMenuRequest("치즈피자", 500_000, menuGroup.getId(), List.of(menuProductDto));

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
