package Shared;

import java.io.Serializable;

public class Response implements Serializable
{
  private boolean success;
  private Object object;
  private String message;

  public Response(boolean success, String message,Object object)
  {
    this.success = success;
    this.object = object;
    this.message = message;
  }

  public Object getObject()
  {
    return object;
  }

  public boolean isSuccess()
  {
    return success;
  }

  public String getMessage()
  {
    return message;
  }
}
