package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuFixture.메뉴_생성_요청;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.test.ServiceTest;
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
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
            MenuRequest request = 메뉴_생성_요청("치즈피자", -1L, menuGroup.getId(), List.of());

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격은 0원 이상이어야 합니다.");
        }

        @Test
        void 메뉴_그룹_아이디가_존재하지_않는_경우_예외를_던진다() {
            // given
            MenuRequest request = 메뉴_생성_요청("치즈피자", 0L, 1L, List.of());

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴 그룹 아이디입니다.");
        }

        @Test
        void 존재하지_않는_메뉴_상품을_입력하는_경우_예외를_던진다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
            Product invalidProduct = 상품(Long.MAX_VALUE, "치즈 피자", 8900L);
            MenuProduct menuProduct = 메뉴_상품(invalidProduct, 1L);
            MenuRequest request = 메뉴_생성_요청("치즈피자", 0L, menuGroup.getId(), List.of(menuProduct));

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품이 존재하지 않습니다.");
        }

        @Test
        void 메뉴의_가격이_메뉴_상품들의_금액의_합보다_큰_경우_예외를_던진다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
            Product product = productRepository.save(상품("치즈 피자", 8900L));
            MenuProduct menuProduct = 메뉴_상품(product, 1L);
            MenuRequest request = 메뉴_생성_요청("치즈피자", 8901L, menuGroup.getId(), List.of(menuProduct));

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격은 메뉴 상품들의 금액의 합보다 클 수 없습니다.");
        }

        @Test
        void 메뉴가_정상적으로_등록된다() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
            Product product = productRepository.save(상품("치즈 피자", 8900L));
            MenuProduct menuProduct = 메뉴_상품(product, 1L);
            MenuRequest request = 메뉴_생성_요청("치즈피자", 8900L, menuGroup.getId(), List.of(menuProduct));

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
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
        Product product = productRepository.save(상품("치즈 피자", 8900L));

        MenuProduct menuProduct1 = 메뉴_상품(product, 1L);
        Menu menu1 = menuRepository.save(메뉴("치즈피자", 8900L, menuGroup.getId(), List.of(menuProduct1)));
        MenuProduct menuProduct2 = 메뉴_상품(product, 2L);
        Menu menu2 = menuRepository.save(메뉴("오픈기념 치즈피자", 5000L, menuGroup.getId(), List.of(menuProduct2)));

        // when
        List<MenuResponse> result = sut.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(List.of(MenuResponse.from(menu1), MenuResponse.from(menu2)));
    }
}
