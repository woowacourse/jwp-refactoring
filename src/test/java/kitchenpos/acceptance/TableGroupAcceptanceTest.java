package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import kitchenpos.acceptance.common.httpcommunication.MenuGroupHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.MenuHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.OrderHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.OrderTableHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.ProductHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.TableGroupHttpCommunication;
import kitchenpos.common.fixture.RequestBody;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.menu.ui.dto.response.MenuGroupResponse;
import kitchenpos.menu.ui.dto.response.MenuResponse;
import kitchenpos.order.ui.dto.response.OrderResponse;
import kitchenpos.order.ui.dto.response.OrderTableResponse;
import kitchenpos.product.ui.dto.response.ProductResponse;
import kitchenpos.order.ui.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("TableGroupAcceptance 는 ")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹을 생성할 떄 ")
    @Nested
    class TableGroupCreationTest extends AcceptanceTest {

        @DisplayName("생성 조건을 만족하면 TableGroup 을 생성해야 한다.")
        @Test
        void createTableGroupSuccess() {
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

        @DisplayName("생성 조건을 만족하지 않으면 에러를 응답한다.")
        @Test
        void createTableGroupFail() {
            final OrderTableResponse orderTable1 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                    .getResponseBodyAsObject(OrderTableResponse.class);
            final ExtractableResponse<Response> response = TableGroupHttpCommunication.create(
                    RequestBody.getOrderTableGroups(orderTable1.getId(), 0L)).getResponse();

            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    @DisplayName("TableGroup 에서 특정 OrderTable 을 제외할 때 ")
    @Nested
    class UngroupTableTest extends AcceptanceTest {

        @DisplayName("테이블들의 주문 상태가 COMPLETION이면 TableGroup 에서 특정 OrderTable 을 제외해야 한다.")
        @Test
        void ungroupTableSuccess() {
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

        @DisplayName("테이블 주문 상태가 COMPLETION이 아니면 에러를 반환한다.")
        @Test
        void ungroupTableFail() {
            final ProductResponse product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                    .getResponseBodyAsObject(ProductResponse.class);
            final MenuGroupResponse menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                    .getResponseBodyAsObject(MenuGroupResponse.class);
            final MenuResponse menu = MenuHttpCommunication.create(
                            RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()))
                    .getResponseBodyAsObject(MenuResponse.class);

            final OrderTableResponse orderTable1 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                    .getResponseBodyAsObject(OrderTableResponse.class);
            final OrderTableResponse orderTable2 = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_2)
                    .getResponseBodyAsObject(OrderTableResponse.class);
            final TableGroupResponse tableGroupResponse = TableGroupHttpCommunication.create(
                            RequestBody.getOrderTableGroups(orderTable1.getId(), orderTable2.getId()))
                    .getResponseBodyAsObject(TableGroupResponse.class);

            final OrderResponse order = OrderHttpCommunication.create(
                            RequestBody.getOrder(menu.getId(), orderTable1.getId()))
                    .getResponseBodyAsObject(OrderResponse.class);
            OrderHttpCommunication.changeOrderStatus(order.getId(), Map.of("ORDER_STATUS", "MEAL"));

            final ExtractableResponse<Response> response = TableGroupHttpCommunication.ungroup(
                            tableGroupResponse.getId())
                    .getResponse();

            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
