package table.bd.models;

// import javafx.beans.property.SimpleStringProperty;

import java.math.BigInteger;

import javax.persistence.*;

@Entity
@Table(name = "people")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone" )
    private String phone;

    @Column(name = "email")
    private String email;

    public Person(BigInteger id, String firstName, String lastName, String phone, String email) {
        this.id = id;
        this.firstName = new String(firstName);
        this.lastName = new String(lastName);
        this.phone = new String(phone);
        this.email = new String(email);
    }

    public Person copy() {
        return new Person(this.id, this.firstName, this.lastName, this.phone, this.email);
    }

    public BigInteger getId() {
        return this.id;
    }

    // public void setId(String id) {
    //     this.id.set(id);
    // }

    public String getFirstName() {
        return this.firstName;
    }

    // public void setFirstName(String firstName) {
    //     this.firstName.set(firstName);
    // }

    public String getLastName() {
        return this.lastName;
    }

    // public void setLastName(String lastName) {
    // this.lastName.set(lastName);
    // }

    public String getPhone() {
        return this.phone;
    }

    // public void setPhone(String phone) {
    //     this.phone.set(phone);
    // }

    public String getEmail() {
        return this.email;
    }

    // public void setEmail(String email) {
    // this.email.set(email);
    // }
}
