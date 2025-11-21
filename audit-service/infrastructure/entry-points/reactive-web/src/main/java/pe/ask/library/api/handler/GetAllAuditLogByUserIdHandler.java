package pe.ask.library.api.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.port.in.usecase.auditlog.IGetAllAuditLogByUserIdUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GetAllAuditLogByUserIdHandler {
    private  final IGetAllAuditLogByUserIdUseCase getAllAuditLogByUserIdUseCase;

    public Mono<ServerResponse> listenGETAllAuditLogByUserIdUseCase(ServerRequest serverRequest) {
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        String userId = serverRequest.queryParam("userId").orElse("");
        return getAllAuditLogByUserIdUseCase.execute(userId, page, size)
                .flatMap(response ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response)
                );
    }
}