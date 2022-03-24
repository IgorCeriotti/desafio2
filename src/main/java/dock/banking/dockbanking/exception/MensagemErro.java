package dock.banking.dockbanking.exception;

import java.util.Date;

/**
 * Classe de modelo e padronização para mensagens de erro
 */
public class MensagemErro {

    /**
     * Status da resposta para a requisição HTTP
     */
    private int statusCode;

    /**
     * Data e horário que aconteceu o erro
     */
    private Date timestamp;

    /**
     * Mensagem a ser apresentada na resposta
     */
    private String mensagem;

    /**
     * Descrição do erro a ser apresentada na resposta
     */
    private String descricao;

    public MensagemErro(int statusCode, Date timestamp, String mensagem, String descricao) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.mensagem = mensagem;
        this.descricao = descricao;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
