package services;

public class Err {

    private Error error;

    public Err(int status){
        if (status == 400){
            this.error = new Error(400, "Error: bad request");
        }
        else if (status == 403){
            this.error = new Error(403, "Error: already taken");
        }
        else if (status == 401){
            this.error = new Error(401, "Error: unauthorized");
        }
        else{
            this.error = new Error(500, "Error: description");
        }

    }

    public Error getError(){
        return this.error;
    }


}
