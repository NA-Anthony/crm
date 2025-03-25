package site.easy.to.build.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.annotation.concurrent.Immutable;

@Entity
@Immutable
@Table(name = "ticket_status_count")
public class TicketStatusCount {

    @Id
    private String status;

    @Column(name = "ticket_count")
    private int ticketCount;

    @Column(name = "customer_id")
    private int customerId;

    // Getters et Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

}