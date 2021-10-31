package kitchenpos.integration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class TableGroupIntegrationTest extends IntegrationTest {

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    public void create() {
        List<OrderTable> orderTables = Arrays.asList(
            fixtureMaker.createOrderTableForEmpty(),
            fixtureMaker.createOrderTableForEmpty()
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
        TableGroup tableGroup = fixtureMaker.createTableGroup();
        webTestClient.delete()
            .uri(uriBuilder -> uriBuilder
                .path("/api/table-groups/{tableGroupId}")
                .build(tableGroup.getId())
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody(TableGroup.class);
    }
}
