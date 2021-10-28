package kitchenpos.integration;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class TableGroupIntegrationTest extends IntegrationTest {

    private static final String TABLE_GROUP_URL = "/api/table-groups";

    private OrderTable orderTable;

    private OrderTable secondOrderTable;

    @BeforeEach
    void setUp() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        orderTable = orderTableDao.save(table);

        OrderTable secondTable = new OrderTable();
        secondTable.setNumberOfGuests(0);
        secondTable.setEmpty(true);
        secondOrderTable = orderTableDao.save(secondTable);
    }

    @DisplayName("TableGroup 을 생성한다")
    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(
                Arrays.asList(orderTable, secondOrderTable)
        );

        // when
        ResponseEntity<TableGroup> tableGroupResponseEntity = testRestTemplate.postForEntity(
                TABLE_GROUP_URL,
                tableGroup,
                TableGroup.class
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
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(
                Arrays.asList(orderTable, secondOrderTable)
        );
        ResponseEntity<TableGroup> tableGroupResponseEntity = testRestTemplate.postForEntity(
                TABLE_GROUP_URL,
                tableGroup,
                TableGroup.class
        );
        TableGroup createdTableGroup = tableGroupResponseEntity.getBody();
        assertThat(createdTableGroup).isNotNull();

        // when
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                TABLE_GROUP_URL + "/{tableGroupId}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                createdTableGroup.getId()
        );
        HttpStatus statusCode = responseEntity.getStatusCode();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
