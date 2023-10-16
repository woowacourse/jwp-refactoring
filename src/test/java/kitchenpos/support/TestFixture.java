package kitchenpos.support;

import kitchenpos.domain.*;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.request.ProductRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    public static ProductRequest 상품 = new ProductRequest("신상", new BigDecimal(1));

    public static MenuGroup 메뉴_분류 = new MenuGroup("중식");

    public static Menu 메뉴(List<Product> 등록하려는_상품들, MenuGroup 메뉴그룹) {
        final List<MenuProduct> 메뉴에_속하는_수량이_있는_상품 = new ArrayList<>();
        for (Product product : 등록하려는_상품들) {
            메뉴에_속하는_수량이_있는_상품.add(new MenuProduct(product.getId(), 1));
        }

        BigDecimal 실제_상품_가격과_갯수를_곱한_총합 = BigDecimal.ZERO;
        for (int i = 0; i < 등록하려는_상품들.size(); i++) {
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(등록하려는_상품들.get(i).getPrice().multiply(new BigDecimal(메뉴에_속하는_수량이_있는_상품.get(i).getQuantity())));
        }

        final Menu 신메뉴 = new Menu();
        신메뉴.setName("신메뉴");
        신메뉴.setPrice(실제_상품_가격과_갯수를_곱한_총합);
        신메뉴.setMenuGroupId(메뉴그룹.getId());
        신메뉴.setMenuProducts(메뉴에_속하는_수량이_있는_상품);

        return 신메뉴;
    }

    public static Order 주문(OrderTable 테이블, List<Menu> 주문_할_메뉴들){
        List<OrderLineItem> 주문항목들 = new ArrayList<>();
        for (Menu 메뉴 : 주문_할_메뉴들) {
            주문항목들.add(new OrderLineItem(메뉴.getId(), 1));
        }

        final Order 새로운_주문 = new Order();
        새로운_주문.setOrderTableId(테이블.getId());
        새로운_주문.setOrderLineItems(주문항목들);

        return 새로운_주문;
    }

    public static OrderTable 주문_테이블(){
        final OrderTable 테이블 = new OrderTable();
        테이블.setEmpty(true);
        return 테이블;
    }

    public static OrderTable 빈_테이블(){
        return new OrderTable();
    }

    public static TableGroup 그룹화_테이블(List<OrderTable> 그룹화_할_테이블들){
        final TableGroup 테이블그룹 = new TableGroup();
        테이블그룹.setOrderTables(그룹화_할_테이블들);
        return 테이블그룹;
    }
}
