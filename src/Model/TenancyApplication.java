package Model;

public class TenancyApplication {

    private int id;
    private int clientId;
    private int listingId;
    private boolean hasPets;
    private String petsDescription;
    private String occupation;
    private int monthlyIncome;
    private int numberOfOccupants;
    private String additionalInfo;
    private String status;

    // Display fields populated by JOIN query
    private String clientName;
    private String clientEmail;
    private String listingAddress;

    // Constructor for new application (no id yet)
    public TenancyApplication(int clientId, int listingId, boolean hasPets, String petsDescription,
                               String occupation, int monthlyIncome, int numberOfOccupants, String additionalInfo) {
        this.clientId = clientId;
        this.listingId = listingId;
        this.hasPets = hasPets;
        this.petsDescription = petsDescription;
        this.occupation = occupation;
        this.monthlyIncome = monthlyIncome;
        this.numberOfOccupants = numberOfOccupants;
        this.additionalInfo = additionalInfo;
        this.status = "pending";
    }

    // Constructor for existing application (from DB)
    public TenancyApplication(int id, int clientId, int listingId, boolean hasPets, String petsDescription,
                               String occupation, int monthlyIncome, int numberOfOccupants,
                               String additionalInfo, String status) {
        this.id = id;
        this.clientId = clientId;
        this.listingId = listingId;
        this.hasPets = hasPets;
        this.petsDescription = petsDescription;
        this.occupation = occupation;
        this.monthlyIncome = monthlyIncome;
        this.numberOfOccupants = numberOfOccupants;
        this.additionalInfo = additionalInfo;
        this.status = status;
    }

    public int getId()               { return id; }
    public int getClientId()         { return clientId; }
    public int getListingId()        { return listingId; }
    public boolean isHasPets()       { return hasPets; }
    public String getPetsDescription() { return petsDescription; }
    public String getOccupation()    { return occupation; }
    public int getMonthlyIncome()    { return monthlyIncome; }
    public int getNumberOfOccupants() { return numberOfOccupants; }
    public String getAdditionalInfo() { return additionalInfo; }
    public String getStatus()        { return status; }
    public String getClientName()    { return clientName; }
    public String getClientEmail()   { return clientEmail; }
    public String getListingAddress() { return listingAddress; }

    public void setStatus(String status)           { this.status = status; }
    public void setClientName(String clientName)   { this.clientName = clientName; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public void setListingAddress(String addr)     { this.listingAddress = addr; }
}
