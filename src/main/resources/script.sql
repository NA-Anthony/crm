CREATE TABLE taux_alerte (
    id_taux INT AUTO_INCREMENT PRIMARY KEY,
    taux DECIMAL(5, 2) NOT NULL
);

CREATE TABLE budget (
    id_budget INT AUTO_INCREMENT PRIMARY KEY,
    montant DECIMAL(15, 2) NOT NULL,
    id_taux INT NOT NULL,
    id_customer INT(10) UNSIGNED NOT NULL, -- Correspond à customer_id
    FOREIGN KEY (id_taux) REFERENCES taux_alerte(id_taux) ON DELETE CASCADE,
    FOREIGN KEY (id_customer) REFERENCES customer(customer_id) ON DELETE CASCADE
);

CREATE TABLE depense (
    id_depense INT AUTO_INCREMENT PRIMARY KEY, -- Identifiant unique de la dépense
    montant DECIMAL(15, 2) NOT NULL,         -- Montant de la dépense
    id_ticket INT(10) UNSIGNED,               -- Référence à la table trigger_ticket (peut être NULL)
    id_lead INT(10) UNSIGNED,                 -- Référence à la table trigger_lead (peut être NULL)
    date DATE NOT NULL,                       -- Date de la dépense
    CONSTRAINT fk_depense_ticket FOREIGN KEY (id_ticket) REFERENCES trigger_ticket(ticket_id) ON DELETE SET NULL,
    CONSTRAINT fk_depense_lead FOREIGN KEY (id_lead) REFERENCES trigger_lead(lead_id) ON DELETE SET NULL
);

CREATE OR REPLACE VIEW ticket_status_count AS
SELECT customer_id, status, COUNT(*) AS ticket_count
FROM trigger_ticket
GROUP BY customer_id, status;

CREATE OR REPLACE VIEW ticket_priority_count AS
SELECT customer_id, priority, COUNT(*) AS ticket_count
FROM trigger_ticket
GROUP BY customer_id, priority;

