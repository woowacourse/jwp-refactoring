package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuRequest.MenuProductDto;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.MenuPriceTooExpensiveException;
import kitchenpos.menu.exception.PriceNullOrLessThanOrEqualToZeroException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MenuService menuService;

    @Nested
    class 메뉴_생성_테스트 {

        @Test
        void 메뉴를_정상_생성한다() {
            // given
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            Product product = Product.of("product", new BigDecimal(2500));
            em.persist(menuGroup);
            em.persist(product);
            em.flush();
            em.clear();
            MenuRequest request = new MenuRequest("menu", new BigDecimal(10_000), 1L,
                    List.of(new MenuProductDto(product.getId(), 4L)));

            // when
            MenuResponse response = menuService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                assertThat(response.getId()).isNotNull();
                assertThat(response.getMenuGroupId()).isNotNull();
                assertThat(response.getMenuProducts().size()).isPositive();
            });
        }

        @Test
        void 존재하지_않는_메뉴_그룹에_속한_메뉴를_생성_시_예외를_반환한다() {
            // given
            MenuRequest request = new MenuRequest("menu", new BigDecimal(10_000), -1L,
                    List.of(new MenuProductDto(2L, 3L)));

            // when, then
            assertThrows(MenuGroupNotFoundException.class, () -> menuService.create(request));
        }

        @Test
        void 가격이_없는_메뉴를_생성_시_예외를_반환한다() {
            // given
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            em.persist(menuGroup);
            em.flush();
            em.clear();
            MenuRequest request = new MenuRequest("menu", null, menuGroup.getId(),
                    List.of(new MenuProductDto(2L, 3L)));

            // when, then
            assertThrows(PriceNullOrLessThanOrEqualToZeroException.class,
                    () -> menuService.create(request));
        }

        @Test
        void 음의_가격을_갖는_메뉴를_생성_시_예외를_반환한다() {
            // given
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            em.persist(menuGroup);
            em.flush();
            em.clear();
            MenuRequest request = new MenuRequest("menu", new BigDecimal(-1), menuGroup.getId(),
                    List.of(new MenuProductDto(2L, 3L)));

            // when, then
            assertThrows(PriceNullOrLessThanOrEqualToZeroException.class,
                    () -> menuService.create(request));
        }

        @Test
        void 존재하지_않는_상품_id로_메뉴를_생성_시_예외를_반환한다() {
            // given
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            em.persist(menuGroup);
            em.flush();
            em.clear();
            MenuRequest request = new MenuRequest("menu", new BigDecimal(10_000), 1L,
                    List.of(new MenuProductDto(-1L, 3L)));

            // when, then
            assertThrows(ProductNotFoundException.class, () -> menuService.create(request));
        }

        @Test
        void 메뉴_가격이_상품_가격과_수량의_곱보다_비싸면_예외를_반환한다() {
            // given
            MenuGroup menuGroup = new MenuGroup("menuGroup");
            Product product = Product.of("product", new BigDecimal(2500));
            em.persist(menuGroup);
            em.persist(product);
            em.flush();
            em.clear();
            MenuRequest request = new MenuRequest("menu", new BigDecimal(10_000), 1L,
                    List.of(new MenuProductDto(product.getId(), 3L)));

            // when, then
            assertThrows(MenuPriceTooExpensiveException.class, () -> menuService.create(request));
        }
    }

    @Test
    void 메뉴를_전체_조회한다() {
        assertThat(menuService.list()).isInstanceOf(List.class);
    }
}
