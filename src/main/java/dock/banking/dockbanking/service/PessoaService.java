package dock.banking.dockbanking.service;

import dock.banking.dockbanking.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    PessoaRepository _pessoaRepository;

    public PessoaRepository get_pessoaRepository() {
        return _pessoaRepository;
    }

    public void set_pessoaRepository(PessoaRepository _pessoaRepository) {
        this._pessoaRepository = _pessoaRepository;
    }
}
