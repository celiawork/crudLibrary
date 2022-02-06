package fr.diginamic.webmvc.controllers;

import javax.validation.Valid;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.diginamic.webmvc.entities.Client;
import fr.diginamic.webmvc.exceptions.ErrorClient;
import fr.diginamic.webmvc.repository.CrudClientRepo;
import fr.diginamic.webmvc.repository.CrudEmpruntRepo;
import fr.diginamic.webmvc.repository.CrudLivreRepo;

@Controller
@RequestMapping("/client")
public class ClientController {

	@Autowired
	CrudClientRepo clientRepo;

	@Autowired
	CrudEmpruntRepo empruntRepo;

	@Autowired
	CrudLivreRepo livreRepo;

	public ClientController() {
		super();
	}

	private void checkClient(Integer pid) throws ErrorClient {
		if (clientRepo.findById(pid).isEmpty()) {
			throw new ErrorClient("N° de client " + pid + " non trouvé");
		}
	}

	/**
	 * Get all clients from DB
	 * @param model
	 * @return template listClient.html
	 */
	@GetMapping("/clients")
	public String findAll(Model model) {

		model.addAttribute("grc", clientRepo);
		model.addAttribute("clients", clientRepo.findAll());
		model.addAttribute("emprunts", empruntRepo.findAll());
		model.addAttribute("chemin_add", "/client/add");
		return "clients/listClient";
	}

	/**
	 * Create a client
	 * @param model
	 * @return template addClient.html
	 */
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("clientForm", new Client());
		model.addAttribute("title_FormAdd_client",  "Ajouter client");
		
		return "clients/addClient";
	}

	/**
	 * Send data of clientForm to the request and save in database
	 * @param model
	 * @param clientForm : form of clients
	 * @return redirect template clientList.html
	 */
	@PostMapping("/add")
	public String addPost(Model model, @Valid @ModelAttribute("clientForm") Client clientForm) {
		clientRepo.save(clientForm);
		return "redirect:/client/clients";
	}

	/**
	 * Delete client by id
	 * @param pid : path Variable id of client
	 * @return redirect template listClient.html
	 * @throws ErrorClient
	 */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer pid) throws ErrorClient {

		checkClient(pid);
		
		clientRepo.getEmpruntByClient(pid).forEach(e -> {

			empruntRepo.findByLivre(e).forEach(l -> {

				l.getLivreEmprunt().remove(e);
				livreRepo.save(l);
			});

			empruntRepo.delete(e);
		});

		clientRepo.deleteById(pid);
		return "redirect:/client/clients";
	}
	
/**
 * Update a client by id
 * @param model
 * @param pid : path Variable id of client
 * @return template updateClient.html
 * @throws ErrorClient
 */
	@GetMapping("/update/{id}")
	public String update(Model model, @PathVariable("id") Integer pid) throws ErrorClient {
		checkClient(pid);
		Client c = clientRepo.findById(pid).get();
		model.addAttribute("client", c);
		model.addAttribute("titre", "modifier un client");
		return "clients/updateClient";
	}
	
	/**
	 * Send a data of clientForm to the request
	 * @param model
	 * @param clientForm : form of client 
	 * @return redirect template listClient.html
	 * @throws ErrorClient
	 */
	@PostMapping("/update")
	public String updatePost(Model model, @Valid @ModelAttribute("client") Client clientForm) throws ErrorClient {
		clientRepo.save(clientForm);
		return "redirect:/client/clients";
	}
	
}
