package library;


 //Something a member can take home and bring back.

public interface Borrowable {
    void borrow(Member member) throws ItemNotAvailableException, MemberLimitExceededException;

    void giveBack();
}
