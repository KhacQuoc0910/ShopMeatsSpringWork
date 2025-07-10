package org.example.shopmeat2.modals;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Email")
public class Emails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emailID;
    @Column(name = "emailAddress")
    private String emailAddress;

    public Emails() {}

    public Emails(int emailID, String emailAddress) {
        this.emailID = emailID;
        this.emailAddress = emailAddress;
    }

    public int getEmailID() {
        return emailID;
    }

    public void setEmailID(int emailID) {
        this.emailID = emailID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}

