package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.common.httpcommunication.OrderTableHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.TableGroupHttpCommunication;
import kitchenpos.acceptance.common.fixture.RequestBody;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("TableGroupAcceptance 는 ")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("TableGroup 을 생성해야 한다.")
    @Test
    void createTableGroup() {
        OrderTable orderTable1 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                .getResponseBodyAsObject(OrderTable.class);
        OrderTable orderTable2 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_2)
                .getResponseBodyAsObject(OrderTable.class);
        ExtractableResponse<Response> response = TableGroupHttpCommunication.create(
                        RequestBody.getOrderTableGroups(orderTable1.getId(), orderTable2.getId())) .getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("TableGroup 에서 특정 OrderTable 을 제외해야 한다.")
    @Test
    void ungroupTable() {
        OrderTable orderTable1 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                .getResponseBodyAsObject(OrderTable.class);
        OrderTable orderTable2 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_2)
                .getResponseBodyAsObject(OrderTable.class);
        TableGroup tableGroup = TableGroupHttpCommunication.create(
                        RequestBody.getOrderTableGroups(orderTable1.getId(), orderTable2.getId()))
                .getResponseBodyAsObject(TableGroup.class);

        ExtractableResponse<Response> response = TableGroupHttpCommunication.ungroup(tableGroup.getId())
                .getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
