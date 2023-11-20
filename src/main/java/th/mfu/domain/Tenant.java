package th.mfu.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tenant{
    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String password;

    //relationship

    public Tenant(){

    }
    

    public Tenant(String firstName, String lastName,String email,  String gender, String phone,String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.password=password;
    }

    
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    
}