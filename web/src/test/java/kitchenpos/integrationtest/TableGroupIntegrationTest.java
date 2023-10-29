package kitchenpos.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.ordertable.service.TableGroupDto;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupIntegrationTest extends IntegrationTest {

    @BeforeEach
    @Override
    void setUp() {
        super.setUp();
        steps.createMenuGroup(MenuGroupFixture.LUNCH.toDto());
        steps.createTable(OrderTableFixture.EMPTY_TABLE1.toDto());
        steps.createTable(OrderTableFixture.EMPTY_TABLE2.toDto());
        steps.createTable(OrderTableFixture.OCCUPIED_TABLE.toDto());
        steps.createTable(OrderTableFixture.OCCUPIED_TABLE_OF_GROUP1.toDto());
        steps.createProduct(ProductFixture.FRIED_CHICKEN.toDto());
        steps.createMenu(MenuFixture.LUNCH_SPECIAL.toDto());
    }

    @Test
    void create_sucess() {
        // given
        TableGroupDto expected = TableGroupFixture.TABLE_GROUP_AVAILABLE.toDto();

        // when
        steps.createTableGroup(expected);
        TableGroupDto actual = sharedContext.getResponse().as(TableGroupDto.class);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedDate()).isNotNull(),
            () -> assertThat(actual.getOrderTables()).isEqualTo(expected.getOrderTables())
        );
    }

    @Nested
    class create_failure {

        @Test
        void order_table_size() {
            // given
            TableGroupDto tableGroupDto = TableGroupFixture.computeDefaultTableGroupDto(arg ->
                arg.setOrderTables(List.of(OrderTableFixture.EMPTY_TABLE1.toDto()))
            );

            // when
            steps.createTableGroup(tableGroupDto);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(500);
        }

        @Test
        void not_empty_table() {
            // given
            TableGroupDto tableGroupDto = TableGroupFixture.computeDefaultTableGroupDto(arg ->
                arg.setOrderTables(List.of(OrderTableFixture.EMPTY_TABLE1.toDto(), OrderTableFixture.OCCUPIED_TABLE.toDto()))
            );

            // when
            steps.createTableGroup(tableGroupDto);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(500);
        }
    }

    @Test
    void upgroup_success() {
        // given
        TableGroupDto tableGroupDto = TableGroupFixture.TABLE_GROUP_AVAILABLE.toDto();
        steps.createTableGroup(tableGroupDto);

        // when
        steps.upgroup(1L);
        ExtractableResponse<Response> response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(204);
    }
}
