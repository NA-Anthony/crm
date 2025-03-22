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
