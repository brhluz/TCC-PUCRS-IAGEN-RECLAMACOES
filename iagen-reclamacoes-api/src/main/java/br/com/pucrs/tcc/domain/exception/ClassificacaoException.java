package br.com.pucrs.tcc.domain.exception;

public class ClassificacaoException extends RuntimeException {
    public ClassificacaoException(String message) {
        super(message);
    }
    public ClassificacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
