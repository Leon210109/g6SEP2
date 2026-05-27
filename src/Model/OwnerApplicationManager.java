package Model;

import Server.OwnerApplicationDAO;
import java.util.ArrayList;

public class OwnerApplicationManager {
  private OwnerApplicationDAO ownerApplicationDAO;

  public OwnerApplicationManager() {
    ownerApplicationDAO = new OwnerApplicationDAO();
  }

  public void addOwnerApplication(OwnerApplication application) {
    ownerApplicationDAO.createApplication(application);
  }

  public OwnerApplication getApplicationById(int applicationId) {
    return ownerApplicationDAO.getApplicationById(applicationId);
  }

  public ArrayList<OwnerApplication> getApplicationsByClientId(int clientId) {
    return ownerApplicationDAO.getApplicationsByClientId(clientId);
  }

  public ArrayList<OwnerApplication> getApplicationsByStatus(String status) {
    return ownerApplicationDAO.getApplicationsByStatus(status);
  }

  public ArrayList<OwnerApplication> getAllApplications() {
    return ownerApplicationDAO.getAllApplications();
  }

  public int getSize() {
    return ownerApplicationDAO.getAllApplications().size();
  }

  // Note: Delete and update operations would need DELETE/UPDATE SQL methods in
  // DAO
  // Keeping these as placeholders for now
  public void removeOwnerApplication(int applicationId) {
    // TODO: Add deleteApplication method to OwnerApplicationDAO
    throw new UnsupportedOperationException("Delete operation not yet implemented in DAO");
  }

  public void updateApplication(OwnerApplication application) {
    // TODO: Add updateApplication method to OwnerApplicationDAO
    throw new UnsupportedOperationException("Update operation not yet implemented in DAO");
  }
}
