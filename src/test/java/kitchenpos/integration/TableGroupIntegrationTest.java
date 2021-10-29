package kitchenpos.integration;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.integration.annotation.IntegrationTest;
import kitchenpos.integration.templates.OrderTableTemplate;
import kitchenpos.integration.templates.TableGroupTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static kitchenpos.integration.templates.TableGroupTemplate.TABLE_GROUP_URL;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class TableGroupIntegrationTest {

    @Autowired
    TableGroupTemplate tableGroupTemplate;

    @Autowired
    OrderTableTemplate orderTableTemplate;

    private OrderTable orderTable;

    private OrderTable secondOrderTable;

    @BeforeEach
    void setUp() {
        orderTable = orderTableTemplate
                .create(
                        0,
                        true)
                .getBody();
        secondOrderTable = orderTableTemplate
                .create(
                        0,
                        true)
                .getBody();
    }

    @DisplayName("TableGroup 을 생성한다")
    @Test
    void create() {
        // given // when
        ResponseEntity<TableGroup> tableGroupResponseEntity = tableGroupTemplate
                .create(
                        orderTable,
                        secondOrderTable
                );
        HttpStatus statusCode = tableGroupResponseEntity.getStatusCode();
        URI location = tableGroupResponseEntity.getHeaders().getLocation();
        TableGroup body = tableGroupResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getOrderTables())
                .extracting("id")
                .contains(
                        orderTable.getId(),
                        secondOrderTable.getId()
                );
        assertThat(location).isEqualTo(URI.create(TABLE_GROUP_URL + "/" + body.getId()));
    }

    @DisplayName("TableGroup 을 해제한다")
    @Test
    void ungroup() {
        // given
        TableGroup createdTableGroup = tableGroupTemplate
                .create(
                        orderTable,
                        secondOrderTable)
                .getBody();
        assertThat(createdTableGroup).isNotNull();

        // when
        ResponseEntity<Void> responseEntity = tableGroupTemplate
                .ungroup(createdTableGroup);
        HttpStatus statusCode = responseEntity.getStatusCode();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
