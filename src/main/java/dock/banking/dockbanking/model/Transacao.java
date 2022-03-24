package dock.banking.dockbanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * POJO que representa dados a serem persistidos no banco de dados, na tabela Transacao
 */
@Entity
@Table(name = "Transacao")
public class Transacao {

    /**
     * Identificador único no BD da transacao
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idTransacao;

    /**
     * Conta vinculada à transação.
     * Mapeado conforme modelagem do BD.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idConta", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Conta conta;

    /**
     * Valor > 0, em reais, da operação.
     */
    @Column(name = "valor")
    private BigDecimal valor;

    /**
     * Data em que foi realizada a operação
     */
    @Column(name = "dataTransacao")
    @CreationTimestamp
    private Timestamp dataTransacao;

    public Transacao() {}

    public Transacao(long idTransacao, Conta conta, BigDecimal valor, Timestamp dataTransacao) {
        this.idTransacao = idTransacao;
        this.conta = conta;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
    }

    public long getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(long idTransacao) {
        this.idTransacao = idTransacao;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Timestamp getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Timestamp dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "idTransacao=" + idTransacao +
                ", conta=" + conta +
                ", valor=" + valor +
                ", dataTransacao=" + dataTransacao +
                '}';
    }
}
