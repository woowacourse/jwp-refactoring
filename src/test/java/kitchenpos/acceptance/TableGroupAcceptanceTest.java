package kitchenpos.acceptance;

import static io.restassured.RestAssured.*;
import static kitchenpos.ui.TableGroupRestController.*;
import static kitchenpos.ui.TableRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupAcceptanceTest extends AcceptanceTest {
    /*
     * Feature: 단체 지정 관리
     *
     * Scenario: 단체 지정을 관리한다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     * When: 주문 테이블을 단체 지정한다.
     * Then: 주문 테이블이 단체 지정된다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     *        주문 테이블이 단체 지정되어 있다.
     * When: 주문 테이블의 단체 지정을 해지한다.
     * Then: 주문 테이블의 단체 지정이 해지된다.
     */
    @DisplayName("단체 지정을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageTableGroup() {
        return Stream.of(
                dynamicTest(
                        "주문 테이블을 단체 지정한다",
                        () -> {
                            // Given
                            final OrderTable orderTable1 = createOrderTable(4, true);
                            final OrderTable orderTable2 = createOrderTable(3, true);

                            // When
                            final TableGroup tableGroup = new TableGroup();
                            tableGroup.setOrderTables(newArrayList(orderTable1, orderTable2));
                            final TableGroup createdTableGroup = create(TABLE_GROUP_REST_API_URI,
                                    tableGroup, TableGroup.class);

                            // Then
                            assertAll(
                                    () -> assertThat(createdTableGroup)
                                            .extracting(TableGroup::getId)
                                            .isNotNull()
                                    ,
                                    () -> assertThat(createdTableGroup)
                                            .extracting(TableGroup::getOrderTables)
                                            .asList()
                                            .usingElementComparatorOnFields("id")
                                            .contains(orderTable1, orderTable2)
                            );
                        }
                ),
                dynamicTest(
                        "단체 지정을 해지한다",
                        () -> {
                            // Given
                            final OrderTable orderTable1 = createOrderTable(3, true);
                            final OrderTable orderTable2 = createOrderTable(4, true);
                            final TableGroup tableGroup = createTableGroup(
                                    newArrayList(orderTable1, orderTable2));

                            // When
                            ungroup(tableGroup.getId());

                            // Then
                            final List<OrderTable> orderTables =
                                    list(TABLE_REST_API_URI, OrderTable.class);

                            assertAll(
                                    () -> assertThat(orderTables)
                                            .filteredOn("id",
                                                    in(orderTable1.getId(), orderTable2.getId()))
                                            .extracting(OrderTable::getTableGroupId)
                                            .containsOnlyNulls()
                                    ,
                                    () -> assertThat(orderTables)
                                            .filteredOn("id",
                                                    in(orderTable1.getId(), orderTable2.getId()))
                                            .extracting(OrderTable::isEmpty)
                                            .doesNotContain(true)
                            );
                        }
                )
        );
    }

    private void ungroup(final Long tableGroupId) {
        // @formatter:off
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
                .delete(TABLE_GROUP_REST_API_URI + "/{tableGroupId}", tableGroupId)
        .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
        ;
        // @formatter:on
    }
}
