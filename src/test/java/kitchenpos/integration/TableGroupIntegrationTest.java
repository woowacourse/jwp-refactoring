package kitchenpos.integration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class TableGroupIntegrationTest extends IntegrationTest {
    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    public void create() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 1L, 10, false),
            new OrderTable(2L, 2L, 10, false)
        );
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        webTestClient.post().uri("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(tableGroup), TableGroup.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(TableGroup.class);
    }

    @DisplayName("테이블 그룹을 삭제한다.")
    @Test
    public void ungroup() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 1L, 10, false),
            new OrderTable(2L, 2L, 10, false)
        );
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        TableGroup createdTableGroup = webTestClient.post().uri("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(tableGroup), TableGroup.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(TableGroup.class)
            .returnResult()
            .getResponseBody();

        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .path("/api/table-groups/{tableGroupId}")
                .build(createdTableGroup.getId())
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody(TableGroup.class);
    }
}
