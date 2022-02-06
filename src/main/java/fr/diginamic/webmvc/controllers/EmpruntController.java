package fr.diginamic.webmvc.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.diginamic.webmvc.entities.Emprunt;
import fr.diginamic.webmvc.exceptions.ErrorEmprunt;
import fr.diginamic.webmvc.repository.CrudClientRepo;
import fr.diginamic.webmvc.repository.CrudEmpruntRepo;
import fr.diginamic.webmvc.repository.CrudLivreRepo;

@Controller
@RequestMapping("/emprunt")
public class EmpruntController {

	@Autowired
	CrudClientRepo clientRepo;

	@Autowired
	CrudEmpruntRepo empruntRepo;

	@Autowired
	CrudLivreRepo livreRepo;

	public EmpruntController() {
		super();
	}

	private void checkEmprunt(Integer pid) throws ErrorEmprunt {
		if (empruntRepo.findById(pid).isEmpty()) {
			throw new ErrorEmprunt("N° de l' emprunt " + pid + " non trouvé");
		}
	}

	/**
	 * Get all loans from DB
	 * @param model
	 * @return template listeEmprunt.html
	 */
	@GetMapping("/emprunts")
	public String findAll(Model model) {
		
		model.addAttribute("emprunts", empruntRepo.findAll());

		model.addAttribute("grc", clientRepo);
		model.addAttribute("empr", empruntRepo);
		model.addAttribute("clients", clientRepo.findAll());
		model.addAttribute("livres", livreRepo.findAll());
		model.addAttribute("chemin_add", "/emprunt/add");
		
		
		model.addAttribute("titre", "Liste des emprunts");
		return "emprunts/listEmprunt";
	}

	/**
	 * Create a loan
	 * @param model
	 * @return template addEmprunt.html
	 */
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("empruntForm", new Emprunt());
		model.addAttribute("livres", livreRepo.findAll());
		model.addAttribute("clients", clientRepo.findAll());
		model.addAttribute("titre", "Ajouter un emprunt");
		return "emprunts/addEmprunt";
	}

	/**
	 * Send data of empruntForm to the request and save in database
	 * @param model
	 * @param empruntForm : form of loan 
	 * @return redirect template empruntList.html
	 */
	@PostMapping("/add")
	public String add(Model model, @Valid @ModelAttribute("empruntForm") Emprunt empruntForm) {
		
		empruntRepo.save(empruntForm);
		empruntForm.getLivresE().forEach(l -> {
			l.getLivreEmprunt().add(empruntForm);
			livreRepo.save(l);
		});
		
		return "redirect:/emprunt/emprunts";
	}

	/**
	 * Delete a loan by id
	 * @param pid : path variable id of loan
	 * @return redirect template empruntList.html
	 * @throws ErrorEmprunt
	 */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer pid) throws ErrorEmprunt {
		
		checkEmprunt(pid);

		Emprunt e = empruntRepo.findById(pid).get();
		empruntRepo.findByLivre(e).forEach(l -> {
			l.getLivreEmprunt().remove(e);
			livreRepo.save(l);
		});
		empruntRepo.delete(e);
		return "redirect:/emprunt/emprunts";
	}
	
	/**
	 * Update a client by id
	 * @param model
	 * @param pid : path Variable id of loan
	 * @return template updateEmprunt.html
	 * @throws ErrorEmprunt
	 */
	@GetMapping("/update/{id}")
	public String update(Model model, @PathVariable("id") Integer pid) throws ErrorEmprunt {
		checkEmprunt(pid);
		Emprunt e = empruntRepo.findById(pid).get();
		model.addAttribute("emprunt", e);
		model.addAttribute("livres", livreRepo.findAll());
		model.addAttribute("clients", clientRepo.findAll());
		return "emprunts/updateEmprunt";
	}
	
	/**
	 * Send a data of empruntForm to the request
	 * @param model
	 * @param empruntForm : data of loans
	 * @return redirect empruntList.html
	 * @throws ErrorEmprunt
	 */
	@PostMapping("/update")
	public String updatePost(Model model, @Valid @ModelAttribute("emprunt") Emprunt empruntForm) throws ErrorEmprunt {
		
		//BUG DU REMOVE
		empruntRepo.save(empruntForm);
		empruntRepo.findByLivre(empruntForm).forEach(l -> {
			Set <Emprunt> se = new HashSet<>();
			l.getLivreEmprunt().forEach(e ->{
				if (e.getId() != empruntForm.getId()) se.add(e);
			});
			l.setLivreEmprunt(se);
			livreRepo.save(l);
		});
		empruntForm.getLivresE().forEach(l -> {
			
			l.getLivreEmprunt().add(empruntForm);
			livreRepo.save(l);
		});
	
		
		return "redirect:/emprunt/emprunts";
	}

}
