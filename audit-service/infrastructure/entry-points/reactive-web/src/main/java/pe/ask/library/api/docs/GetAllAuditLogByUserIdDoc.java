package pe.ask.library.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import pe.ask.library.model.auditlog.AuditLog;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

@Component
@Schema(
        name = "Query AuditLogs by User",
        description = "Documentation for the operation to retrieve all audit logs filtered by userId with pagination."
)
public class GetAllAuditLogByUserIdDoc {

    @Operation(
            operationId = "getAllAuditLogsByUserId",
            summary = "Get AuditLogs for a user",
            description = "Returns a paginated list of audit logs associated with the provided `userId`. " +
                    "Allows specifying the page and page size to control the pagination of the results."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paginated list of AuditLogs",
                    content = @Content(
                            schema = @Schema(
                                    implementation = Pageable.class
                            )
                    )
            )
    })
    public Mono<Pageable<AuditLog>>  getAllAuditLogsByUserIdDoc(
            @Parameter(name = "page", description = "Page number (default: 0)")
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,

            @Parameter(name = "size", description = "Page size (default: 10)")
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,

            @Parameter(name = "userId", description = "Filter by user ID (userId)")
            @RequestParam(name = "userId", required = false) String userId
    ) {
        return Mono.empty();
    }
}