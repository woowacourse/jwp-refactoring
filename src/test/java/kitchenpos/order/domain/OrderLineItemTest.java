package kitchenpos.order.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderLineItemTest {

    @Test
    @DisplayName("OrderLineItem 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Long seq = 1L;
        Long orderTableId = 1L;
        Long 마늘_Id = 1L;
        Long 치킨_Id = 2L;
        Order order = Order.create(orderTableId);
        MenuGroup 치킨세트 = MenuGroup.create("치킨세트");
        List<MenuProduct> 상품들 = Arrays.asList(
                MenuProduct.create(마늘_Id, 1L),
                MenuProduct.create(치킨_Id, 1L)
        );
        Menu menu = Menu.create(1L, "마늘치킨", BigDecimal.valueOf(4000), 치킨세트.getId(), 상품들);
        Long quantity = 5L;

        //when
        OrderLineItem actual = OrderLineItem.create(seq, order, menu.getId(), quantity);

        //then
        assertEquals(actual.getSeq(), seq);
        assertEquals(actual.getOrder(), order);
        assertEquals(actual.getMenuId(), menu.getId());
        assertEquals(actual.getQuantity(), quantity);
    }
}
