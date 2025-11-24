package pe.ask.library.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.handler.ChangeLoanStatusHandler;
import pe.ask.library.api.handler.CreateLoanHandler;
import pe.ask.library.api.handler.GetAllLoansByBookIdHandler;
import pe.ask.library.api.handler.GetAllLoansByUserHandler;
import pe.ask.library.api.handler.GetAllLoansHandler;
import pe.ask.library.api.handler.GetLoanByIdHandler;
import pe.ask.library.api.utils.routes.LoanRoutes;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoanRouterRest {

    @Bean
    public RouterFunction<ServerResponse> loanRouterFunction(
            CreateLoanHandler createLoanHandler,
            GetAllLoansHandler getAllLoansHandler,
            GetLoanByIdHandler getLoanByIdHandler,
            ChangeLoanStatusHandler changeLoanStatusHandler,
            GetAllLoansByUserHandler getAllLoansByUserHandler,
            GetAllLoansByBookIdHandler getAllLoansByBookIdHandler
    ) {
        return route(POST(LoanRoutes.CREATE_LOAN).and(accept(MediaType.APPLICATION_JSON)), createLoanHandler::listenPOSTCreateLoanUseCase)
                .andRoute(GET(LoanRoutes.GET_ALL_LOANS).and(accept(MediaType.APPLICATION_JSON)), getAllLoansHandler::listenGETAllLoansUseCase)
                .andRoute(GET(LoanRoutes.GET_LOAN_BY_ID).and(accept(MediaType.APPLICATION_JSON)), getLoanByIdHandler::listenGETLoanByIdUseCase)
                .andRoute(PATCH(LoanRoutes.PATCH_LOAN_STATUS).and(accept(MediaType.APPLICATION_JSON)), changeLoanStatusHandler::listenPATCHChangeLoanStatusUseCase)
                .andRoute(GET(LoanRoutes.GET_ALL_LOANS_BY_USER_ID).and(accept(MediaType.APPLICATION_JSON)), getAllLoansByUserHandler::listenGETAllLoansByUserIdUseCase)
                .andRoute(GET(LoanRoutes.GET_ALL_LOANS_BY_BOOK_ID).and(accept(MediaType.APPLICATION_JSON)), getAllLoansByBookIdHandler::listenGETAllLoansByBookIdUseCase);
    }
}
