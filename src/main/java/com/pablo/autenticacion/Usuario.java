package com.pablo.autenticacion;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String nombreUsuario;

    @Column
    private String passwordHash;

    @Column
    private String email;

}
