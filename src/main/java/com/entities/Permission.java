package com.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Vinit Solanki
 *
 */
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity implements Serializable, GrantedAuthority {

    private static final long serialVersionUID = -5404269148967698143L;
    
    @NotNull
    @NotEmpty
    @Size(max = 50)
    @Column(name = "permissionname", length = 50)
    private String permissionName;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",   
        joinColumns        = {@JoinColumn(name = "permission_id", referencedColumnName = "id")},  
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}  
    )  
    private Set<Role> permRoles;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return permissionName;
    }
    @JsonIgnore
    public Set<Role> getPermRoles() {
        return permRoles;
    }

    public void setPermRoles(Set<Role> permRoles) {
        this.permRoles = permRoles;
    }

    @Override
    public String toString() {
        return String.format("%s(id=%d, permissionname='%s')", 
                this.getClass().getSimpleName(), 
                this.getId(), this.getPermissionName());
    }

}
