# AndroidApp
Main Android app with UI. Other Android related projects will be merged here when become ready for use.
## Architecture
This project uses [Moxy](https://github.com/Arello-Mobile/Moxy) library to structure UI objects related code(kind of [MVP](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) as part of the Android adaptation of the [Clean Architecture](https://github.com/android10/Android-CleanArchitecture)). [Cicerone](https://github.com/terrakok/Cicerone) library is used to provide better Activity, Fragment & Tab navigation structure. [Greenrobot EventBus](http://greenrobot.org/eventbus/) library is used for passing events between different objects. So, including all of these, there is a macro-level structure of the application by source code packages:
 1. "application" holds system-level singletones, such as application context and Cicerone navigators.
 2. "event" is a collection of events, which are used by the EventBus to establish communication between different parts of the application.
 3. "models" is a collection of models, which will be produced by Data Layer of the Clean Architecture, but currenly created in presenters to display sample data.
 4. "navigation" is home for Cicerone-related stuff for 3-level (Activity, Fragment, Tab) navigation.
 5. "persistent_data" holds application constants and SharedPreferences related logic. Maybe database-related stuff will appear here in the future.
 6. "presentation" is Moxy-related package to hold Presenter and ViewState classes.
 7. "ui" holds all the Android UI elements, such as Activity, Fragment, ListView adapters, custom Views and base classes for those.
 8. "utils" holds all components, which are used to aggregate some equal to the above-mentioned functionality in the classes not related to the above-mentioned.
## UI Structure
Currently, there are 11 main screens in application:
 1. "Main" - application's main activity. it is displayed in 4 tab sections after the user logged in.
 2. "Change Pin" - starts, when the user clicked the respective element in the settings.
 3. "Forgot Pin" - starts, when the user clicks the **forgot pin** button while signing in.
 4. "Mining" - displays current mining status and other information. It can be accessed after the login by clicking on the top panel with the mining button and status info.
 5. "New account" - displayed, when the user presses the **new account** button during the registration process.
 6. "Registration" - displayed firstly after loading screen, if a user doesn't logged in yet
 7. "Registration finished" - displayed after a successfull registration, right before loading the main screen ("Main").
 8. "Scan" - displayed, when user wants to scan a QR code from the Send tab.
 9. "Sign In" - displayed, when the user presses the **sing in** button on the welcome screen.
 10. "Splash" - first application screen displayed before application data loading is finished.
 11. "Transaction repeat" - displayed when the user wants to repeat an existing outgoing transaction.
## Fragments Navigation
### Home Tab Fragments
  * Balance - displayed first after the login. It shows the most important wallet details.
  * Tokens - displays information about Tokens and Jettons.
### Send Tab Fragments
  * Send parameters - displayed first when the user opens the Send Tab to make a transaction. It is used to get target wallet info and the amount of ENQ in posssession.
  * Send finish - displayed to allow the user to check and confirm all the parameters before the actual sending occurs.
### Receive Tab Fragments
  * Receive parameters - similar to Send Parameters. Currently is it also used to create a new wallet and is intended to be renamed in the future and extended with more functionality.
  * Receive QR code - generates and displays the QR code with the address of the wallet to be read by another device. It is used for incoming transactions.
### Settings Tab Fragments
  * Main settings - used to display the main app settings.
  * Settings backup - displays data backup related options.
  * About app settings - displays application version, link to the main website, etc.
  * Terms settings - displays the "Terms of Use" page.
### Mining Fragments
  * Mining start - first mining fragment, if mining is not started yet.
  * Mining loading - displayed during the loading process of available PoA nodes to form the PoA team.
  * Mining join team - displayed after the Poa team members loading has been finished.
  * Mining in progress - displayed when mining has successfully started.
### Registration Fragments
  * Create new PIN - displayed when the user creates new PIN.
  * Create seed - displayed when the user creates a passphrase.
### Restore PIN Fragments
  * Remember seed - displayed when the user wants to restore the password. It asks the user for the passphrase.
  * Restore PIN -  displayed when the user wants to restore the PIN.
    
