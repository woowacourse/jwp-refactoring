package kitchenpos.domain.menu;

import java.util.List;
import kitchenpos.domain.Money;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuValidatorTest {

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 메뉴_상품들_가격_합보다_크면_예외가_발생한다() {
        //given
        MenuGroup 메뉴_그룹 = menuGroupRepository.save(new MenuGroup(null, "메뉴 그룹"));
        List<MenuProduct> 메뉴상품 = 총_가격_13000원_메뉴상품목록();
        int 가격_합 = 13_000;
        Money 합보다_큰_가격 = Money.of(가격_합 + 1);

        Menu 메뉴 = new Menu(null, "메뉴", 합보다_큰_가격, 메뉴_그룹.getId(), 메뉴상품);

        //expect
        assertThatThrownBy(() -> menuValidator.validate(메뉴))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 가격이 메뉴에 속한 상품 가격의 합보다 큽니다.");
    }

    private List<MenuProduct> 총_가격_13000원_메뉴상품목록() {
        Product 상품_1 = productRepository.save(new Product(null, "상품", Money.of(10_000)));
        Product 상품_2 = productRepository.save(new Product(null, "상품", Money.of(3_000)));
        return List.of(new MenuProduct(null, 상품_1.getId(), 1), new MenuProduct(null, 상품_2.getId(), 1));
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
        //given
        List<MenuProduct> 메뉴상품_목록 = 총_가격_13000원_메뉴상품목록();
        Money 메뉴_가격 = Money.of(12_000);
        Long 유효하지_않은_메뉴그룹_아이디 = 유효하지_않은_메뉴그룹_아이디_만들기();
        Menu 메뉴 = new Menu(null, "메뉴이름", 메뉴_가격, 유효하지_않은_메뉴그룹_아이디, 메뉴상품_목록);

        //expect
        assertThatThrownBy(() -> menuValidator.validate(메뉴))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 그룹이 존재하지 않습니다.");
    }

    private Long 유효하지_않은_메뉴그룹_아이디_만들기() {
        MenuGroup 메뉴_그룹 = menuGroupRepository.save(new MenuGroup(null, "메뉴 그룹"));
        menuGroupRepository.deleteById(메뉴_그룹.getId());
        return 메뉴_그룹.getId();
    }

}
