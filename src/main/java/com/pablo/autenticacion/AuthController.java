package com.pablo.autenticacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequestMapping(value = "/auth")
@RestController
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    JWTService jwtService;

    //Ademas, tiene que ANTES comprobar si ese usuario existe
    @PostMapping(value = "/registrar")
    private ResponseEntity<Usuario> registrar(@RequestParam String usuario, @RequestParam String password){
        if(authService.existeUsuarioSiNo(usuario)==true){
            return new ResponseEntity<Usuario>(HttpStatus.CONFLICT);
        }
        else{
            Usuario usuario1 = authService.guardarUsuario(usuario ,password);
            usuario1.setPasswordHash(null);
            return new ResponseEntity<Usuario>(usuario1,HttpStatus.OK);
        }
    }

    @GetMapping(value = "/JWT")
    private String jwt() throws NoSuchAlgorithmException, InvalidKeyException {
        jwtService.generarJWT("Pepon");
        return "Hecho";
    }


    @PostMapping(value = "/login")
    private ResponseEntity<String> login(@RequestParam String usuario, @RequestParam String password) throws NoSuchAlgorithmException, InvalidKeyException {

       Usuario usuarioLogueado=authService.login(usuario, password);

       if(usuarioLogueado!=null){
           return new ResponseEntity<String>(jwtService.generarJWT(usuarioLogueado.getNombreUsuario()),HttpStatus.OK);
       }
       else{
           return new ResponseEntity<String>("USUARIO INCORRECTO", HttpStatus.UNAUTHORIZED);
       }


    }

}
