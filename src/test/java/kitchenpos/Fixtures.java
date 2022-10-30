package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.TableGroupRequest;
import org.assertj.core.api.ListAssert;

@SuppressWarnings("NonAsciiCharacters")
public class Fixtures {

    public static MenuGroup 메뉴그룹_한마리메뉴() {
        return new MenuGroup(null, "한마리메뉴");
    }

    public static MenuGroupRequest 메뉴그룹요청_한마리메뉴() {
        return new MenuGroupRequest("한마리메뉴");
    }

    public static MenuGroupRequest 메뉴그룹요청_두마리메뉴() {
        return new MenuGroupRequest("두마리메뉴");
    }

    public static Product 상품_후라이드() {
        return new Product(null, "후라이드", BigDecimal.valueOf(16000));
    }

    public static ProductRequest 상품요청_후라이드() {
        return new ProductRequest("후라이드", BigDecimal.valueOf(16000));
    }

    public static MenuProductRequest 메뉴상품요청_후라이드() {
        return new MenuProductRequest(1L, 1);
    }

    public static MenuProduct 메뉴상품_후라이드() {
        return new MenuProduct(1L, 1L, 1L, 1);
    }


    public static Menu 메뉴_후라이드치킨() {
        return new Menu(null, "후라이드치킨", BigDecimal.valueOf(16000),
                1L,
                List.of(메뉴상품_후라이드()));
    }

    public static MenuRequest 메뉴요청_후라이드치킨() {
        return new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), 1L,
                List.of(메뉴상품요청_후라이드()));
    }

    public static Menu 메뉴_치킨그룹(MenuProduct... menuProducts) {
        return new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000),
                1L,
                Arrays.asList(menuProducts));
    }

    public static MenuRequest 메뉴요청_치킨그룹(MenuProductRequest... menuProducts) {
        return new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000),
                1L,
                Arrays.asList(menuProducts));
    }


    public static OrderTable 테이블_1(long tableGroupId) {
        return new OrderTable(1L, tableGroupId, 0, false);
    }

    public static OrderTable 테이블_ofId(long id) {
        return new OrderTable(id, null, 0, false);
    }

    public static OrderTableCreateRequest 빈테이블생성요청() {
        return new OrderTableCreateRequest(0, false);
    }

    public OrderTableRequest 주문요청변환(OrderTable 빈테이블_1) {
        return new OrderTableRequest(빈테이블_1.getId());
    }

    public static OrderTable 테이블_1() {
        return new OrderTable(1L, null, 0, false);
    }

    public static OrderTable 테이블() {
        return new OrderTable(null, null, 0, false);
    }

    public static OrderTable 빈테이블() {
        return new OrderTable(null, null, 0, true);
    }

    public static OrderTable 빈테이블_1() {
        return new OrderTable(1L, null, 0, true);
    }

    public static OrderTable 빈테이블_2() {
        return new OrderTable(2L, null, 0, true);
    }

    public static TableGroupRequest 테이블그룹요청(List<OrderTableRequest> tables) {
        return new TableGroupRequest(1L, LocalDateTime.now(), tables);
    }

    public static TableGroupRequest 테이블그룹요청2(List<OrderTableRequest> tables) {
        return new TableGroupRequest(2L, LocalDateTime.now(), tables);
    }

    public static TableGroup 테이블그룹(List<OrderTable> tables) {
        return new TableGroup(1L, LocalDateTime.now(), tables);
    }

    public static TableGroup 테이블그룹2(List<OrderTable> tables) {
        return new TableGroup(2L, LocalDateTime.now(), tables);
    }

    public static OrderLineItem 주문아이템(long menuId) {
        return new OrderLineItem(1L, 1L, menuId, 1L);
    }

    public static OrderLineItem 주문아이템_후라이드() {
        return new OrderLineItem(1L, 1L, 1L, 1L);
    }

    public static Order 주문_테이블1() {
        return new Order(1L, 테이블_1().getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(주문아이템_후라이드()));
    }

    public static Order 주문(long tableId) {
        return new Order(1L, tableId,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(주문아이템_후라이드()));
    }

    public static Order 주문_테이블1(List<OrderLineItem> items) {
        return new Order(1L, 테이블_1().getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                items);
    }

    public static Order 주문_테이블1(OrderStatus status, List<OrderLineItem> items) {
        return new Order(1L, 테이블_1().getId(),
                status.name(),
                LocalDateTime.now(),
                items);
    }

    @SafeVarargs
    public static <ELEMENT> void 검증_필드비교_값포함(ListAssert<ELEMENT> assertThat, ELEMENT... values) {
        assertThat.usingRecursiveFieldByFieldElementComparator()
                .contains(values);
    }

    @SafeVarargs
    public static <ELEMENT> void 검증_필드비교_값포함(List<ELEMENT> list, ELEMENT... values) {
        assertThat(list).usingRecursiveFieldByFieldElementComparator()
                .contains(values);
    }

    public static <ELEMENT> void 검증_필드비교_동일_목록(List<ELEMENT> list, List<ELEMENT> values, String... ignore) {
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i)).usingRecursiveComparison()
                    .ignoringFields(ignore)
                    .isEqualTo(values.get(i));
        }
    }

    public OrderRequest 주문요청_변환(Order order) {
        return new OrderRequest(order.getOrderTableId(), toOrderLineItemRequest(order));
    }

    private List<OrderLineItemRequest> toOrderLineItemRequest(Order savedOrder) {
        return savedOrder.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity())).collect(
                        Collectors.toList());
    }

    public static <D, T> T 변환(D inputObj, Class<T> outputClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(inputObj);
            return objectMapper.readValue(value, outputClass);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
