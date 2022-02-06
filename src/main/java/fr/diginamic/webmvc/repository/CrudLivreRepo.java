package fr.diginamic.webmvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.diginamic.webmvc.entities.Emprunt;
import fr.diginamic.webmvc.entities.Livre;

@Repository
public interface CrudLivreRepo extends CrudRepository<Livre, Integer> {


}
