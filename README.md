# AndroidApp
Main android app with UI. Other android related projects will be merged here, when become ready to use
## Architecture
This project uses [Moxy](https://github.com/Arello-Mobile/Moxy) library to structure UI objects related code(kind of [MVP](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) as part of Android adaptation of the [Clean Architecture](https://github.com/android10/Android-CleanArchitecture)). [Cicerone](https://github.com/terrakok/Cicerone) library is used to provide better Activity, Fragment & tab navigation structure. [Greenrobot EventBus](http://greenrobot.org/eventbus/) library is used for passing events between different objects. So, include all of these, there are macro-level structure of application by source code packages:
 1. "application" holds system-level singletones, such as application context and Cicerone navigators.
 2. "event" is a collection of events, which used by EventBus to establish communication between different parts of application.
 3. "models" is a collection of models, which will be produced by Data Layer of the Clean architecture, but currenly created in presenters to display sample data.
 4. "navigation" is home for Cicerone-related stuff for 3-level (Activity, Fragment, tab) navigation.
 5. "persistent_data" holds application constants and SharedPreferences related logic. Maybe database-related stuff will be here in future
 6. "presentation" is Moxy-related package to hold Presenter and ViewState classes
 7. "ui" holds all android UI elements, such as Activity, Fragment, ListView adapters, custom Views and base classes for that.
 8. "utils" holds all components, which used to aggregate some equal features in non-related classes.
## UI structure
Currently, there are 11 main screens in application:
 1. "Main" - application main activity. Displayed with 4 tab sections after user logged in
 2. "Change Pin" - starts, when user clicked such element in settings
 3. "Forgot Pin" - starts, when used presses the **forgot pin** button, when signing in
 4. "Mining" - displays current mining status and other information. Can be displayed every time after login by clicking top panel
 5. "New account" - displayed, when user presses the **new account** button in the registration process
 6. "Registration" - displayed firstly after loading screen, if a user doesn't logged in yet
 7. "Registration finished" - displayed after successfull registration, before loading main screen
 8. "Scan" - displayed, when user wants to scan Qr code from Send tab.
 9. "Sign In" - displayed, when user presses the **sing in** button in registration screen
 10. "Splash" - first application screen, displaying, before application data loading is finished
 11. "Transaction repeat" - displayed, when user wants to repeat existing transaction
## Fragments navigation
### Home tab fragments
  * Balance. Displayed first, displays most important wallet details.
  * Tokens. Displays tokens and jettons information.
### Send tab fragments
  * Send parameters. Displayed firstly when user opens this tab. Used to get target wallet info and ENQ amount.
  * Send finish. Displayed to give user ability for check all parameters before actual sending occurs.
### Receive tab fragments
  * Receive parameters. Similar to send parameters, used to input target wallet.
  * Receive Qr code. Displays Qr code for receive target fragment.
### Settings tab fragments
  * Main settings. Used to display main app settings.
  * Settings backup. Displays data backup related options.
  * About app settings. Displays application version, website etc.
  * Terms settings. Displays terms of use page
### Mining fragments
  * Mining start. First mining fragment, if mining is not started yet.
  * Mining loading. Displaying, when additional nodes loading process is not finished yet.
  * Mining join team. Displayed after team members loading finished.
  * Mining in progress. Displayed, when mining successfully started.
### Registration fragments
  * Create new PIN code fragments
  * Create seed fragments
### Restore PIN fragments
  * Remember seed fragment
  * Restore PIN code fragments
  
    
