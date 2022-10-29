package kitchenpos.ui.dto.request;

public class OrderTableCreationRequest {

   private int numberOfGuests;
   private boolean empty;

   private OrderTableCreationRequest() {}

   public OrderTableCreationRequest(final int numberOfGuests, final boolean empty) {
      this.numberOfGuests = numberOfGuests;
      this.empty = empty;
   }

   public int getNumberOfGuests() {
      return numberOfGuests;
   }

   public boolean isEmpty() {
      return empty;
   }

   @Override
   public String toString() {
      return "OrderTableCreationRequest{" +
              "numberOfGuests=" + numberOfGuests +
              ", empty=" + empty +
              '}';
   }
}
