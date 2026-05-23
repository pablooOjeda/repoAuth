package com.pablo.autenticacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {

    @Autowired
    AuthService authService;

    public String generarJWT(String usuario) throws NoSuchAlgorithmException, InvalidKeyException {
        //Queremos hacer un token JWT
        //Esta compuesto de 3 strings
        String cabecera="{\n" +
                "  \"alg\": \"HS256\",\n" +
                "  \"typ\": \"JWT\"\n" +
                "}";

        String cabecera64 = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(cabecera.getBytes());

        //El body: Usuario + expiracion

        String nombreUsuario=usuario;
        Date expiracion = new Date(System.currentTimeMillis() + 24L * 60 * 60 * 1000);
        String fechaExpiracion= expiracion.toString();


        String body="{\n" +
                "  \"NombreUsuario\": \""+nombreUsuario+"\",\n" +
                "  \"fechaExpiracion\": \""+fechaExpiracion+"\"\n" +
                "}";

        String body64 = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(body.getBytes());

        System.out.println(cabecera);
        System.out.println(body);

        System.out.println(cabecera64);
        System.out.println(body64);

        //Paso 3: el hasheo

        String firma64 = authService.hashear(cabecera64+"."+body64,"DaleAlegriaATuCuerpoMacarena");
        System.out.println(firma64);




        return cabecera64+"."+body64+"."+firma64;
    }

}
