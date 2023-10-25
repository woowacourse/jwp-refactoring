package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class create_성공_테스트 {

        @Test
        void 메뉴를_생성하다() {
            // given
            menuGroupRepository.save(new MenuGroup("메뉴_그룹"));
            final var product1 = productRepository.save(new Product("상품_이름1", BigDecimal.valueOf(1000)));
            final var product2 = productRepository.save(new Product("상품_이름2", BigDecimal.valueOf(200)));

            final var menuProductRequest1 = new MenuProductRequest(product1.getId(), 10L);
            final var menuProductRequest2 = new MenuProductRequest(product2.getId(), 5L);
            final var menuCreateRequest = new MenuCreateRequest("메뉴_이름", BigDecimal.valueOf(5600), 1L,
                    List.of(menuProductRequest1, menuProductRequest2));

            // when
            final var actual = menuService.create(menuCreateRequest);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 존재하지_않은_메뉴_그룹을_사용하면_에러를_반환한다() {
            // given
            final var request = new MenuCreateRequest("메뉴_이름", BigDecimal.valueOf(1000), 1L, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 메뉴 그룹입니다.");
        }

        @Test
        void 존재하지_않는_상품을_사용하면_에러를_반환한다() {
            // given
            menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            final var menuProductRequest = new MenuProductRequest(999L, 1L);
            final var menuCreateRequest = new MenuCreateRequest("메뉴_이름", BigDecimal.valueOf(1000), 1L,
                    List.of(menuProductRequest));

            // when & then
            assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 상품입니다.");
        }
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 메뉴_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given & when
            final var actual = menuService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 메뉴가_하나_이상_존재하면_메뉴_목록을_반환한다() {
            // given
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            final var menu = menuRepository.save(
                    new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, Collections.emptyList()));
            final var response = MenuResponse.from(menu);
            final var expected = List.of(response);

            // when
            final var actual = menuService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class list_실패_테스트 {
    }
}
