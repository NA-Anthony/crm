package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "depense")
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depense")
    private Integer idDepense;

    @Column(name = "montant", nullable = false)
    private Double montant;

    @ManyToOne
    @JoinColumn(name = "id_ticket", nullable = true) // nullable = true car une dépense peut ne pas être liée à un ticket
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_lead", nullable = true) // nullable = true car une dépense peut ne pas être liée à un lead
    private Lead lead;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    // Constructeurs, getters et setters

    public Depense() {
    }

    public Depense(Double montant, Ticket ticket, Lead lead, LocalDate date) {
        this.montant = montant;
        this.ticket = ticket;
        this.lead = lead;
        this.date = date;
    }

    // Getters et setters
    public Integer getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(Integer idDepense) {
        this.idDepense = idDepense;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}