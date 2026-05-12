package SEP2.SEP2.src.Model;

import java.util.ArrayList;

public class OwnerApplicationManager
{
  private ArrayList<OwnerApplication> applications;


  public OwnerApplicationManager(){
    applications= new ArrayList<>();
  }
  public void addOwnerApplication(OwnerApplication application){
    applications.add(application);
  }
  public int getSize(){
    return applications.size();
  }
  public void removeOwnerApplication(int applicationId){
    for(OwnerApplication application:applications){
      if(application.getApplicationId()==applicationId){
        applications.remove(applicationId);
      }
    }
  }
  public void updateListing(OwnerApplication application){
    for(int i=0; i<applications.size();i++){
      if(applications.get(i).getApplicationId()== application.getApplicationId()){
        applications.set(i,application);
      }
    }
  }


}
