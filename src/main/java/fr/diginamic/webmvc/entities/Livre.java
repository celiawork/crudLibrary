package fr.diginamic.webmvc.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "LIVRE")
public class Livre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotBlank
	@Column(name = "AUTEUR", length = 50, nullable = false)
	private String auteur;

	@NotNull
	@NotBlank
	@Column(name = "TITRE", length = 255, nullable = false)
	private String titre;

	/**
	 * REGLE DE TABLE DE JONCTION N,N 1 Livre -> n Emprunt(s) 1 Emprunt -> n
	 * Livre(s)
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "COMPO", joinColumns = @JoinColumn(name = "ID_LIV", referencedColumnName = "ID"), 
	inverseJoinColumns = @JoinColumn(name = "ID_EMP", referencedColumnName = "ID"))
	private Set<Emprunt> livreEmprunt;

	public Livre() {
		super();
		// TODO Auto-generated constructor stub
		livreEmprunt = new HashSet<Emprunt>();
	}

	public Livre(String auteur, String titre) {
		super();
		this.auteur = auteur;
		this.titre = titre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Set<Emprunt> getLivreEmprunt() {
		return livreEmprunt;
	}

	public void setLivreEmprunt(Set<Emprunt> livreEmprunt) {
		this.livreEmprunt = livreEmprunt;
	}

	@Override
	public String toString() {
		return "Livre [id=" + id + ", auteur=" + auteur + ", titre=" + titre + "]";
	}

}
