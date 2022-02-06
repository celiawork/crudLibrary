package fr.diginamic.webmvc.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.diginamic.webmvc.entities.Client;
import fr.diginamic.webmvc.entities.Livre;
import fr.diginamic.webmvc.exceptions.ErrorClient;
import fr.diginamic.webmvc.exceptions.ErrorLivre;
import fr.diginamic.webmvc.repository.CrudLivreRepo;

@Controller
@RequestMapping("/livre")
public class LivreController {

	@Autowired
	CrudLivreRepo livreRepo;

	public LivreController() {
		super();

	}
	
	private void checkBook(Integer pid) throws ErrorLivre {
		if (livreRepo.findById(pid).isEmpty()) {
			throw new ErrorLivre("N° du livre " + pid + " non trouvé");
		}
	}

	/**
	 * Get all books from DB
	 * @param model
	 * @return template listLivre.html
	 */
	@GetMapping("/livres")
	public String findAll(Model model) {
		model.addAttribute("livres", (List<Livre>) livreRepo.findAll());
		model.addAttribute("chemin_add", "/livre/add");
		return "livres/listLivre";
	}

	/**
	 * Create a book
	 * @param model
	 * @return addLivre.html
	 */
	@GetMapping("/add")
	public String addT(Model model) {
		model.addAttribute("livreForm", new Livre());
		model.addAttribute("titre", "Ajouter un livre");
		return "livres/addLivre";
	}

	/**
	 *  Send data of livreForm to the request and save in database
	 * @param model
	 * @param livreForm : form of books
	 * @return redirect livreList.html
	 */
	@PostMapping("/add")
	public String add(Model model, @Valid @ModelAttribute("livreForm") Livre livreForm) {
		livreRepo.save(livreForm);
		return "redirect:/livre/livres";

	}

	/**
	 * Delete book by id
	 * @param pid : path Variable id of book
	 * @return redirect template livreList.html
	 * @throws ErrorLivre
	 */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer pid) throws ErrorLivre {
		checkBook(pid);
		livreRepo.deleteById(pid);
		return "redirect:/livre/livres";
	}
	
	/**
	 * Update a book by id
	 * @param model
	 * @param id : path Variable id of book
	 * @return template updateLivre.html
	 * @throws ErrorLivre
	 */
	@GetMapping("/update/{id}")
	public String update(Model model, @PathVariable("id") Integer pid) throws ErrorLivre {
		checkBook(pid);
		Livre c = livreRepo.findById(pid).get();
		model.addAttribute("livre", c);
		model.addAttribute("titre", "Modifier un livre");
		return "livres/updateLivre";
	}
	
	/**
	 * Send a data of clientForm to the request
	 * @param model
	 * @param livreForm : form of book
	 * @return redirect livreList.html
	 * @throws ErrorLivre
	 */
	@PostMapping("/update")
	public String updatePost(Model model, @Valid @ModelAttribute("livre") Livre livreForm) throws ErrorLivre {
		
		livreRepo.save(livreForm);
		
		return "redirect:/livre/livres";
	}
	

}
