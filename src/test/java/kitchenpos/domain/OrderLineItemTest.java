package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderLineItemTest {

    @Test
    @DisplayName("OrderLineItem 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Long seq = 1L;
        Order order = Order.create(OrderTable.create(2, false));
        MenuGroup 치킨세트 = MenuGroup.create("치킨세트");
        List<MenuProduct> 상품들 = Arrays.asList(
                MenuProduct.create(Product.create("마늘", BigDecimal.valueOf(1000)), 1L),
                MenuProduct.create(Product.create("치킨", BigDecimal.valueOf(3000)), 1L)
        );
        Menu menu = Menu.create(1L, "마늘치킨", BigDecimal.valueOf(4000), 치킨세트, 상품들);
        Long quantity = 5L;

        //when
        OrderLineItem actual = OrderLineItem.create(seq, order, menu, quantity);

        //then
        assertEquals(actual.getSeq(), seq);
        assertEquals(actual.getOrder(), order);
        assertEquals(actual.getMenu(), menu);
        assertEquals(actual.getQuantity(), quantity);
    }
}
