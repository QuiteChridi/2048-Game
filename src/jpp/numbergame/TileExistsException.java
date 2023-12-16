package jpp.numbergame;

public class TileExistsException extends RuntimeException{
    public  TileExistsException(){
        super();
    }
    public TileExistsException(String message){
        super(message);
    }
}
