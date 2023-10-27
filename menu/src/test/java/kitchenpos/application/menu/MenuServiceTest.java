package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.test.ServiceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
public class MenuServiceTest {

    @Autowired
    private MenuService sut;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class 메뉴를_등록할_때 {

        @Test
        void 메뉴의_가격이_0원_미만인_경우_예외를_던진다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹("피자"));
            MenuRequest request = MenuFixture.메뉴_생성_요청("치즈피자", -1L, menuGroup.getId(), List.of());

            // expect
            Assertions.assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격은 0원 이상이어야 합니다.");
        }

        @Test
        void 메뉴_그룹_아이디가_존재하지_않는_경우_예외를_던진다() {
            // given
            MenuRequest request = MenuFixture.메뉴_생성_요청("치즈피자", 0L, 1L, List.of());

            // expect
            Assertions.assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴 그룹 아이디입니다.");
        }

        @Test
        void 존재하지_않는_메뉴_상품을_입력하는_경우_예외를_던진다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹("피자"));
            Product invalidProduct = ProductFixture.상품(Long.MAX_VALUE, "치즈 피자", 8900L);
            MenuProduct menuProduct = MenuProductFixture.메뉴_상품(invalidProduct, 1L);
            MenuRequest request = MenuFixture.메뉴_생성_요청("치즈피자", 0L, menuGroup.getId(), List.of(menuProduct));

            // expect
            Assertions.assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품이 존재하지 않습니다.");
        }

        @Test
        void 메뉴의_가격이_메뉴_상품들의_금액의_합보다_큰_경우_예외를_던진다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹("피자"));
            Product product = productRepository.save(ProductFixture.상품("치즈 피자", 8900L));
            MenuProduct menuProduct = MenuProductFixture.메뉴_상품(product, 1L);
            MenuRequest request = MenuFixture.메뉴_생성_요청("치즈피자", 8901L, menuGroup.getId(), List.of(menuProduct));

            // expect
            Assertions.assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격은 메뉴 상품들의 금액의 합보다 클 수 없습니다.");
        }

        @Test
        void 메뉴가_정상적으로_등록된다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹("피자"));
            Product product = productRepository.save(ProductFixture.상품("치즈 피자", 8900L));
            MenuProduct menuProduct = MenuProductFixture.메뉴_상품(product, 1L);
            MenuRequest request = MenuFixture.메뉴_생성_요청("치즈피자", 8900L, menuGroup.getId(), List.of(menuProduct));

            // when
            MenuResponse result = sut.create(request);

            // then
            Menu menu = menuRepository.findById(result.getId()).get();
            assertThat(result)
                    .usingRecursiveComparison()
                    .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                    .isEqualTo(MenuResponse.from(menu));
        }
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹("피자"));
        Product product = productRepository.save(ProductFixture.상품("치즈 피자", 8900L));

        MenuProduct menuProduct1 = MenuProductFixture.메뉴_상품(product, 1L);
        Menu menu1 = menuRepository.save(MenuFixture.메뉴("치즈피자", 8900L, menuGroup.getId(), List.of(menuProduct1)));
        MenuProduct menuProduct2 = MenuProductFixture.메뉴_상품(product, 2L);
        Menu menu2 = menuRepository.save(MenuFixture.메뉴("오픈기념 치즈피자", 5000L, menuGroup.getId(), List.of(menuProduct2)));

        // when
        List<MenuResponse> result = sut.list();

        // then
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(List.of(MenuResponse.from(menu1), MenuResponse.from(menu2)));
    }
}
