package Model;

public class OwnerApplication
{
  private int applicationId;
  private Client clientId;
  private Admin adminId;
  private Date submissionDate;
  private String status;

  private String propertyRegistrationNumber;

  public OwnerApplication(int appId, Client cId,Admin adId, Date d,String s,String number){
    applicationId=appId;
    clientId=cId;
    adminId=adId;
    submissionDate=d;
    status=s;
    propertyRegistrationNumber= number;
  }


  public Admin getAdminId()
  {
    return adminId;
  }

  public int getApplicationId()
  {
    return applicationId;
  }

  public Client getClientId()
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

  public void setClientId(Client clientId)
  {
    this.clientId = clientId;
  }

  public void setAdminId(Admin adminId)
  {
    this.adminId = adminId;
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
