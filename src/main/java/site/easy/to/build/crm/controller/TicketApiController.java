package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketApiController {

    private final TicketService ticketService;
    private final DepenseService depenseService;

    @Autowired
    public TicketApiController(TicketService ticketService, DepenseService depenseService) {
        this.ticketService = ticketService;
        this.depenseService = depenseService;
    }

    /**
     * Récupère tous les tickets.
     *
     * @return Une liste de tous les tickets.
     */
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets(@RequestParam int userId) {
        List<Ticket> tickets = ticketService.findEmployeeTickets(userId);
        return ResponseEntity.ok(tickets);
    }

    /**
     * Récupère un ticket par son ID.
     *
     * @param id L'ID du ticket.
     * @return Le ticket correspondant à l'ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Depense> getTicketById(@PathVariable int id) {
        Depense depense = depenseService.findByTicketId(id);
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
        Ticket ticket = ticketService.findByTicketId(id);
        Depense depense = depenseService.findByTicketId(id);
        if (ticket != null) {
            ticketService.delete(ticket);
            depenseService.deleteDepense(depense.getIdDepense());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}