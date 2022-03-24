package dock.banking.dockbanking.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * POJO que representa dados a serem persistidos no banco de dados, na tabela Pessoas
 */
@Entity
@Table(name = "Pessoas")
public class Pessoa {

    /**
     * Identificador único no BD da pessoa
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Nome da pessoa (máximo de 50 caracteres)
     */
    @Column(name = "nome")
    private String nome;

    /**
     * CPF da pessoa, sem '.' ou '-' (máximo 12 caracteres)
     */
    @Column(name = "cpf")
    private String cpf;

    /**
     * Data de nascimento da pessoa, formato yyyy-mm-dd
     */
    @Column(name = "nascimento")
    private Date nascimento;

    public Pessoa() {}

    public Pessoa(String nome, String cpf, Date dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.nascimento = dataNascimento;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNascimento() {
        return this.nascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.nascimento = dataNascimento;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nome='" + getNome() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            "}";
    }
    
}
