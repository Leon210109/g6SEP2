package SEP2.SEP2.src.Model;

public class OwnerApplication
{
  private int applicationId;
  private int  clientId;
  private int  adminId;
  private Date submissionDate;
  private String status;
  private String propertyAddress;
  private String propertyRegistrationNumber;

  public String getPropertyAddress()
  {
    return propertyAddress;
  }

  public OwnerApplication(int appId, int cId,int adId, Date d,String s,String a,String number){
    applicationId=appId;
    clientId=cId;
    adminId=adId;
    submissionDate=d;
    status=s;
    propertyAddress=a;
    propertyRegistrationNumber= number;
  }


  public int getAdminId()
  {
    return adminId;
  }

  public int getApplicationId()
  {
    return applicationId;
  }

  public int getClientId()
  {
    return clientId;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public String getPropertyRegistrationNumber()
  {
    return propertyRegistrationNumber;
  }

  public String getStatus()
  {
    return status;
  }

  public void setApplicationId(int applicationId)
  {
    this.applicationId = applicationId;
  }



  public void setSubmissionDate(Date submissionDate)
  {
    this.submissionDate = submissionDate;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public void setPropertyRegistrationNumber(String propertyRegistrationNumber)
  {
    this.propertyRegistrationNumber = propertyRegistrationNumber;
  }
  public String toString() {
    return("Application ID: " + applicationId + "Client ID: "+ clientId + "Admin ID: "+ adminId + "Submission Date: " + submissionDate + "Status: "+status);
  }
}
