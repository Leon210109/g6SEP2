package ViewModel;

import Model.RentalModel;

public class ViewModelFactory
{
  private static ViewModelFactory instance;
  private final RentalModel model;

  private LoginViewModel       loginViewModel;
  private RegistrationViewModel registrationViewModel;
  private ListingViewModel     listingViewModel;
  private BookingViewModel     bookingViewModel;
  private FavoriteViewModel    favoriteViewModel;
  private TenancyViewModel     tenancyViewModel;

  private ViewModelFactory(RentalModel model)
  {
    this.model = model;
  }

  public static synchronized void init(RentalModel model)
  {
    instance = new ViewModelFactory(model);
  }

  public static ViewModelFactory getInstance()
  {
    if (instance == null)
      throw new IllegalStateException("ViewModelFactory not initialised. Call init() first.");
    return instance;
  }

  public RentalModel getModel() { return model; }

  public LoginViewModel getLoginViewModel()
  {
    if (loginViewModel == null) loginViewModel = new LoginViewModel(model);
    return loginViewModel;
  }

  /** Discard the old LoginViewModel so a fresh one is created on next call. */
  public void resetLoginViewModel()
  {
    if (loginViewModel != null) {
      model.removePropertyChangeListener(loginViewModel);
      loginViewModel = null;
    }
  }

  public RegistrationViewModel getRegistrationViewModel()
  {
    if (registrationViewModel == null) registrationViewModel = new RegistrationViewModel(model);
    return registrationViewModel;
  }

  public ListingViewModel getListingViewModel()
  {
    if (listingViewModel == null) listingViewModel = new ListingViewModel(model);
    return listingViewModel;
  }

  public BookingViewModel getBookingViewModel()
  {
    if (bookingViewModel == null) bookingViewModel = new BookingViewModel(model);
    return bookingViewModel;
  }

  public FavoriteViewModel getFavoriteViewModel()
  {
    if (favoriteViewModel == null) favoriteViewModel = new FavoriteViewModel(model);
    return favoriteViewModel;
  }

  public TenancyViewModel getTenancyViewModel()
  {
    if (tenancyViewModel == null) tenancyViewModel = new TenancyViewModel(model);
    return tenancyViewModel;
  }
}
