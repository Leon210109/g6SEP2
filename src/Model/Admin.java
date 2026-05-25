package Model;


public class Admin {
  private int ID;
  private String username;
  private String password;
  private String adminName;

  public Admin(int ID,String adminName,String username,String password) {
    this.ID = ID;
    this.adminName = adminName;
    this.username=username;
    this.password=password;
  }
  public Admin(String adminName,String username,String password) {
    this.adminName = adminName;
    this.username=username;
    this.password=password;
  }


  public int getID() {
    return ID;
  }



  public void setID(int ID) {
    this.ID = ID;
  }

  public String getUsername()
  {
    return username;
  }

  public String getPassword()
  {
    return password;
  }

  public String getAdminName() {
    return adminName;
  }

  public void setAdminName(String adminName) {
    this.adminName = adminName;
  }

  @Override public String toString()
  {
    return "Admin{" + "ID=" + ID + ", username='" + username + '\''
        + ", password='" + password + '\'' + ", adminName='" + adminName + '\''
        + '}';
  }
}
