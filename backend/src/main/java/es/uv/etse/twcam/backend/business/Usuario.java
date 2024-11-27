package es.uv.etse.twcam.backend.business;

public class Usuario {
    String user;
    String password;
    
    public boolean login(String user, String password){
        if(user == "carolina" && password == "123abc"){
            return true;
        }else{
            return false;
        }
    }

    public String registrarse(String nombre, String apellido, String dni, 
                                String user, String password ){
                                    
        return "Registro Exitoso";
                                }
}
