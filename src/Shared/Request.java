package Shared;

import java.io.Serializable;

public class Request implements Serializable
{
  private RequestType requestType;
  private Object[] args;

  public Request(RequestType requestType, Object... args)
  {
    this.requestType = requestType;
    this.args = args;
  }

  public RequestType getRequestType()
  {
    return requestType;
  }

  public Object[] getArgs()
  {
    return args;
  }
}
