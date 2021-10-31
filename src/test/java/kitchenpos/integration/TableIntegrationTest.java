package kitchenpos.integration;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class TableIntegrationTest extends IntegrationTest {

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    public void list() {
        webTestClient.get().uri("/api/tables")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(OrderTable.class);
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    public void create() {
        OrderTable orderTable = new OrderTable(10, true);
        webTestClient.post().uri("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(orderTable), OrderTable.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(OrderTable.class);
    }

    @DisplayName("테이블이 비어있는 지 여부의 상태를 변경한다.")
    @Test
    public void changeEmpty() throws Exception {
        boolean expectedEmpty = false;

        OrderTable orderTable = new OrderTable(10, true);
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

        createdOrderTable.changeEmpty(expectedEmpty);

        webTestClient.put()
            .uri(uriBuilder -> uriBuilder
                .path("/api/tables/{orderTableId}/empty")
                .build(createdOrderTable.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(createdOrderTable), OrderTable.class)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("empty").isEqualTo(false);
    }

    @DisplayName("테이블의 할당 가능한 인원 수를 변경한다.")
    @Test
    public void changeNumberOfGuests() throws Exception {
        int expectedNumberOfGuests = 15;

        OrderTable orderTable = new OrderTable(10, false);
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

        createdOrderTable.assignNumberOfGuests(expectedNumberOfGuests);

        webTestClient.put()
            .uri(uriBuilder -> uriBuilder
                .path("/api/tables/{orderTableId}/number-of-guests")
                .build(createdOrderTable.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(createdOrderTable), OrderTable.class)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("numberOfGuests").isEqualTo(expectedNumberOfGuests);
    }
}
