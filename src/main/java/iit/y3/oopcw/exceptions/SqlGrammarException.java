package iit.y3.oopcw.exceptions;

public class SqlGrammarException extends RuntimeException{

    private static final long serialVersionUID = 1418507449111709933L;

    public SqlGrammarException() {
        super();
    }

    public SqlGrammarException(final String message) {
        super(message);
    }
}
