package kitchenpos.acceptance;

import static io.restassured.RestAssured.*;
import static kitchenpos.ui.TableRestController.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테이블 인수 테스트")
class TableAcceptanceTest extends AcceptanceTest {
    /*
     * Feature: 주문 테이블 관리
     *
     * Scenario: 주문 테이블을 관리한다.
     *
     * When: 주문 테이블을 등록한다.
     * Then: 주문 테이블이 등록된다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     * When: 주문 테이블의 목록을 조회한다.
     * Then: 저장되어 있는 주문 테이블의 목록이 반환된다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     * When: 주문 테이블을 빈 테이블로 설정한다.
     * Then: 해당 주문 테이블이 빈 테이블로 설정된다.
     *
     * Given: 주문 테이블이 등록되어 있다.
     * When: 주문 테이블의 방문한 손님 수를 설정한다.
     * Then: 해당 주문 테이블의 방문한 손님 수가 설정된다.
     */
    @DisplayName("주문 테이블을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageTable() {
        return Stream.of(
                dynamicTest(
                        "주문 테이블을 등록한다",
                        () -> {
                            // When
                            final OrderTable orderTable = new OrderTable();
                            orderTable.setNumberOfGuests(0);
                            orderTable.setEmpty(true);

                            final OrderTable createdOrderTable = create(TABLE_REST_API_URI,
                                    orderTable, OrderTable.class);

                            // Then
                            assertAll(
                                    () -> assertThat(createdOrderTable)
                                            .extracting(OrderTable::getId)
                                            .isNotNull()
                                    ,
                                    () -> assertThat(createdOrderTable)
                                            .extracting(OrderTable::getNumberOfGuests)
                                            .isEqualTo(orderTable.getNumberOfGuests())
                                    ,
                                    () -> assertThat(createdOrderTable)
                                            .extracting(OrderTable::isEmpty)
                                            .isEqualTo(orderTable.isEmpty())
                            );
                        }
                ),
                dynamicTest(
                        "주문 테이블의 목록을 조회한다",
                        () -> {
                            // Given
                            final OrderTable orderTable = new OrderTable();
                            orderTable.setNumberOfGuests(0);
                            orderTable.setEmpty(true);

                            final OrderTable createdOrderTable = create(TABLE_REST_API_URI,
                                    orderTable, OrderTable.class);

                            // When
                            final List<OrderTable> orderTables = list(TABLE_REST_API_URI,
                                    OrderTable.class);

                            // Then
                            assertThat(orderTables)
                                    .extracting(OrderTable::getId)
                                    .contains(createdOrderTable.getId())
                            ;
                        }
                ),
                dynamicTest(
                        "주문 테이블을 빈 테이블로 설정한다",
                        () -> {
                            // Given
                            final OrderTable orderTable = new OrderTable();
                            orderTable.setNumberOfGuests(0);
                            orderTable.setEmpty(false);

                            final OrderTable createdOrderTable = create(TABLE_REST_API_URI,
                                    orderTable, OrderTable.class);

                            // When
                            final OrderTable emptyOrderTable = new OrderTable();
                            emptyOrderTable.setEmpty(true);

                            final OrderTable changedOrderTable = changeEmpty(
                                    createdOrderTable.getId(), emptyOrderTable);

                            // Then
                            assertAll(
                                    () -> assertThat(changedOrderTable)
                                            .extracting(OrderTable::getId)
                                            .isEqualTo(createdOrderTable.getId())
                                    ,
                                    () -> assertThat(changedOrderTable)
                                            .extracting(OrderTable::isEmpty)
                                            .isEqualTo(true)
                            );
                        }
                ),
                dynamicTest(
                        "주문 테이블의 방문한 손님 수를 설정한다",
                        () -> {
                            // Given
                            final OrderTable orderTable = new OrderTable();
                            orderTable.setNumberOfGuests(0);
                            orderTable.setEmpty(false);

                            final OrderTable createdOrderTable = create(TABLE_REST_API_URI,
                                    orderTable, OrderTable.class);

                            // When
                            final OrderTable setOrderTable = new OrderTable();
                            orderTable.setNumberOfGuests(4);

                            final OrderTable changedOrderTable = changeNumberOfGuests(
                                    createdOrderTable.getId(), setOrderTable);

                            // Then
                            assertAll(
                                    () -> assertThat(changedOrderTable)
                                            .extracting(OrderTable::getId)
                                            .isEqualTo(createdOrderTable.getId())
                                    ,
                                    () -> assertThat(changedOrderTable)
                                            .extracting(OrderTable::getNumberOfGuests)
                                            .isEqualTo(setOrderTable.getNumberOfGuests())
                            );
                        }
                )
        );
    }

    private OrderTable changeEmpty(final Long orderTableId, final OrderTable emptyOrderTable)
            throws JsonProcessingException {
        final String request = objectMapper.writeValueAsString(emptyOrderTable);

        // @formatter:off
        return
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                .when()
                        .put(TABLE_REST_API_URI + "/{orderTableId}/empty", orderTableId)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().as(OrderTable.class);
        // @formatter:on
    }

    private OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable setOrderTable)
            throws JsonProcessingException {
        final String request = objectMapper.writeValueAsString(setOrderTable);

        // @formatter:off
        return
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                .when()
                        .put(TABLE_REST_API_URI + "/{orderTableId}/number-of-guests", orderTableId)
                .then()
                        .log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().as(OrderTable.class);
        // @formatter:on
    }
}
