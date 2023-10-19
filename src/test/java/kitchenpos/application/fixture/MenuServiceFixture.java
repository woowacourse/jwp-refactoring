package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuServiceFixture {

    protected Menu 저장된_메뉴 = new Menu();
    protected Menu 가격이_0보다_작은_메뉴 = new Menu();
    protected Menu 가격이_입력되지_않은_메뉴 = new Menu();
    protected Menu 유효하지_않은_메뉴_그룹_아이디를_갖는_메뉴 = new Menu();
    protected Menu 유효하지_않은_메뉴_상품_아이디를_갖는_메뉴 = new Menu();
    protected Menu 유효하지_않은_가격을_갖는_메뉴 = new Menu();
    protected MenuProduct 첫번째_메뉴_상품 = new MenuProduct();
    protected Product 첫번째_메뉴_상품의_상품 = new Product();
    protected List<Menu> 저장된_메뉴_리스트;

    public void 메뉴를_생성한다_픽스처_생성() {
        첫번째_메뉴_상품의_상품 = new Product();
        첫번째_메뉴_상품의_상품.setId(1L);
        첫번째_메뉴_상품의_상품.setPrice(BigDecimal.valueOf(20_000));

        첫번째_메뉴_상품.setSeq(1L);
        첫번째_메뉴_상품.setMenuId(1L);
        첫번째_메뉴_상품.setProductId(첫번째_메뉴_상품의_상품.getId());
        첫번째_메뉴_상품.setQuantity(1L);

        저장된_메뉴.setId(1L);
        저장된_메뉴.setName("후라이드");
        저장된_메뉴.setPrice(BigDecimal.valueOf(19_000));
        저장된_메뉴.setMenuGroupId(1L);
        저장된_메뉴.setMenuProducts(List.of(첫번째_메뉴_상품));

        저장된_메뉴_리스트 = List.of(저장된_메뉴);
    }

    protected void 전달_받은_메뉴의_가격이_입력되지_않았다면_예외가_발생한다_픽스처_생성() {
        가격이_입력되지_않은_메뉴.setPrice(null);
    }

    protected void 전달_받은_메뉴의_가격이_0보다_작은_경우_예외가_발생한다_픽스처_생성() {
        가격이_0보다_작은_메뉴.setPrice(BigDecimal.valueOf(-1L));
    }

    protected void 유효하지_않은_메뉴_그룹_아이디를_전달_받으면_예외가_발생한다_픽스처_생성() {
        final long 유효하지_않은_그룹_메뉴_아이디 = -999L;
        유효하지_않은_메뉴_그룹_아이디를_갖는_메뉴.setName("후라이드");
        유효하지_않은_메뉴_그룹_아이디를_갖는_메뉴.setPrice(BigDecimal.valueOf(19_000));
        유효하지_않은_메뉴_그룹_아이디를_갖는_메뉴.setMenuGroupId(유효하지_않은_그룹_메뉴_아이디);
        유효하지_않은_메뉴_그룹_아이디를_갖는_메뉴.setMenuProducts(List.of(첫번째_메뉴_상품));
    }

    protected void 유효하지_않은_메뉴_상품_아이디를_전달_받으면_예외가_발생한다_픽스처_생성() {
        final MenuProduct 유효하지_않은_상품 = new MenuProduct();
        final long 유효하지_않은_상품_아이디 = -999L;
        유효하지_않은_상품.setProductId(유효하지_않은_상품_아이디);
        유효하지_않은_메뉴_상품_아이디를_갖는_메뉴.setId(-999L);
        유효하지_않은_메뉴_상품_아이디를_갖는_메뉴.setName("후라이드");
        유효하지_않은_메뉴_상품_아이디를_갖는_메뉴.setPrice(BigDecimal.valueOf(19_000));
        유효하지_않은_메뉴_상품_아이디를_갖는_메뉴.setMenuGroupId(1L);
        유효하지_않은_메뉴_상품_아이디를_갖는_메뉴.setMenuProducts(List.of(유효하지_않은_상품));
    }

    protected void 메뉴의_가격이_메뉴에_포함된_상품_가격을_합친_것보다_작은_경우_예외가_발생한다_픽스처_생성() {
        final Product 첫_번째_상품 = new Product();
        final Product 두_번째_상품 = new Product();
        첫_번째_상품.setId(2L);
        두_번째_상품.setId(3L);
        첫_번째_상품.setPrice(BigDecimal.valueOf(10_000));
        두_번째_상품.setPrice(BigDecimal.valueOf(20_000));

        final MenuProduct 첫_번째_메뉴_상품 = new MenuProduct();
        final MenuProduct 두_번째_메뉴_상품 = new MenuProduct();
        첫_번째_메뉴_상품.setProductId(첫_번째_상품.getId());
        첫_번째_메뉴_상품.setQuantity(1L);
        두_번째_메뉴_상품.setQuantity(2L);

        첫번째_메뉴_상품의_상품 = new Product();
        첫번째_메뉴_상품의_상품.setId(1L);
        첫번째_메뉴_상품의_상품.setPrice(BigDecimal.valueOf(20_000));

        첫번째_메뉴_상품.setSeq(1L);
        첫번째_메뉴_상품.setMenuId(1L);
        첫번째_메뉴_상품.setProductId(첫번째_메뉴_상품의_상품.getId());
        첫번째_메뉴_상품.setQuantity(1L);

        final BigDecimal 유효하지_않은_메뉴_가격 = BigDecimal.ZERO;

        유효하지_않은_가격을_갖는_메뉴.setId(1L);
        유효하지_않은_가격을_갖는_메뉴.setName("후라이드");
        유효하지_않은_가격을_갖는_메뉴.setPrice(유효하지_않은_메뉴_가격);
        유효하지_않은_가격을_갖는_메뉴.setMenuGroupId(1L);
        유효하지_않은_가격을_갖는_메뉴.setMenuProducts(List.of(첫_번째_메뉴_상품, 두_번째_메뉴_상품));
    }

    protected void 모든_메뉴를_조회한다_픽스처_생성() {
        첫번째_메뉴_상품의_상품 = new Product();
        첫번째_메뉴_상품의_상품.setId(1L);
        첫번째_메뉴_상품의_상품.setPrice(BigDecimal.valueOf(20_000));

        첫번째_메뉴_상품.setSeq(1L);
        첫번째_메뉴_상품.setMenuId(1L);
        첫번째_메뉴_상품.setProductId(첫번째_메뉴_상품의_상품.getId());
        첫번째_메뉴_상품.setQuantity(1L);

        저장된_메뉴.setId(1L);
        저장된_메뉴.setName("후라이드");
        저장된_메뉴.setPrice(BigDecimal.valueOf(19_000));
        저장된_메뉴.setMenuGroupId(1L);
        저장된_메뉴.setMenuProducts(List.of(첫번째_메뉴_상품));

        저장된_메뉴_리스트 = List.of(저장된_메뉴);
    }
}
