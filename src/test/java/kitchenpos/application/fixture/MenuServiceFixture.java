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
    protected BigDecimal 메뉴_가격이_0 = BigDecimal.valueOf(0);
    protected Long 메뉴_그룹_아이디 = 1L;
    protected Long 존재하지_않는_메뉴_그룹_아이디 = -999L;
    protected List<MenuProduct> 메뉴_상품들;
    protected List<MenuProduct> 존재하지_않는_상품_가진_메뉴_상품들;
    protected Menu 저장된_메뉴;

    protected Product 상품1;
    protected Product 상품2;
    protected MenuProduct 메뉴_상품1;
    protected MenuProduct 메뉴_상품2;

    @BeforeEach
    void setUp() {
        상품1 = new Product();
        상품1.setId(1L);
        상품1.setName("상품1");
        상품1.setPrice(BigDecimal.valueOf(1_000));
        상품2 = new Product();
        상품2.setId(2L);
        상품2.setName("상품2");
        상품2.setPrice(BigDecimal.valueOf(1_000));

        메뉴_상품1 = new MenuProduct();
        메뉴_상품1.setSeq(1L);
        메뉴_상품1.setProductId(상품1.getId());
        메뉴_상품1.setQuantity(2);
        메뉴_상품2 = new MenuProduct();
        메뉴_상품2.setSeq(2L);
        메뉴_상품2.setProductId(상품2.getId());
        메뉴_상품2.setQuantity(2);

        메뉴_상품들 = List.of(메뉴_상품1, 메뉴_상품2);

        저장된_메뉴 = new Menu();
        저장된_메뉴.setId(1L);
        저장된_메뉴.setName(메뉴_이름);
        저장된_메뉴.setPrice(메뉴_가격);
        저장된_메뉴.setMenuGroupId(메뉴_그룹_아이디);
        저장된_메뉴.setMenuProducts(메뉴_상품들);
        메뉴_상품1.setMenuId(저장된_메뉴.getMenuGroupId());
        메뉴_상품2.setMenuId(저장된_메뉴.getMenuGroupId());

        MenuProduct 존재하지_않는_상품을_가진_메뉴_상품 = new MenuProduct();
        존재하지_않는_상품을_가진_메뉴_상품.setSeq(1L);
        존재하지_않는_상품을_가진_메뉴_상품.setProductId(-999L);
        존재하지_않는_상품을_가진_메뉴_상품.setQuantity(2);
        존재하지_않는_상품_가진_메뉴_상품들 = List.of(존재하지_않는_상품을_가진_메뉴_상품);
    }
}
