package com.culturacarabobo.sicuc.backend.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.culturacarabobo.sicuc.backend.entities.ArtCategory;
import com.culturacarabobo.sicuc.backend.entities.ArtDiscipline;
import com.culturacarabobo.sicuc.backend.entities.Municipality;
import com.culturacarabobo.sicuc.backend.entities.Parish;
import com.culturacarabobo.sicuc.backend.entities.Role;
import com.culturacarabobo.sicuc.backend.entities.User;
import com.culturacarabobo.sicuc.backend.repositories.ArtCategoryRepository;
import com.culturacarabobo.sicuc.backend.repositories.ArtDisciplineRepository;
import com.culturacarabobo.sicuc.backend.repositories.MunicipalityRepository;
import com.culturacarabobo.sicuc.backend.repositories.ParishRepository;
import com.culturacarabobo.sicuc.backend.repositories.UserRepository;

@Component
@Profile("dev") // Only run this component in the 'dev' profile
public class DatabaseSeeder implements CommandLineRunner {

    // Dependency injection for repository access
    private final MunicipalityRepository municipalityRepository;
    private final ParishRepository parishRepository;
    private final ArtCategoryRepository artCategoryRepository;
    private final ArtDisciplineRepository artDisciplineRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(MunicipalityRepository municipalityRepository, ParishRepository parishRepository,
            ArtCategoryRepository artCategoryRepository, ArtDisciplineRepository artDisciplineRepository,
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.municipalityRepository = municipalityRepository;
        this.parishRepository = parishRepository;
        this.artCategoryRepository = artCategoryRepository;
        this.artDisciplineRepository = artDisciplineRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // Populate database with seed data on application startup
        List<Municipality> municipalities = seedMunicipalities();
        seedParishes(municipalities);
        List<ArtCategory> artCategories = seedArtCategories();
        seedArtDisciplines(artCategories);
        seedUsers();

    }

    /**
     * Seeds municipalities if the repository is empty.
     *
     * @return a list of inserted municipalities or null if already seeded
     */
    @SuppressWarnings("null")
    private List<Municipality> seedMunicipalities() {

        List<Municipality> municipalities = List.of(
                new Municipality("Bejuma"),
                new Municipality("Carlos Arvelo"),
                new Municipality("Diego Ibarra"),
                new Municipality("Guacara"),
                new Municipality("Juan José Mora"),
                new Municipality("Libertador"),
                new Municipality("Los Guayos"),
                new Municipality("Miranda"),
                new Municipality("Montalbán"),
                new Municipality("Naguanagua"),
                new Municipality("Puerto Cabello"),
                new Municipality("San Diego"),
                new Municipality("San Joaquín"),
                new Municipality("Valencia"));
        if (municipalityRepository.count() == 0) {
            municipalityRepository.saveAll(municipalities);
            return municipalities;
        }
        return null;

    }

    /**
     * Seeds parishes linked to the given municipalities if none exist.
     *
     * @param municipalities list of parent municipalities
     */
    @SuppressWarnings("null")
    private void seedParishes(List<Municipality> municipalities) {
        if (municipalities != null) {
            List<Parish> parishes = List.of(
                    new Parish("Bejuma", municipalities.get(0)),
                    new Parish("Canoabo", municipalities.get(0)),
                    new Parish("Simón Bolívar", municipalities.get(0)),
                    new Parish("Güigüe", municipalities.get(1)),
                    new Parish("Belén", municipalities.get(1)),
                    new Parish("Tacarigua", municipalities.get(1)),
                    new Parish("Mariara", municipalities.get(2)),
                    new Parish("Aguas Calientes", municipalities.get(2)),
                    new Parish("Guacara", municipalities.get(3)),
                    new Parish("Yagua", municipalities.get(3)),
                    new Parish("Ciudad Alianza", municipalities.get(3)),
                    new Parish("Morón", municipalities.get(4)),
                    new Parish("Urama", municipalities.get(4)),
                    new Parish("Tocuyito", municipalities.get(5)),
                    new Parish("Independencia", municipalities.get(5)),
                    new Parish("Los Guayos", municipalities.get(6)),
                    new Parish("Miranda", municipalities.get(7)),
                    new Parish("Montalbán", municipalities.get(8)),
                    new Parish("Naguanagua", municipalities.get(9)),
                    new Parish("Bartolomé Salom", municipalities.get(10)),
                    new Parish("Borburata", municipalities.get(10)),
                    new Parish("Democracia", municipalities.get(10)),
                    new Parish("Fraternidad", municipalities.get(10)),
                    new Parish("Goaigoaza", municipalities.get(10)),
                    new Parish("Juan José Flores", municipalities.get(10)),
                    new Parish("Patanemo", municipalities.get(10)),
                    new Parish("Unión", municipalities.get(10)),
                    new Parish("San Diego", municipalities.get(11)),
                    new Parish("San Joaquín", municipalities.get(12)),
                    new Parish("Candelaria", municipalities.get(13)),
                    new Parish("Catedral", municipalities.get(13)),
                    new Parish("El Socorro", municipalities.get(13)),
                    new Parish("Miguel Peña", municipalities.get(13)),
                    new Parish("Rafael Urdaneta", municipalities.get(13)),
                    new Parish("San Blas", municipalities.get(13)),
                    new Parish("San José", municipalities.get(13)),
                    new Parish("Santa Rosa", municipalities.get(13)),
                    new Parish("Negro Primero", municipalities.get(13)));
            if (parishRepository.count() == 0) {
                parishRepository.saveAll(parishes);
            }
        }
    }

    /**
     * Seeds art categories if the repository is empty.
     *
     * @return a list of inserted art categories or null if already seeded
     */
    @SuppressWarnings("null")
    private List<ArtCategory> seedArtCategories() {

        List<ArtCategory> artCategories = List.of(
                new ArtCategory("Artes Plásticas"),
                new ArtCategory("Artesanía"),
                new ArtCategory("Audiovisuales"),
                new ArtCategory("Danza"),
                new ArtCategory("Literatura"),
                new ArtCategory("Música"),
                new ArtCategory("Teatro"));
        if (artCategoryRepository.count() == 0) {
            artCategoryRepository.saveAll(artCategories);
            return artCategories;
        }
        return null;

    }

    /**
     * Seeds art disciplines linked to the given categories if none exist.
     *
     * @param artCategories list of parent art categories
     */
    @SuppressWarnings("null")
    private void seedArtDisciplines(List<ArtCategory> artCategories) {
        if (artCategories != null) {
            List<ArtDiscipline> disciplines = List.of(
                    new ArtDiscipline("Cestería", artCategories.get(0)),
                    new ArtDiscipline("Dulcería criolla", artCategories.get(0)),
                    new ArtDiscipline("Luthería", artCategories.get(0)),
                    new ArtDiscipline("Muñequería", artCategories.get(0)),
                    new ArtDiscipline("Prendas", artCategories.get(0)),
                    new ArtDiscipline("Tejido", artCategories.get(0)),
                    new ArtDiscipline("Otra...", artCategories.get(0)),
                    new ArtDiscipline("Cerámica", artCategories.get(1)),
                    new ArtDiscipline("Escultura", artCategories.get(1)),
                    new ArtDiscipline("Instalación", artCategories.get(1)),
                    new ArtDiscipline("Orfebrería", artCategories.get(1)),
                    new ArtDiscipline("Pintura", artCategories.get(1)),
                    new ArtDiscipline("Otra...", artCategories.get(1)),
                    new ArtDiscipline("Cine", artCategories.get(2)),
                    new ArtDiscipline("Experimental", artCategories.get(2)),
                    new ArtDiscipline("Fotografía", artCategories.get(2)),
                    new ArtDiscipline("Videoarte", artCategories.get(2)),
                    new ArtDiscipline("Otra...", artCategories.get(2)),
                    new ArtDiscipline("Ballet", artCategories.get(3)),
                    new ArtDiscipline("Contemporánea", artCategories.get(3)),
                    new ArtDiscipline("Moderna", artCategories.get(3)),
                    new ArtDiscipline("Urbana", artCategories.get(3)),
                    new ArtDiscipline("Tradicional", artCategories.get(3)),
                    new ArtDiscipline("Otra...", artCategories.get(3)),
                    new ArtDiscipline("Crónica", artCategories.get(4)),
                    new ArtDiscipline("Cuento", artCategories.get(4)),
                    new ArtDiscipline("Ensayo", artCategories.get(4)),
                    new ArtDiscipline("Novela", artCategories.get(4)),
                    new ArtDiscipline("Poesía", artCategories.get(4)),
                    new ArtDiscipline("Otra...", artCategories.get(4)),
                    new ArtDiscipline("Clásica o académica", artCategories.get(5)),
                    new ArtDiscipline("Experimental", artCategories.get(5)),
                    new ArtDiscipline("Fusión", artCategories.get(5)),
                    new ArtDiscipline("Llanera", artCategories.get(5)),
                    new ArtDiscipline("Popular", artCategories.get(5)),
                    new ArtDiscipline("Rock", artCategories.get(5)),
                    new ArtDiscipline("Tradicional", artCategories.get(5)),
                    new ArtDiscipline("Urbana", artCategories.get(5)),
                    new ArtDiscipline("Otra...", artCategories.get(5)),
                    new ArtDiscipline("Circo", artCategories.get(6)),
                    new ArtDiscipline("Clown", artCategories.get(6)),
                    new ArtDiscipline("Mimo", artCategories.get(6)),
                    new ArtDiscipline("Teatro", artCategories.get(6)),
                    new ArtDiscipline("Títeres", artCategories.get(6)),
                    new ArtDiscipline("Otra...", artCategories.get(6)));
            if (artDisciplineRepository.count() == 0) {
                artDisciplineRepository.saveAll(disciplines);
            }
        }
    }

    /**
     * Seeds default users if the repository is empty.
     */
    @SuppressWarnings("null")
    private void seedUsers() {
        List<User> users = List.of(
                new User("admin", passwordEncoder.encode("admin"), Role.ROLE_ADMIN));
        if (userRepository.count() == 0) {
            userRepository.saveAll(users);
        }
    }

}