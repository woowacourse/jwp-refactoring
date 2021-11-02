package kitchenpos.integration;

import static kitchenpos.integration.templates.TableGroupTemplate.TABLE_GROUP_URL;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.factory.OrderTableFactory;
import kitchenpos.factory.TableGroupFactory;
import kitchenpos.integration.annotation.IntegrationTest;
import kitchenpos.integration.templates.OrderTableTemplate;
import kitchenpos.integration.templates.TableGroupTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@IntegrationTest
class TableGroupIntegrationTest {

    @Autowired
    TableGroupTemplate tableGroupTemplate;

    @Autowired
    OrderTableTemplate orderTableTemplate;

    private OrderTable orderTable1;

    private OrderTable orderTable2;

    private OrderTableResponse orderTableResponse1;

    private OrderTableResponse orderTableResponse2;

    @BeforeEach
    void setUp() {
        orderTableResponse1 = orderTableTemplate
            .create(
                0,
                true)
            .getBody();
        orderTableResponse2 = orderTableTemplate
            .create(
                0,
                true)
            .getBody();

        orderTable1 = OrderTableFactory.copy(orderTableResponse1);

        orderTable2 = OrderTableFactory.copy(orderTableResponse2);
    }

    @DisplayName("TableGroup 을 생성한다")
    @Test
    void create() {
        // given // when
        ResponseEntity<TableGroupResponse> tableGroupResponse = tableGroupTemplate
            .create(Arrays.asList(
                orderTable1,
                orderTable2
            ));
        HttpStatus statusCode = tableGroupResponse.getStatusCode();
        URI location = tableGroupResponse.getHeaders().getLocation();
        TableGroupResponse body = tableGroupResponse.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getOrderTableResponses())
            .extracting("id")
            .contains(
                orderTable1.getId(),
                orderTable2.getId()
            );
        assertThat(location).isEqualTo(URI.create(TABLE_GROUP_URL + "/" + body.getId()));
    }

    @DisplayName("TableGroup 을 해제한다")
    @Test
    void ungroup() {
        // given
        TableGroupResponse tableGroupResponse = tableGroupTemplate
            .create(Arrays.asList(
                orderTable1,
                orderTable2))
            .getBody();
        assertThat(tableGroupResponse).isNotNull();
        TableGroup createdTableGroup = TableGroupFactory.copy(tableGroupResponse).build();

        // when
        ResponseEntity<Void> responseEntity = tableGroupTemplate
            .ungroup(createdTableGroup);
        HttpStatus statusCode = responseEntity.getStatusCode();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
