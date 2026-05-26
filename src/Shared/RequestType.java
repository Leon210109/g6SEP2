package Shared;

public enum RequestType
{
  // ── Auth ──
  Login,
  Register_Client,

  // ── Listings ──
  Get_All_Listings,
  Get_Available_Listings,
  Get_Listings_By_Owner,
  Add_Listing,
  Remove_Listing,
  Update_Listing,

  // ── Bookings ──
  Make_Booking,
  Remove_Booking,
  Update_Booking,
  Get_All_Booking,
  Get_Bookings_By_Client,

  // ── Owner Applications ──
  Add_Owner_Application,
  Remove_Owner_Application,
  Update_Owner_Application,
  Get_All_Owner_Applications,
  Approve_Owner_Application,
  Reject_Owner_Application,

  // ── Favorites ──
  Get_Favorites_By_Client,
  Add_Favorite,
  Remove_Favorite,

  // ── Tenancy Applications ──
  Submit_Tenancy_Application,
  Get_Tenancy_Apps_By_Owner,
  Get_Tenancy_Apps_By_Client,
  Delete_Tenancy_Application,
  Update_Tenancy_Application_Status,

  // ── Account ──
  Delete_Client,
  Delete_Property_Owner,

  // ── Lookup ──
  Get_Owner_By_Id
}
