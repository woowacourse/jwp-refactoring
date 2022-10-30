package kitchenpos.table.domain;

import java.util.Objects;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Integer numberOfGuests, final Boolean empty){
        this(null, null , numberOfGuests, empty);
    }

    public OrderTable(final Long tableGroupId, final Integer numberOfGuests, final Boolean empty) {
        this(null, tableGroupId , numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        validateNumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    private void validateNumberOfGuests(final Integer numberOfGuests){
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void validateEmptyTable(){
        if (!this.isEmpty()) {
              throw new IllegalArgumentException();
          }
    }

    public void validateNotEmptyTable(){
        if (this.isEmpty()) {
              throw new IllegalArgumentException();
          }
    }

    public void validateNotGroupTable(){
        if(this.getTableGroupId() != null){
            throw new IllegalArgumentException();
        }
    }

    public void addTableId(final Long tableGroupId){
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }
}
