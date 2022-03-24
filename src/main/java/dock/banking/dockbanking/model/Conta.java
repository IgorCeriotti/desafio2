package dock.banking.dockbanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * POJO que representa dados a serem persistidos no banco de dados, na tabela Conta
 */
@Entity
@Table(name = "Conta")
public class Conta {

    /**
     * Identificador único no BD da conta
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idConta;

    /**
     * Pessoa vinculada à conta.
     * Mapeado conforme modelagem do BD.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idPessoa", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Pessoa pessoa;

    /**
     * Quantidade, em reais, disponível na conta
     */
    @Column(name = "saldo")
    private BigDecimal saldo;

    /**
     * Quantidade máxima por dia, em reais, para realização de saques
     */
    @Column(name = "limiteSaqueDiario")
    private BigDecimal limiteSaqueDiario;

    /**
     * Flag que indica se conta está ativa, ou não
     */
    @Column(name = "flagAtivo")
    private boolean flagAtivo;

    /**
     * Indica o tipo de conta (conta corrente, poupança)
     */
    @Column(name = "tipoConta")
    private Integer tipoConta;

    /**
     * Indica quando a conta foi criada
     */
    @Column(name = "dataCriacao")
    @CreationTimestamp
    private Timestamp dataCriacao;

    public Conta() {}

    public Conta(long id, Pessoa pessoa, BigDecimal saldo, BigDecimal limiteSaqueDiario, boolean flagAtivo, Integer tipoConta, Timestamp dataCriacao) {
        this.idConta = id;
        this.pessoa = pessoa;
        this.saldo = saldo;
        this.limiteSaqueDiario = limiteSaqueDiario;
        this.flagAtivo = flagAtivo;
        this.tipoConta = tipoConta;
        this.dataCriacao = dataCriacao;
    }

    public long getId() {
        return idConta;
    }

    public void setId(long id) {
        this.idConta = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getLimiteSaqueDiario() {
        return limiteSaqueDiario;
    }

    public void setLimiteSaqueDiario(BigDecimal limiteSaqueDiario) {
        this.limiteSaqueDiario = limiteSaqueDiario;
    }

    public boolean isFlagAtivo() {
        return flagAtivo;
    }

    public void setFlagAtivo(boolean flagAtivo) {
        this.flagAtivo = flagAtivo;
    }

    public Integer getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(Integer tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Timestamp getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "id=" + idConta +
                ", pessoa=" + pessoa +
                ", saldo=" + saldo +
                ", limiteSaqueDiario=" + limiteSaqueDiario +
                ", flagAtivo=" + flagAtivo +
                ", tipoConta=" + tipoConta +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}
