package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuServiceFixture {

    protected String 메뉴_이름 = "메뉴";
    protected BigDecimal 메뉴_가격 = BigDecimal.valueOf(4_000);
    protected BigDecimal 상품_합보다_큰_메뉴_가격 = BigDecimal.valueOf(5_000);
    protected BigDecimal 메뉴_가격이_음수 = BigDecimal.valueOf(-4_000);
    protected Long 메뉴_그룹_아이디 = 1L;
    protected Long 존재하지_않는_메뉴_그룹_아이디 = -999L;
    protected List<MenuProduct> 메뉴_상품들;
    protected List<MenuProduct> 존재하지_않는_상품_가진_메뉴_상품들;
    protected Menu 저장된_메뉴;
    protected Menu 저장된_메뉴1;
    protected Menu 저장된_메뉴2;
    protected List<Menu> 저장된_메뉴들;

    protected Product 상품1;
    protected Product 상품2;
    protected MenuProduct 메뉴_상품1;
    protected MenuProduct 메뉴_상품2;

    @BeforeEach
    void setUp() {
        상품1 = new Product("상품1", BigDecimal.valueOf(1_000));
        상품1.updateId(1L);
        상품2 = new Product("상품2", BigDecimal.valueOf(1_000));
        상품2.updateId(2L);

        메뉴_상품1 = new MenuProduct(상품1.getId(), 2);
        메뉴_상품1.updateSeq(1L);
        메뉴_상품2 = new MenuProduct(상품2.getId(), 2);
        메뉴_상품2.updateSeq(2L);
        MenuProduct 메뉴_상품3 = new MenuProduct(상품2.getId(), 1);
        메뉴_상품3.updateSeq(3L);

        메뉴_상품들 = List.of(메뉴_상품1, 메뉴_상품2);

        저장된_메뉴 = new Menu(메뉴_이름, 메뉴_가격, 메뉴_그룹_아이디, 메뉴_상품들);
        저장된_메뉴.updateId(1L);
        메뉴_상품1.updateMenuId(저장된_메뉴.getMenuGroupId());
        메뉴_상품2.updateMenuId(저장된_메뉴.getMenuGroupId());
        저장된_메뉴1 = 저장된_메뉴;
        저장된_메뉴2 = new Menu("메뉴_이름2", BigDecimal.valueOf(5000), 2L, List.of(메뉴_상품1, 메뉴_상품3));
        저장된_메뉴2.updateId(2L);

        MenuProduct 존재하지_않는_상품을_가진_메뉴_상품 = new MenuProduct(-999L, 2);
        존재하지_않는_상품을_가진_메뉴_상품.updateSeq(4L);
        존재하지_않는_상품_가진_메뉴_상품들 = List.of(존재하지_않는_상품을_가진_메뉴_상품);

        저장된_메뉴들 = List.of(저장된_메뉴1, 저장된_메뉴2);
    }
}
