package fr.diginamic.webmvc.controllers.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fr.diginamic.webmvc.exceptions.ErrorClient;
import fr.diginamic.webmvc.exceptions.ErrorEmprunt;
import fr.diginamic.webmvc.exceptions.ErrorLivre;

/**
 * @RestControllerAdvice 
 * 
 *
 */
@RestControllerAdvice
public class ErrorController {

	public ErrorController() {

	}

	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String errorGeneralException(Exception e) {
		String message = "Il y a une erreur : " + e.getMessage();
		return message;
	}

	@ExceptionHandler(value = { ErrorClient.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String errorClientException(ErrorClient e) {
		String message = "Erreur lié au client : " + e.getMessage();

		return message;
	}

	@ExceptionHandler(value = { ErrorLivre.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String errorLivreException(ErrorLivre e) {
		String message = "Erreur lié au livre : " + e.getMessage();

		return message;
	}

	@ExceptionHandler(value = { ErrorEmprunt.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String errorEmpruntException(ErrorEmprunt e) {
		String message = "Erreur lié à l'emprunt : " + e.getMessage();
		return message;
	}

}
