package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.common.httpcommunication.OrderTableHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.TableGroupHttpCommunication;
import kitchenpos.common.fixture.RequestBody;
import kitchenpos.ui.dto.response.OrderTableResponse;
import kitchenpos.ui.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("TableGroupAcceptance 는 ")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("TableGroup 을 생성해야 한다.")
    @Test
    void createTableGroup() {
        final OrderTableResponse orderTable1 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                .getResponseBodyAsObject(OrderTableResponse.class);
        final OrderTableResponse orderTable2 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_2)
                .getResponseBodyAsObject(OrderTableResponse.class);
        final ExtractableResponse<Response> response = TableGroupHttpCommunication.create(
                RequestBody.getOrderTableGroups(orderTable1.getId(), orderTable2.getId())).getResponse();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    @DisplayName("TableGroup 에서 특정 OrderTable 을 제외해야 한다.")
    @Test
    void ungroupTable() {
        final OrderTableResponse orderTable1 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                .getResponseBodyAsObject(OrderTableResponse.class);
        final OrderTableResponse orderTable2 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_2)
                .getResponseBodyAsObject(OrderTableResponse.class);
        final TableGroupResponse tableGroup = TableGroupHttpCommunication.create(
                        RequestBody.getOrderTableGroups(orderTable1.getId(), orderTable2.getId()))
                .getResponseBodyAsObject(TableGroupResponse.class);

        final ExtractableResponse<Response> response = TableGroupHttpCommunication.ungroup(tableGroup.getId())
                .getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
