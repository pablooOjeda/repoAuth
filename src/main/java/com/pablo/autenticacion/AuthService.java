package com.pablo.autenticacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public String hashPassowrd(String password){
        return passwordEncoder.encode(password);
    }

    public String hashear(String queHashear, String conQueClave) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key=new SecretKeySpec(conQueClave.getBytes(), "HmacSHA256");
        mac.init(key);
        byte[] signatureBytes = mac.doFinal(queHashear.getBytes(StandardCharsets.UTF_8));

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(signatureBytes);
    }

    public boolean existeUsuarioSiNo(String nombreUsuario){

        Optional<Usuario> posibleUsuario = usuarioRepository.getUsuarioByNombreUsuario(nombreUsuario);

        if(posibleUsuario.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }


    public Usuario guardarUsuario(String usuario, String password){

        //Comprobar si ya existe

        Usuario usuario1=new Usuario();
        usuario1.setNombreUsuario(usuario);
        usuario1.setPasswordHash(hashPassowrd(password));
        usuarioRepository.save(usuario1);
        return usuario1;
    }

    public Usuario login(String usuario, String password){
        //1 - Comprobar si existia el usuario:
        Optional<Usuario> optionalUsuario = usuarioRepository.getUsuarioByNombreUsuario(usuario);

        if(optionalUsuario.isEmpty()){
            return null;
        }
        else{
            Usuario recuperado=optionalUsuario.get();
            String hashPasswordGuardado= recuperado.getPasswordHash();
            if(passwordEncoder.matches(password, hashPasswordGuardado)){
                return recuperado;
            }
            else{
                return null;
            }
        }
    }


}
