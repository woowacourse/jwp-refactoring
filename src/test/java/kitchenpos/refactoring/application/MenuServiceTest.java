package kitchenpos.refactoring.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.refactoring.application.dto.MenuProductRequest;
import kitchenpos.refactoring.application.dto.MenuRequest;
import kitchenpos.refactoring.application.dto.MenuResponse;
import kitchenpos.refactoring.domain.Menu;
import kitchenpos.refactoring.domain.MenuGroup;
import kitchenpos.refactoring.domain.MenuGroupRepository;
import kitchenpos.refactoring.domain.Price;
import kitchenpos.refactoring.domain.Product;
import kitchenpos.refactoring.domain.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupDao;
    @Autowired
    private ProductRepository productRepository;


    @Nested
    class 메뉴를_등록할_때 {

        @Test
        void success() {
            // given
            Product savedProduct = productRepository.save(new Product("후라이드", new Price(BigDecimal.valueOf(2000))));
            MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("치킨"));

            MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 1L);
            MenuRequest menuRequest = new MenuRequest(
                    "후라이드 치킨",
                    BigDecimal.valueOf(1000),
                    savedMenuGroup.getId(),
                    List.of(menuProductRequest)
            );

            // when
            Menu result = menuService.create(menuRequest);

            // then
            assertThat(result.getName()).isEqualTo(menuRequest.getName());
            assertThat(result.getPrice().getPrice()).isEqualTo(menuRequest.getPrice());
        }

        @Test
        void 가격_정보가_없거나_0보다_작은_경우_실패() {
            // given
            Product savedProduct = productRepository.save(new Product("후라이드", new Price(BigDecimal.valueOf(2000))));
            MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("치킨"));

            MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 1L);
            MenuRequest menuRequest = new MenuRequest(
                    "후라이드 치킨",
                    BigDecimal.valueOf(-10),
                    savedMenuGroup.getId(),
                    List.of(menuProductRequest)
            );

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 등록되지_않은_메뉴그룹에_속할_시_실패() {
            // given
            Product savedProduct = productRepository.save(new Product("후라이드", new Price(BigDecimal.valueOf(2000))));
            MenuGroup savedMenuGroup = new MenuGroup("치킨");

            MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 1L);
            MenuRequest menuRequest = new MenuRequest(
                    "후라이드 치킨",
                    BigDecimal.valueOf(1000),
                    savedMenuGroup.getId(),
                    List.of(menuProductRequest)
            );

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_메뉴_내부의_상품의_총_합계_가격보다_크면_실패() {
            // given
            Product savedProduct = productRepository.save(new Product("후라이드", new Price(BigDecimal.valueOf(2000))));
            MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("치킨"));

            MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 1L);
            MenuRequest menuRequest = new MenuRequest(
                    "후라이드 치킨",
                    BigDecimal.valueOf(3000),
                    savedMenuGroup.getId(),
                    List.of(menuProductRequest)
            );

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 메뉴_목록_조회() {
        // given
        Product savedProduct = productRepository.save(new Product("후라이드", new Price(BigDecimal.valueOf(2000))));
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("치킨"));

        MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest(
                "후라이드 치킨",
                new BigDecimal("1000.00"),
                savedMenuGroup.getId(),
                List.of(menuProductRequest)
        );
        Menu savedMenu = menuService.create(menuRequest);

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(List.of(MenuResponse.from(savedMenu)));
    }
}
