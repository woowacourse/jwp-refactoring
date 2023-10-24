package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.TableGroup;
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
        steps.createMenuGroup(MenuGroupFixture.LUNCH.toEntity());
        steps.createTable(OrderTableFixture.EMPTY_TABLE1.toEntity());
        steps.createTable(OrderTableFixture.EMPTY_TABLE2.toEntity());
        steps.createTable(OrderTableFixture.OCCUPIED_TABLE.toEntity());
        steps.createProduct(ProductFixture.FRIED_CHICKEN.toEntity());
        steps.createMenu(MenuFixture.LUNCH_SPECIAL.toEntity());
    }

    @Test
    void create_sucess() {
        // given
        TableGroup expected = TableGroupFixture.TWO_TABLES.toEntity();

        // when
        steps.createTableGroup(expected);
        TableGroup actual = sharedContext.getResponse().as(TableGroup.class);

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
            TableGroup tableGroup = TableGroupFixture.computeDefaultTableGroup(arg ->
                arg.setOrderTables(List.of(OrderTableFixture.EMPTY_TABLE1.toEntity()))
            );

            // when
            steps.createTableGroup(tableGroup);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(500);
        }

        @Test
        void not_empty_table() {
            // given
            TableGroup tableGroup = TableGroupFixture.computeDefaultTableGroup(arg ->
                arg.setOrderTables(List.of(OrderTableFixture.EMPTY_TABLE1.toEntity(), OrderTableFixture.OCCUPIED_TABLE.toEntity()))
            );

            // when
            steps.createTableGroup(tableGroup);
            ExtractableResponse<Response> response = sharedContext.getResponse();

            // then
            assertThat(response.statusCode()).isEqualTo(500);
        }
    }

    @Test
    void upgroup_success() {
        // given
        TableGroup tableGroup = TableGroupFixture.TWO_TABLES.toEntity();
        steps.createTableGroup(tableGroup);
        TableGroup created = TableGroupFixture.TWO_TABLES.toEntity();

        // when
        steps.upgroup(created.getId());
        ExtractableResponse<Response> response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(204);
    }
}
