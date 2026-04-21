package com.nagarseva.backend.entity;

import com.nagarseva.backend.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean active;

    private String department;

    @OneToMany(mappedBy = "createdBy")
    private List<Complaint> createdComplaints;

    @OneToMany(mappedBy = "assignedTo")
    private List<Complaint> assignedComplaints;

}
