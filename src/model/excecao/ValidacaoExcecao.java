package model.excecao;

import java.util.HashMap;
import java.util.Map;

public class ValidacaoExcecao extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Map<String, String> errors = new HashMap<>();

	public ValidacaoExcecao(String msg) {
		super(msg);
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
