package pe.ask.library.api.utils.routes;

public class LoanRoutes {
    public static final String CREATE_LOAN = "api/v1/loans";
    public static final String GET_ALL_LOANS = "api/v1/loans";
    public static final String GET_LOAN_BY_ID = "api/v1/loans/{loanId}";
    public static final String PATCH_LOAN_STATUS = "api/v1/loans/{loanId}";
    public static final String GET_ALL_LOANS_BY_USER_ID = "api/v1/loans/user/{userId}";
    public static final String GET_ALL_LOANS_BY_BOOK_ID = "api/v1/loans/book/{bookId}";
}
