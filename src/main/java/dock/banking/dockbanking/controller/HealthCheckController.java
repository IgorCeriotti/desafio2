package dock.banking.dockbanking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementa endpoint para testar saúde da aplicação
 * É utilizado pelo Docker para gerar healthcheck do container
 * Checa se a aplicação está rodando e exposta em localhost:8080
 */
@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping()
    public ResponseEntity<Object> healthCheck() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
