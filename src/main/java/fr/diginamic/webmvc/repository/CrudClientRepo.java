package fr.diginamic.webmvc.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.diginamic.webmvc.entities.Client;
import fr.diginamic.webmvc.entities.Emprunt;

@Repository
public interface CrudClientRepo extends CrudRepository<Client, Integer> {

	@Query("select emp from Emprunt emp where emp.clientEmprunt.id = :id")
	public Iterable<Emprunt> getEmpruntByClient(Integer id);
	
}
