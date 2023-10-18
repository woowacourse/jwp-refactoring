package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Boolean empty;

    protected OrderTable() {
    }

    private OrderTable(
            Long id,
            TableGroup tableGroup,
            Integer numberOfGuests,
            Boolean empty
    ) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(
            Long id,
            TableGroup tableGroup,
            Integer numberOfGuests,
            Boolean empty
    ) {
        validateNumberOfGuests(numberOfGuests);

        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable createWithoutTableGroup(
            Integer numberOfGuests,
            Boolean empty
    ) {
        validateNumberOfGuests(numberOfGuests);

        return new OrderTable(null, null, numberOfGuests, empty);
    }

    private static void validateNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException();
        }
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        validateChangeableNumberOfGuests(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validateChangeableNumberOfGuests(Integer numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);

        if (empty == Boolean.TRUE) {
            throw new IllegalArgumentException();
        }
    }

    public void changeEmpty(Boolean empty) {
        validateChangeableEmpty();

        this.empty = empty;
    }

    private void validateChangeableEmpty() {
        if (isGrouped()) {
            throw new IllegalArgumentException();
        }
    }

    public void group(TableGroup tableGroup) {
        if (isGrouped()) {
            throw new IllegalArgumentException();
        }

        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public boolean isGrouped() {
        return tableGroup != null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }

}
