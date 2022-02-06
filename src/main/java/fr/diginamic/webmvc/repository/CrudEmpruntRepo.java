package fr.diginamic.webmvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.diginamic.webmvc.entities.Emprunt;
import fr.diginamic.webmvc.entities.Livre;

@Repository
public interface CrudEmpruntRepo extends CrudRepository<Emprunt, Integer> {

	@Query("select l from Livre l where :emp MEMBER OF l.livreEmprunt")
	public Iterable<Livre> findByLivre(Emprunt emp);
	

}
