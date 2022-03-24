package dock.banking.dockbanking.controller;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dock.banking.dockbanking.model.Pessoa;
import dock.banking.dockbanking.repository.PessoaRepository;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe responsável por processar REST API requests da pessoa, retornando view que será renderizada em resposta
 */
@RestController
@RequestMapping("/banking/pessoas")
public class PessoaController {

    /**
     * Bean injetado do tipo PessoaRepository
     */
    @Autowired
    PessoaRepository _pessoaRepository;

    /**
     * Path com verbo GET para verificar pessoas cadastradas na BD
     * @return lista de todas as pessoas cadastradas
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de pessoas cadastradas"),
            @ApiResponse(code = 204, message = "Não há pessoas cadastradas"),
            @ApiResponse(code = 500, message = "Ocorreu uma exceção ao realizar o request")
    })
    @GetMapping(value="/all")
    public ResponseEntity<List<Pessoa>> getTodasPessoas() {
        try{
            List<Pessoa> pessoas = new ArrayList<Pessoa>();
            _pessoaRepository.findAll().forEach(pessoas::add);

            if(pessoas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pessoas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
