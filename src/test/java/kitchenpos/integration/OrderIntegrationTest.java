package kitchenpos.integration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class OrderIntegrationTest extends IntegrationTest {

    @DisplayName("주문 목록을 조회한다.")
    @Test
    public void list() {
        webTestClient.get().uri("/api/orders")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Order.class);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    public void create() throws Exception {
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(999L, 1L, 1L, 1)
        );
        OrderTable orderTable = new OrderTable(999L, 10, false);
        OrderTable createdOrderTable = webTestClient.post().uri("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(orderTable), OrderTable.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .returnResult(OrderTable.class)
            .getResponseBody()
            .toStream()
            .findFirst()
            .orElseThrow(() -> new Exception());
        Order order = new Order(createdOrderTable.getId(), "COMPLETION", LocalDateTime.now(), orderLineItems);

        webTestClient.post().uri("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(order), Order.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Order.class);
    }

//    @DisplayName("주문 상태를 수정한다.")
//    @Test
//    public void changeOrderStatus() {
//        List<OrderLineItem> orderLineItems = Arrays.asList(
//            new OrderLineItem(999L, 1L, 1L, 1)
//        );
//        Order order = new Order(5L, "COMPLETE", LocalDateTime.now(), orderLineItems);
//        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
//        TableGroup createdTableGroup = webTestClient.post().uri("/api/table-groups")
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON)
//            .body(Mono.just(tableGroup), TableGroup.class)
//            .exchange()
//            .expectStatus().isCreated()
//            .expectHeader().contentType(MediaType.APPLICATION_JSON)
//            .expectBody(TableGroup.class)
//            .returnResult()
//            .getResponseBody();
//
//        webTestClient.delete()
//            .uri(uriBuilder -> uriBuilder
//                .path("/api/table-groups/{tableGroupId}")
//                .build(createdTableGroup.getId())
//            )
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .expectStatus().isNoContent()
//            .expectBody(TableGroup.class);
//    }
}