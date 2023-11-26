
package th.mfu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Tenant tenant;
    @ManyToOne
    private Dormitory dormitory;
    
    //constructors and getters and setters
    public WishList()
    {

    }
    public Tenant getTenant() {
        return tenant;
    }
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    public Dormitory getDormitory() {
        return dormitory;
    }
    public void setDormitory(Dormitory dormitory) {
        this.dormitory = dormitory;
    }
    
}
