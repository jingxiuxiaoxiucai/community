package life.majiang.community.exception;

public class CustomizeException  extends  RuntimeException{
    private    String  message;
    private  Integer code;
    public CustomizeException(ICustomizeErrorCode e)
    {
        this.code =  e.getCode();
        this.message =  e.getMessage();
    }
    @Override
    public String getMessage() {
        return message;
    }
    public Integer getCode() {
        return code;
    }
}
