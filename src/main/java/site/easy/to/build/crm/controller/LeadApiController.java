package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadApiController {

    private final LeadService leadService;
    private final DepenseService depenseService;

    @Autowired
    public LeadApiController(LeadService leadService, DepenseService depenseService) {
        this.leadService = leadService;
        this.depenseService = depenseService;
    }

    /**
     * Récupère tous les leads.
     *
     * @return Une liste de tous les leads.
     */
    @GetMapping
    public ResponseEntity<List<Lead>> getAllTickets(@RequestParam int userId) {
        List<Lead> leads = leadService.findAssignedLeads(userId);
        return ResponseEntity.ok(leads);
    }

    /**
     * Récupère un lead par son ID.
     *
     * @param id L'ID du lead.
     * @return Le ticket correspondant à l'ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Depense> getTicketById(@PathVariable int id) {
        Depense depense = depenseService.findByLeadId(id);
        if (depense != null) {
            return ResponseEntity.ok(depense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crée un nouveau ticket.
     *
     * @param depense Les données du ticket à créer.
     * @return Le ticket créé.
     */
    @PostMapping
    public ResponseEntity<Depense> createTicket(@RequestBody Depense depense) {
        Depense createdTicket = depenseService.createDepense(depense);
        return ResponseEntity.ok(createdTicket);
    }

    /**
     * Met à jour un ticket existant.
     *
     * @param id     L'ID du ticket à mettre à jour.
     * @param depense Les nouvelles données du ticket.
     * @return Le ticket mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Depense> updateDepense(@PathVariable int id, @RequestBody Depense depense) {
        // Vérifier si la dépense existe
        Depense existingDepense = depenseService.getDepenseById(id);
        if (existingDepense == null) {
            return ResponseEntity.notFound().build(); // Retourner 404 si la dépense n'existe pas
        }

        // Mettre à jour les champs de la dépense existante
        existingDepense.setMontant(depense.getMontant());

        // Appeler la méthode updateDepense du service
        Depense updatedDepense = depenseService.updateDepense(id, existingDepense);

        // Retourner la dépense mise à jour
        return ResponseEntity.ok(updatedDepense);
    }

    /**
     * Supprime un ticket par son ID.
     *
     * @param id L'ID du ticket à supprimer.
     * @return Un statut HTTP indiquant le succès ou l'échec de l'opération.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable int id) {
        Lead lead = leadService.findByLeadId(id);
        Depense depense = depenseService.findByLeadId(id);
        if (lead != null) {
            leadService.delete(lead);
            depenseService.deleteDepense(depense.getIdDepense());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}