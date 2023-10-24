package kitchenpos.support;

import kitchenpos.domain.TableGroup;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.ui.dto.request.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    public static ProductRequest 상품 = new ProductRequest("신상", new BigDecimal(1));

    public static MenuGroupRequest 메뉴_분류 = new MenuGroupRequest("중식");

    public static MenuRequest 메뉴(List<Product> 등록하려는_상품들, MenuGroup 메뉴그룹) {
        final List<MenuProductRequest> 메뉴에_속하는_수량이_있는_상품 = new ArrayList<>();
        for (Product product : 등록하려는_상품들) {
            메뉴에_속하는_수량이_있는_상품.add(new MenuProductRequest(product.getId(), 1));
        }

        BigDecimal 실제_상품_가격과_갯수를_곱한_총합 = BigDecimal.ZERO;
        for (int i = 0; i < 등록하려는_상품들.size(); i++) {
            실제_상품_가격과_갯수를_곱한_총합 = 실제_상품_가격과_갯수를_곱한_총합.add(등록하려는_상품들.get(i).getPrice().multiply(new BigDecimal(메뉴에_속하는_수량이_있는_상품.get(i).getQuantity())));
        }

        return new MenuRequest("신메뉴", 실제_상품_가격과_갯수를_곱한_총합, 메뉴그룹.getId(), 메뉴에_속하는_수량이_있는_상품);
    }

    public static OrderRequest 주문(OrderTable 테이블, List<Menu> 주문_할_메뉴들) {
        List<OrderLineItemRequest> 주문항목들 = new ArrayList<>();
        for (Menu 메뉴 : 주문_할_메뉴들) {
            주문항목들.add(new OrderLineItemRequest(메뉴.getId(), 1));
        }
        return new OrderRequest(테이블.getId(), 주문항목들);
    }

    public static CreateOrderTableRequest 주문_테이블() {
        return new CreateOrderTableRequest(1);
    }

    public static TableGroup 그룹화_테이블(List<OrderTable> 그룹화_할_테이블들) {
        final TableGroup 테이블그룹 = new TableGroup();
        테이블그룹.setOrderTables(그룹화_할_테이블들);
        return 테이블그룹;
    }
}
